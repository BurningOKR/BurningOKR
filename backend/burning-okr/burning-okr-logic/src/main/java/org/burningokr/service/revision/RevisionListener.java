package org.burningokr.service.revision;

import lombok.RequiredArgsConstructor;
import org.burningokr.model.revision.RevisionInformation;
import org.burningokr.service.security.AuthorizationUserContextService;
import org.hibernate.envers.EntityTrackingRevisionListener;
import org.hibernate.envers.RevisionType;

@RequiredArgsConstructor
public class RevisionListener
        implements org.hibernate.envers.RevisionListener, EntityTrackingRevisionListener {
  private final AuthorizationUserContextService authorizationUserContextService;

  @Override
  public void newRevision(Object revisionEntity) {
    RevisionInformation r = (RevisionInformation) revisionEntity;
    // TODO Workaround entfernen nach Spring Upgrade (wegen fehlender DI). (MV)
    r.setUserId(authorizationUserContextService.getAuthenticatedUser().getId());
  }

  @Override
  public void entityChanged(Class entityClass, String entityName, Object entityId, RevisionType revisionType, Object revisionEntity) {
    // TODO Hierher gehört nach dem Spring Upgrade (wegen fehlender DI) der Vergleich, ob es Benutzeränderungen gegeben hat. Dazu den RevisionService befähigen und hier aufrufen dazu bzw. eine Klasse für Tasks. (MV)
  }
}
