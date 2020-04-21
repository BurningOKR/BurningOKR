export type ConfigurationId = number;
export type ConfigurationValue = string;

export interface ConfigurationDto {
  id: ConfigurationId;
  name: string;
  value: ConfigurationValue;
  type: string;
}
