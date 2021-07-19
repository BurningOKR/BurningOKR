package org.burningokr.model.okr;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class NoteKeyResult extends Note {

  @ManyToOne private KeyResult parentKeyResult;
}
