import { Injectable } from '@angular/core';
import * as moment from 'moment';

@Injectable({
  providedIn: 'root',
})
export class DateMapper {

  mapToDate(date: Date | moment.Moment): Date {
    if (moment.isMoment(date)) {
      return date.toDate();
    } else if (moment.isDate(date)) {
      return date;
    } else {
      return null;
    }
  }
}
