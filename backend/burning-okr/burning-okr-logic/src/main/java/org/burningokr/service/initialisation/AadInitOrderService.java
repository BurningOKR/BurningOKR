package org.burningokr.service.initialisation;

import org.burningokr.model.initialisation.InitStateName;

public class AadInitOrderService extends InitOrderService {

  private static final InitStateName[] initStateNames = {
    InitStateName.NO_AZURE_ADMIN_USER, InitStateName.INITIALIZED
  };

  public AadInitOrderService() {
    super(initStateNames);
  }
}
