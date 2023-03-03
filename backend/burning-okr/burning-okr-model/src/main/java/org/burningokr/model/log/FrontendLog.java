package org.burningokr.model.log;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "log")
public class FrontendLog {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String level;
  private LocalDateTime timestamp;
  private String fileName;
  private String lineNumber;
  private String message;
}
