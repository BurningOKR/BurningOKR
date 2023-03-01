package org.burningokr.model.revision;

import java.util.Date;
import java.util.UUID;

public interface IRevisionInformation {
    Date getDate();
    UUID getUserId();
}
