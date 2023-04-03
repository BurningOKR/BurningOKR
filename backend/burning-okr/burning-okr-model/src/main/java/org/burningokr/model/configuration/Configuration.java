package org.burningokr.model.configuration;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import org.burningokr.model.activity.Trackable;

@Entity
@Data
public class Configuration implements Trackable<Long> {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "hibernate_sequence")
  private Long id;

  private String name;

  private String value;

  private String type;
}
