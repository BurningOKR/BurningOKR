package org.burningokr.model.revision;

import lombok.Data;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@RevisionEntity
public class RevisionInformation implements IRevisionInformation {

  @Id @GeneratedValue @RevisionNumber private long id;

  @RevisionTimestamp private Date date;

  private UUID userId;
}
