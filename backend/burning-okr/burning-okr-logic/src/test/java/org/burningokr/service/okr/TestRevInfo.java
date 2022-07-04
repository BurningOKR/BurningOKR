package org.burningokr.service.okr;

import lombok.Data;
import org.burningokr.model.revision.IRevisionInformation;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@RevisionEntity
public class TestRevInfo implements IRevisionInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @RevisionNumber
    private long id;

    @RevisionTimestamp
    private Date date;

    private UUID userId;
}
