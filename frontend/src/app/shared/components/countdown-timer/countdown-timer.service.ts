import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class CountdownTimerService {

  getLocalTimeString(remainingTime: Date): String {

    const temporaryDate: Date = new Date(remainingTime.getTime() + 1000 * 60 * remainingTime.getTimezoneOffset());

    return temporaryDate.toLocaleTimeString();
  }

  getRemainingTimeUntil(endTime: Date): Date {

    return new Date(endTime?.getTime() - new Date().getTime());
  }
}
