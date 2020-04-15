export class OauthClientDetails {
  accessTokenValidity: number = 43200;
  clientId: string = OauthClientDetails.generateRandomString();
  clientSecret: string = OauthClientDetails.generateRandomString();
  refreshTokenValidity: number =  43200;
  webServerRedirectUri: string = window.location.origin;

  private static generateRandomString(): string {
    return Array(64)
      .fill(0)
      .map(() => Math.random()
        .toString(36)
        .charAt(2))
      .join('');
  }
}
