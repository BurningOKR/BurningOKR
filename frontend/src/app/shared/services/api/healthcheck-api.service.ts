// istanbul ignore file
import { Injectable } from '@angular/core';
import { ApiHttpService } from '../../../core/services/api-http.service';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})

export class HealthcheckApiService {

  constructor(private api: ApiHttpService) {
  }

  isAlive$(): Observable<boolean> {
    return this.api.getData$(`isAlive`);
  }
}
