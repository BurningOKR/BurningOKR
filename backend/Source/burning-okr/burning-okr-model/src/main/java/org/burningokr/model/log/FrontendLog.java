package org.burningokr.model.log;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
