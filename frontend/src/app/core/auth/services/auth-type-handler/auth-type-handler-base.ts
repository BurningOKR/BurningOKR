export interface AuthTypeHandlerBase {

  /**
   * Starts the Login Procedure when the user is not logged in.
   * This will redirect the user to the Login Page of the current AuthTypeHandler.
   * @returns true, when the user is already logged in. In this case it will not redirect the user.
   */
  startLoginProcedure(): Promise<boolean>;

  /**
   * Sets up the silent refresh of the access token.
   */
  setupSilentRefresh(): void;

  /**
   * This method should be called after the configure() method of the AuthenticationService has been called.
   */
  afterConfigured(): Promise<any>;

  /**
   * Logs the user in with the current AuthTypeHandler.
   * @param email the email of the user
   * @param password the password of the user.
   */
  login(email?: string, password?: string): Promise<any>;
}
