package org.burningokr.model.okr;

import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.burningokr.model.activity.Trackable;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Data
public class Note implements Trackable<Long> {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @NotNull private UUID userId;

  @Column(length = 1023)
  private String text;

  @Column(name = "date", nullable = false)
  private LocalDateTime date;

//  @Column
//  private NoteParentType noteParentType;

  @Override
  public String getName() {
    return "";
  }
}
