package org.burningokr.service.okr;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.burningokr.model.okr.Revision;
import org.burningokr.model.okr.RevisionValueType;
import org.burningokr.model.revision.IRevisionInformation;
import org.burningokr.service.userhandling.UserService;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RevisionService<R extends IRevisionInformation> {

  private final UserService userService;

  protected void setEntityManager(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  private EntityManager entityManager;

  @Autowired
  public RevisionService(UserService userService, EntityManager entityManager) {
    this.userService = userService;
    this.entityManager = entityManager;
  }

  private AuditReader getAuditReader() {
    return AuditReaderFactory.get(entityManager);
  }

  public <T> Collection<Revision> getRevisions(long objectId, Class<T> cls) {
    AuditQuery auditQuery = getAuditReader().createQuery().forRevisionsOfEntity(cls, false, true);
    auditQuery.add(AuditEntity.id().eq(objectId));
    auditQuery.addOrder(AuditEntity.revisionNumber().asc());
    @SuppressWarnings("unchecked")
    List<Object[]> resultList = auditQuery.getResultList();
    List<Revision> revisionList = new LinkedList<>();
    T lastRevision = null;
    for (Object[] revision : resultList) {
      @SuppressWarnings("unchecked")
      T currentRevision = (T) revision[0];
      R revisionInformation = (R) revision[1];
      RevisionType revisionType = (RevisionType) revision[2];
      compare(revisionList, revisionInformation, revisionType, lastRevision, currentRevision, cls);
      lastRevision = currentRevision;
    }
    Collections.reverse(revisionList);
    return revisionList;
  }

  protected <T> void compare(
      List<Revision> revisionList,
      R revInfo,
      RevisionType revisionType,
      T lastRevision,
      T currentRevision,
      Class<T> classType) {

    Field[] auditedFields = getAuditedFields(classType);
    Map<String, Field[]> auditedReferencedFields = getReferencedAuditedFields(auditedFields);

    List<Field> changedFields = getChangedFields(auditedFields, lastRevision, currentRevision);

    for (int i = changedFields.size() - 1; i >= 0; i--) {

      Field field = changedFields.get(i);
      Revision revision = new Revision();
      if (i == 0) {
        revision.setDate(revInfo.getDate());
        if (revInfo.getUserId() != null)
          revision.setUser(revInfo.getUserId());
        revision.setRevisionType(revisionType);
      }
      revision.setChangedField(field.getName());

      Object oldValue = getValueByGetter(lastRevision, field);
      Object newValue = getValueByGetter(currentRevision, field);
      if (storesCollectionOfUUIDs(field)) {
        revision.setRevisionValueType(RevisionValueType.USER_COLLECTION);
        @SuppressWarnings("unchecked")
        Collection<UUID> oldUserList = (Collection<UUID>) oldValue;
        if (oldUserList == null) {
          oldUserList = Collections.EMPTY_LIST;
        }
        @SuppressWarnings("unchecked")
        Collection<UUID> newUserList = (Collection<UUID>) newValue;
        if (newUserList == null) {
          newUserList = Collections.EMPTY_LIST;
        }
        revision.setOldValue(oldUserList);
        revision.setNewValue(newUserList);
      } else {
        revision.setRevisionValueType(RevisionValueType.STRING);
        if (isBurningOkrField(field)) {
          Field[] referencedFields = auditedReferencedFields.get(field.getName());
          revision.setOldValue(getObjectStringByFieldList(referencedFields, oldValue));
          revision.setNewValue(getObjectStringByFieldList(referencedFields, newValue));
        } else {
          revision.setOldValue(Objects.toString(oldValue, ""));
          revision.setNewValue(Objects.toString(newValue, ""));
        }
      }

      // Validate, that the value has visibly changed, otherwise discard (e.g. NULL -> empty
      // string)
      if (!Objects.equals(revision.getOldValue(), revision.getNewValue())) {
        revisionList.add(revision);
      }
    }
  }

  protected String getObjectStringByFieldList(Field[] fields, Object object) {
    if (fields.length > 1) {
      List<String> values = new LinkedList<>();
      for (Field f : fields) {
        values.add(f.getName() + ": " + getValueByGetter(object, f));
      }
      values.add("-- TO BE IMPLEMENTED WITH MORE SENSE --");
      return String.join("\n", values);
    } else {
      return Objects.toString(getValueByGetter(object, fields[0]), "");
    }
  }

  protected boolean storesCollectionOfUUIDs(Field field) {
    return field.getType().equals(Collection.class)
        && ((ParameterizedType) field.getGenericType())
            .getActualTypeArguments()[0].equals(UUID.class);
  }

  protected <T> List<Field> getChangedFields(
      Field[] fieldsToCompare, T firstVersion, T secondVersion) {
    List<Field> changedFields = new LinkedList<>();
    for (Field field : fieldsToCompare) {
      Object firstValue = getValueByGetter(firstVersion, field);
      Object secondValue = getValueByGetter(secondVersion, field);
      if (!Objects.equals(firstValue, secondValue)) {
        changedFields.add(field);
      }
    }
    return changedFields;
  }

  protected Map<String, Field[]> getReferencedAuditedFields(Field[] auditedFields) {
    return Arrays.stream(auditedFields)
        .filter(this::isBurningOkrField)
        .map(f -> new AbstractMap.SimpleEntry<>(f.getName(), getAuditedFields(f.getType())))
        .collect(
            Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));
  }

  protected boolean isBurningOkrField(Field f) {
    return f.getType().getCanonicalName().startsWith("org.burningokr.");
  }

  protected <T> Field[] getAuditedFields(Class<T> classType) {
    return Arrays.stream(classType.getDeclaredFields())
        .filter(field -> field.isAnnotationPresent(Audited.class))
        .toArray(Field[]::new);
  }

  protected Object getValueByGetter(Object objectWithFields, Field fieldToRetrieve) {
    if (objectWithFields == null) {
      return null;
    }
    Object fieldValue = null;
    try {
      Optional<Method> getterMethod =
          Arrays

              // Liste der Methoden
              .stream(objectWithFields.getClass().getMethods())

              // Filter der Methode auf..
              .filter(
                  possibleMethod ->

                      // ..Name beginnt mit ‚get‘
                      possibleMethod.getName().startsWith("get")
                          &&

                          // ..der weitere Name passt zum angefragten Feld
                          possibleMethod
                              .getName()
                              .substring(3)
                              .equalsIgnoreCase(fieldToRetrieve.getName())
                          &&

                          // ..hat keine Parameter
                          possibleMethod.getParameterCount() == 0)

              // Und von dieser Menge die erste Methode
              .findFirst();

      // Aufrufen, falls eine solche Methode gefunden worden ist.
      fieldValue = getterMethod.get().invoke(objectWithFields);

      // Abfangen von Fehlern (Aufruf scheitert, nicht gefunden, ..)
    } catch (Exception e) {
      log.error(
        String.format(
          "Error finding/calling getter for field '%s' on %s --> %s",
          fieldToRetrieve.getName(),
          objectWithFields,
          e));
    }

    return fieldValue;
  }
}
