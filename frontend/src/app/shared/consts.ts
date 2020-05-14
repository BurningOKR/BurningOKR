export class Consts {
  static API_URL = '/api/'; // The base URL of the api.

  static SILENT_REFRESH_MULTIPLIER = 0.7; // How long of the access Token validity time to wait, until the token is refreshed.
  static MIN_TOKEN_DURATION = 1; // min access token validity time in seconds

  static MAX_UNSUCCESSFUL_PING_ATTEMPTS_FOR_RESTART = 200; // How often should we ping the server, while its restarting to change config values?
  static MIN_INTERVAL_BETWEEN_PING_ATTEMPTS = 1000; // Interval in milliseconds, at which the server is pinged, while its restarting to change config values.

  static HTTP_ERROR_RESPONSE_WRONG_PASSWORD = 'Wrong Password'; // The http message that is sent by the server, if the password was wrong

  static FETCHING_SERVICE_DEBOUNCE_TIME = 20; // Debounce time to prevent double fetching, when the AuthenticationService initializes and logs in.

  static CURRENCY_EURO = 'EURO';
  static NUMBER_FORMAT_PERCENT = 'PERCENT';

  static AUTHTYPE_LOCAL = 'local';
  static AUTHTYPE_AZURE = 'azure';

  static UNAUTHORIZED_ERROR = 401; // HTTP Error Status Code for unauthorized.
  static CLIENT_RESOLVABLE_ERRORS = [ // HTTP Error Status Codes, that can be resolved by simply retrying.
    0,
    407,
    408,
    409,
    412,
    421,
    423,
    424,
    429,
    444,
    499,
    501,
    502,
    503,
    505,
    507,
    509,
    510,
    511
  ];
}
