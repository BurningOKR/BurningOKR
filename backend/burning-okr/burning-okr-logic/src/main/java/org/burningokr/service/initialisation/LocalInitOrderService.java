package org.burningokr.service.initialisation;

import org.burningokr.model.initialisation.InitStateName;

public class LocalInitOrderService extends InitOrderService {

  private static final InitStateName[] initStateNames = {
    InitStateName.SET_OAUTH_CLIENT_DETAILS, InitStateName.CREATE_USER, InitStateName.INITIALIZED
  };

  public LocalInitOrderService() {
    super(initStateNames);
  }
}
