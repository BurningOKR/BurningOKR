package org.burningokr.service.okr;

import jakarta.persistence.EntityManager;
import org.burningokr.model.okr.FieldRevision;
import org.burningokr.model.okr.RevisionValueType;
import org.burningokr.model.revision.RevisionInformation;
import org.burningokr.model.revision.TypedRevisionEntry;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;

import static org.burningokr.service.helpers.revisionService.RevisionServiceHelper.*;

@Service
public class RevisionService{
  private final EntityManager entityManager;

  @Autowired
  public RevisionService(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  public <T> Collection<FieldRevision> getRevisions(long objectId, Class<T> cls) {
    List<TypedRevisionEntry<T>> revisions = getTypedRevisions(objectId, cls);

    List<FieldRevision> fieldRevisionList = new LinkedList<>();
    TypedRevisionEntry<T> lastRevision = null;
    for (TypedRevisionEntry<T> revision : revisions) {
      compare(fieldRevisionList, revision, lastRevision, cls);
      lastRevision = revision;
    }

    Collections.reverse(fieldRevisionList);
    return fieldRevisionList;
  }

  private <T> void compare(
      List<FieldRevision> fieldRevisionList,
      TypedRevisionEntry<T> currentRevisionData,
      TypedRevisionEntry<T> beforeRevisionData,
      Class<T> classType) {
    T currentRevision = currentRevisionData.revision;
    T lastRevision = beforeRevisionData.revision;

    Field[] auditedFields = getAuditedFields(classType);
    Map<String, Field[]> auditedReferencedFields = getReferencedAuditedFields(auditedFields);

    List<Field> changedFields = getChangedFields(auditedFields, lastRevision, currentRevision);

    for (int i = changedFields.size() - 1; i >= 0; i--) {

      Field field = changedFields.get(i);
      FieldRevision fieldRevision = new FieldRevision();
      if (i == 0) {
        setRevisionGeneralInfo(currentRevisionData.revisionInformation, currentRevisionData.revisionType, fieldRevision);
      }
      fieldRevision.setChangedField(field.getName());

      Object oldValue = getValueByGetter(lastRevision, field);
      Object newValue = getValueByGetter(currentRevision, field);
      if (storesCollectionOfUUIDs(field)) {
        fieldRevision.setRevisionValueType(RevisionValueType.USER_COLLECTION);
        setOldUserList(fieldRevision, (Collection<UUID>) oldValue);
        setNewUserList(fieldRevision, (Collection<UUID>) newValue);
      } else {
        fieldRevision.setRevisionValueType(RevisionValueType.STRING);
        setOldAndNewValuesAsString(auditedReferencedFields, field, fieldRevision, oldValue, newValue);
      }

      if (isValueDifferent(fieldRevision.getOldValue(), fieldRevision.getNewValue())) {
        fieldRevisionList.add(fieldRevision);
      }
    }
  }

  private <T> List<TypedRevisionEntry<T>> getTypedRevisions(long objectId, Class<T> cls) {
    List<Object[]> resultList = getRevisionsAsc(objectId, cls);

    List<TypedRevisionEntry<T>> revisions = new ArrayList<>();
    for (Object[] revision : resultList) {
      @SuppressWarnings("unchecked")
      TypedRevisionEntry<T> currentRevisionEntry = new TypedRevisionEntry<>((T) revision[0], (RevisionInformation) revision[1], (RevisionType) revision[2]);
      revisions.add(currentRevisionEntry);
    }
    return revisions;
  }

  private <T> List<Object[]> getRevisionsAsc(long objectId, Class<T> cls) {
    AuditReader reader = AuditReaderFactory.get(entityManager);
    AuditQuery auditQuery = reader.createQuery().forRevisionsOfEntity(cls, false, true);
    auditQuery.add(AuditEntity.id().eq(objectId));
    auditQuery.addOrder(AuditEntity.revisionNumber().asc());
    @SuppressWarnings("unchecked")
    List<Object[]> resultList = auditQuery.getResultList();
    return resultList;
  }

  private static void setOldAndNewValuesAsString(Map<String, Field[]> auditedReferencedFields, Field field, FieldRevision fieldRevision, Object oldValue, Object newValue) {
    if (isInBurningOkrNamespace(field)) {
      Field[] referencedFields = auditedReferencedFields.get(field.getName());
      fieldRevision.setOldValue(getObjectStringByFieldList(referencedFields, oldValue));
      fieldRevision.setNewValue(getObjectStringByFieldList(referencedFields, newValue));
    } else {
      fieldRevision.setOldValue(Objects.toString(oldValue, ""));
      fieldRevision.setNewValue(Objects.toString(newValue, ""));
    }
  }

  private static void setOldUserList(FieldRevision fieldRevision, Collection<UUID> oldValue) {
    Collection<UUID> oldUserList = oldValue;
    if (oldUserList == null) {
      oldUserList = Collections.emptyList();
    }
    fieldRevision.setOldValue(oldUserList);
  }

  private static void setNewUserList(FieldRevision fieldRevision, Collection<UUID> newValue) {
    Collection<UUID> newUserList = newValue;
    if (newUserList == null) {
      newUserList = Collections.emptyList();
    }
    fieldRevision.setNewValue(newUserList);
  }
}
