import { ConfigurationId } from '../id-types';

export class Configuration {
  id: ConfigurationId;
  name: string;
  value: string;
  type: string;

  constructor(id: ConfigurationId, name: string, value: string, type: string) {
    this.id = id;
    this.name = name;
    this.value = value;
    this.type = type;
  }
}
