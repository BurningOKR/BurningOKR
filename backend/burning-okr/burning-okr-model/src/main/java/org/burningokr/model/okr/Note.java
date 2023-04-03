package org.burningokr.model.okr;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.burningokr.model.activity.Trackable;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Data
public class Note implements Trackable<Long> {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "hibernate_sequence")
  private Long id;

  @NotNull
  private UUID userId;

  @Column(length = 1023)
  private String text;

  @Column(name = "date", nullable = false)
  private LocalDateTime date;

  public Note() {}

  public Note(Note note) {
    this.id = note.getId();
    this.userId = note.getUserId();
    this.text = note.getText();
    this.date = note.getDate();
  }

  @Override
  public String getName() {
    return "";
  }
}
