import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ConfigurationDto } from '../../shared/model/api/configuration.dto';
import { ConfigurationId } from '../../shared/model/id-types';
import { ConfigurationApiService } from './configuration-api.service';

@Injectable({
  providedIn: 'root',
})
export class ConfigurationService {

  constructor(private apiService: ConfigurationApiService) {
  }

  getHasMailConfigured$(): Observable<boolean> {
    return this.apiService.getHasMailConfigured$();
  }

  getConfigurations$(): Observable<ConfigurationDto[]> {
    return this.apiService.getConfigurations$();
  }

  getConfigurationById$(id: ConfigurationId): Observable<ConfigurationDto> {
    return this.apiService.getConfigurationById$(id);
  }

  getConfigurationByName$(name: string): Observable<ConfigurationDto> {
    return this.apiService.getConfigurationByName$(name);
  }

  postConfiguration$(configuration: ConfigurationDto): Observable<ConfigurationDto> {
    return this.apiService.postConfiguration$(configuration);
  }

  putConfiguration$(configuration: ConfigurationDto): Observable<ConfigurationDto> {
    return this.apiService.putConfiguration$(configuration);
  }

  deleteConfiguration$(configurationId: ConfigurationId): Observable<boolean> {
    return this.apiService.deleteConfiguration$(configurationId);
  }

}
