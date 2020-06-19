import { ConfigurationApiService } from '../../../core/settings/configuration-api.service';
import { Injectable } from '@angular/core';
import { map } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { Configuration } from '../../model/ui/configuration';
import { ConfigurationDto } from '../../model/api/configuration.dto';
import { ConfigurationId } from '../../model/id-types';

@Injectable({
  providedIn: 'root'
})
export class ConfigurationMapper {

  constructor(private configurationApiService: ConfigurationApiService) {
  }

  static mapToConfiguration(configuration: ConfigurationDto): Configuration {
    return new Configuration(configuration.id, configuration.name, configuration.value, configuration.type);
  }

  static mapToConfigurationApi(configuration: Configuration): ConfigurationDto {
    return {
      id: configuration.id,
      name: configuration.name,
      value: configuration.value,
      type: configuration.type
    };
  }

  getConfigurations$(): Observable<Configuration[]> {
    return this.configurationApiService.getConfigurations$()
      .pipe(
      map((configurations: ConfigurationDto[]) => {
        return configurations.map(ConfigurationMapper.mapToConfiguration);
      })
    );
  }

  getConfigurationById$(id: ConfigurationId): Observable<Configuration> {
    return this.configurationApiService.getConfigurationById$(id)
      .pipe(map(ConfigurationMapper.mapToConfiguration));
  }

  getConfigurationByName$(name: string): Observable<Configuration> {
    return this.configurationApiService.getConfigurationByName$(name)
      .pipe(map(ConfigurationMapper.mapToConfiguration));
  }

  postConfiguration$(configuration: Configuration): Observable<Configuration> {
    return this.configurationApiService.postConfiguration$(ConfigurationMapper.mapToConfigurationApi(configuration))
      .pipe(map(ConfigurationMapper.mapToConfiguration));
  }

  putConfiguration$(configuration: Configuration): Observable<Configuration> {
    return this.configurationApiService.putConfiguration$(ConfigurationMapper.mapToConfigurationApi(configuration))
      .pipe(map(ConfigurationMapper.mapToConfiguration));
  }

  deleteConfiguration$(configurationId: number): Observable<boolean> {
    return this.configurationApiService.deleteConfiguration$(configurationId);
  }
}
