package org.burningokr.model.okr;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.RevisionType;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Revision {

  private Date date;

  private UUID user;

  private RevisionType revisionType;

  private String changedField;

  private RevisionValueType revisionValueType;

  private Object oldValue;

  private Object newValue;
}
