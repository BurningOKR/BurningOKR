import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
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

}
