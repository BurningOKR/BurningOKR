package org.burningokr.model.users;

import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Data;

@Entity
@Data
public class AuditorUser {
    @Id private UUID id;
}
