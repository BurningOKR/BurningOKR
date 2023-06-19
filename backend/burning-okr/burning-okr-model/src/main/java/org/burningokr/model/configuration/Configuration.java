package org.burningokr.model.configuration;

import jakarta.persistence.*;
import lombok.Data;
import org.burningokr.model.activity.Trackable;

@Entity
@Data
public class Configuration implements Trackable<Long> {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "hibernate_sequence_generator")
  @SequenceGenerator(name = "hibernate_sequence_generator", sequenceName = "hibernate_sequence", allocationSize = 1)
  private Long id;

  private String name;

  private String value;

  private String type;
}
