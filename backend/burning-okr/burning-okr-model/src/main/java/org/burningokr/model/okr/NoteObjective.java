package org.burningokr.model.okr;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class NoteObjective extends Note {

    @ManyToOne
    private Objective parentObjective;
}
