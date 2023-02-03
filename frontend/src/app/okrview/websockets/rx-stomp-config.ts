import { InjectableRxStompConfig } from '@stomp/ng2-stompjs';
import { environment } from '../../../environments/environment';

const protocol: string = (location.protocol === 'https:') ? 'wss' : 'ws';
export const myRxStompConfig: InjectableRxStompConfig = {
  // Which server?
  brokerURL: `${protocol}://${window.location.host}/${environment.brokerURLSuffix}`,
  // How often to heartbeat?
  // Interval in milliseconds, set to 0 to disable
  heartbeatIncoming: 10000, // Typical value 0 - disabled
  heartbeatOutgoing: 10000, // Typical value 20000 - every 20 seconds

  // Wait in milliseconds before attempting auto reconnect
  // Set to 0 to disable
  // Typical value 500 (500 milli seconds)
  reconnectDelay: 5000,

  beforeConnect: (client): void => {
    client.configure(
      {
        connectHeaders: {
          Authorization: `Bearer ${localStorage.getItem('access_token')}`,
        },
      },
    );
  },

  // Will log diagnostics on console
  // It can be quite verbose, not recommended in production
  // Skip this key to stop logging to console
  // debug: (msg: string): void => {
  // console.log(new Date(), msg, ' ', this.brokerURL);
  // },
};
