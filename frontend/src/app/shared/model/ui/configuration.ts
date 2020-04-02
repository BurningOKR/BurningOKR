import { ConfigurationId } from '../api/configuration.dto';

export class Configuration {
  id: ConfigurationId;
  name: string;
  value: string;

  constructor(id: ConfigurationId, name: string, value: string) {
    this.id = id;
    this.name = name;
    this.value = value;
  }
}
