package org.burningokr.service.helpers.revisionService;

import org.burningokr.model.okr.FieldRevision;
import org.burningokr.model.revision.RevisionInformation;
import org.burningokr.service.exceptions.GetterNotFoundException;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RevisionType;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;

public class RevisionServiceHelper {

  public static void setRevisionGeneralInfo(RevisionInformation revInfo, RevisionType revisionType, FieldRevision fieldRevision) {
    fieldRevision.setDate(revInfo.getDate());
    if (revInfo.getUserId() != null)
      fieldRevision.setUser(revInfo.getUserId());
    fieldRevision.setRevisionType(revisionType);
  }

  public static boolean storesCollectionOfUUIDs(Field field) {
    return isCollection(field)
            && isCollectionOfTypeUuid(field);
  }

  public static boolean isInBurningOkrNamespace(Field f) {
    return f.getType().getCanonicalName().startsWith("org.burningokr.");
  }

  public static Object getValueByGetter(Object objectWithFields, Field fieldToRetrieve) {
    if (objectWithFields == null) {
      return null;
    }
    Object fieldValue;
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
      throw new GetterNotFoundException("Error finding / calling the getter for '"
              + fieldToRetrieve.getName()
              + "' on "
              + objectWithFields, e);
    }

    return fieldValue;
  }

  public static <T> List<Field> getChangedFields(
          Field[] fieldsToCompare, T firstVersion, T secondVersion) {
    List<Field> changedFields = new LinkedList<>();
    for (Field field : fieldsToCompare) {
      Object firstValue = RevisionServiceHelper.getValueByGetter(firstVersion, field);
      Object secondValue = RevisionServiceHelper.getValueByGetter(secondVersion, field);
      if (!Objects.equals(firstValue, secondValue)) {
        changedFields.add(field);
      }
    }
    return changedFields;
  }

  public static Map<String, Field[]> getReferencedAuditedFields(Field[] auditedFields) {
    return Arrays.stream(auditedFields)
            .filter(RevisionServiceHelper::isInBurningOkrNamespace)
            .map(f -> new AbstractMap.SimpleEntry<>(f.getName(), getAuditedFields(f.getType())))
            .collect(
                    Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));
  }


  public static <T> Field[] getAuditedFields(Class<T> classType) {
    return Arrays.stream(classType.getDeclaredFields())
            .filter(field -> field.isAnnotationPresent(Audited.class))
            .toArray(Field[]::new);
  }

  public static String getObjectStringByFieldList(Field[] fields, Object object) {
    if (fields.length > 1) {
      List<String> values = new LinkedList<>();
      for (Field f : fields) {
        values.add(f.getName() + ": " + RevisionServiceHelper.getValueByGetter(object, f));
      }
      values.add("-- TO BE IMPLEMENTED WITH MORE SENSE --");
      return String.join("\n", values);
    } else {
      return Objects.toString(RevisionServiceHelper.getValueByGetter(object, fields[0]), "");
    }
  }

  public static boolean isValueDifferent(Object oldValue, Object newValue) {
    return !Objects.equals(oldValue, newValue);
  }

  private static boolean isCollectionOfTypeUuid(Field field) {
    return ((ParameterizedType) field.getGenericType())
            .getActualTypeArguments()[0].equals(UUID.class);
  }

  private static boolean isCollection(Field field) {
    return field.getType().equals(Collection.class);
  }

}
