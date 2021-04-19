// Use this file as a sample for your environment.local.ts.
// It does not extend the environment.ts. It is completely standalone.
//
// The environment.local.ts is normally ignored by git.
// If not, please ensure that you do not commit and push your environment.local.ts
//
// In order to use the environment.local.ts, use the following serve command:
// npm run start-dev
//
// (You can do this in IntelliJ by editing your Build Configuration)

import { Environment } from '../app/shared/model/environment/environment';

export const environment: Environment = {
  production: false,
  brokerURL: 'ws://localhost:8080/wsregistry',
};
