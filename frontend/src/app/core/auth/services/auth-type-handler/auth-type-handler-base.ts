export interface AuthTypeHandlerBase {
  startLoginProcedure(): Promise<boolean>;
  setupSilentRefresh(): void;
  afterConfigured(): Promise<any>;
  login(email?: string, password?: string): Promise<any>;
}
