import { Injectable } from '@angular/core';
import { ApiHttpService } from '../services/api-http.service';
import { Observable } from 'rxjs';
import { ConfigurationDto } from '../../shared/model/api/configuration.dto';
import { ConfigurationId } from '../../shared/model/id-types';

@Injectable({
  providedIn: 'root'
})
export class ConfigurationApiService {

  constructor(private api: ApiHttpService) {
  }

  getHasMailConfigured$(): Observable<boolean> {
    return this.api.getData$('configurations/hasmail');
  }

  getConfigurations$(): Observable<ConfigurationDto[]> {
    return this.api.getData$('configurations');
  }

  getConfigurationById$(id: ConfigurationId): Observable<ConfigurationDto> {
    return this.api.getData$(`configurations/id/${id}`);
  }

  getConfigurationByName$(name: string): Observable<ConfigurationDto> {
    return this.api.getData$(`configurations/name/${name}`);
  }

  postConfiguration$(configuration: ConfigurationDto): Observable<ConfigurationDto> {
    return this.api.postData$('configurations', configuration);
  }

  putConfiguration$(configuration: ConfigurationDto): Observable<ConfigurationDto> {
    return this.api.putData$(`configurations/${configuration.id}`, configuration);
  }

  deleteConfiguration$(configurationId: ConfigurationId): Observable<boolean> {
    return this.api.deleteData$(`configuration/${configurationId}`);
  }

}
