import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { ApiHttpService } from '../../../core/services/api-http.service';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class CountdownTimestampApiService {

  constructor(private api: ApiHttpService) {
  }

  getDateForNextReset(): Observable<Date> {

    return this.api.getData$('demo/reset')
      .pipe(map((date: number[]) => {
          return new Date(date[0], date[1] - 1, date[2], date[3], date[4], date[5]);
        }
      ));
  }
}
