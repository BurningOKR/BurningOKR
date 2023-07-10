import { Injectable } from '@angular/core';
import * as moment from 'moment';

@Injectable({
  providedIn: 'root',
})
export class DateMapper {

  static mapDateToDateString(date: Date): string {
    const parsedDate: Date = this.mapToDate(date);

    return date ? `${parsedDate.getFullYear()}-${parsedDate.getMonth()}-${parsedDate.getDate()}` : null;
  }

  static mapDateStringToDate(dateSting: string): Date {
    let parsedDate: Date = null;

    if (dateSting) {
      // Date is a Sting with Format yyyy-mm-dd
      const splitDate: string[] = dateSting.split('-');
      parsedDate = new Date(Number(splitDate[0]), Number(splitDate[1]), Number(splitDate[2]));
    }

    return parsedDate;
  }

  static mapToDate(date: Date | moment.Moment): Date {
    if (moment.isMoment(date)) {
      return date.toDate();
    } else if (moment.isDate(date)) {
      return date;
    } else {
      return null;
    }
  }
}
