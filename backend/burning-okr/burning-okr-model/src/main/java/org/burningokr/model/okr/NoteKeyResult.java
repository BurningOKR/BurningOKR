package org.burningokr.model.okr;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@Data
public class NoteKeyResult extends Note {

    @ManyToOne
    private KeyResult parentKeyResult;
}
