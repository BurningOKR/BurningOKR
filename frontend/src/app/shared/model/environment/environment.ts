export interface Environment {
  production: boolean;

  /**
   * Definition of the websocket broker url for register your connection
   * Default for testing/developing it is 'ws://localhost:8080/wsregistry'
   */
  brokerURL: string,
}
