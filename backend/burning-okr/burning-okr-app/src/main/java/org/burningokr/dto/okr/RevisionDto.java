package org.burningokr.dto.okr;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.burningokr.model.okr.RevisionValueType;
import org.hibernate.envers.RevisionType;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RevisionDto {

  private Date date;

  private UUID user;

  private RevisionType revisionType;

  private String changedField;

  private RevisionValueType revisionValueType;

  private Object oldValue;

  private Object newValue;
}
