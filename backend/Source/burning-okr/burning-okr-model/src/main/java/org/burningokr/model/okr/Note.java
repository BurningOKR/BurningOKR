package org.burningokr.model.okr;

import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.burningokr.model.activity.Trackable;

@Entity
@Data
public class Note implements Trackable<Long> {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ManyToOne private KeyResult parentKeyResult;

  @NotNull private UUID userId;

  @Column(length = 1023)
  private String text;

  @Column(name = "date", nullable = false)
  private LocalDateTime date;

  @Override
  public String getName() {
    return "";
  }
}
