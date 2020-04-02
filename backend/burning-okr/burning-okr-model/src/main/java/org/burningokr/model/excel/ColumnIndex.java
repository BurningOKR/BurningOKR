package org.burningokr.model.excel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ColumnIndex {
  /**
   * Value of the ColumnIndex.
   *
   * @return the value
   */
  int value();
}
