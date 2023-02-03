import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { ApiHttpService } from '../../../core/services/api-http.service';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class CountdownTimestampApiService {

  constructor(private api: ApiHttpService) {
  }

  getDateForNextReset$(): Observable<Date> {

    return this.api.getData$('demo/reset')
      .pipe(map((timeLeft: number) => {
          return new Date(Date.now() + timeLeft);
        },
      ));
  }
}
