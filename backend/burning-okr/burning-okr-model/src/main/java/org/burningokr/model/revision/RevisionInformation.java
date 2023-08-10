package org.burningokr.model.revision;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

import java.util.Date;
import java.util.UUID;

@Entity
@Data
@RevisionEntity
public class RevisionInformation {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "hibernate_sequence_generator")
  @SequenceGenerator(name = "hibernate_sequence_generator", sequenceName = "hibernate_sequence", allocationSize = 1)
  @RevisionNumber
  private long id;

  @RevisionTimestamp
  private Date date;

  private UUID userId;
}
