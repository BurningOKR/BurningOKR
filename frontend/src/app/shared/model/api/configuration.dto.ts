import { ConfigurationId, ConfigurationValue } from '../id-types';

export interface ConfigurationDto {
  id: ConfigurationId;
  name: string;
  value: ConfigurationValue;
  type: string;
}
