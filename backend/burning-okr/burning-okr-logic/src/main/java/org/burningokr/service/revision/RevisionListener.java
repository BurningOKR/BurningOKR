package org.burningokr.service.revision;

import org.burningokr.model.revision.RevisionInformation;
import org.burningokr.service.security.UserFromContextService;
import org.hibernate.envers.EntityTrackingRevisionListener;
import org.hibernate.envers.RevisionType;

import java.io.Serializable;

public class RevisionListener
    implements org.hibernate.envers.RevisionListener, EntityTrackingRevisionListener {

  @Override
  public void newRevision(Object revisionEntity) {
    RevisionInformation r = (RevisionInformation) revisionEntity;
    // TODO Workaround entfernen nach Spring Upgrade (wegen fehlender DI). (MV)
    r.setUserId(UserFromContextService.extractUserIdFromSecurityContext().getUserId());
  }

  @Override
  public void entityChanged(
      Class entityClass,
      String entityName,
      Serializable entityId,
      RevisionType revisionType,
      Object revisionEntity) {
    /* if (Task.class.equals(entityClass)) {
      // TODO Hierher gehört nach dem Spring Upgrade (wegen fehlender DI) der Vergleich, ob es Benutzeränderungen gegeben hat. Dazu den RevisionService befähigen und hier aufrufen dazu bzw. eine Klasse für Tasks. (MV)
    } */
  }
}
