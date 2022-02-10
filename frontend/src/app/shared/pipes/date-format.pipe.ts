import { Pipe, PipeTransform } from '@angular/core';
import { OkrTranslationHelperService } from '../services/helper/okr-translation-helper.service';
import { map } from 'rxjs/operators';
import { Observable } from 'rxjs';

@Pipe({
  name: 'dateFormat'
})
export class DateFormatPipe implements PipeTransform {

  currentLanguage$: Observable<string>;
  constructor(private okrTranslationHelper: OkrTranslationHelperService) {
    this.currentLanguage$ = okrTranslationHelper.getCurrentLanguage$();
  }

  transform(date: Date, monthString?: boolean): Observable<string> {
    return monthString ? this.getDateWithMonthString$(date) : this.getLocalDateString$(date);
  }

  private getDateWithMonthString$(date: Date): Observable<string> {
    console.log('Monat String');

    return this.getLocalDateString$(date, {month: 'short', day: 'numeric', year: 'numeric'});
  }

  private getLocalDateString$(date: Date, options: any = {}) {
    console.log('Date Function with: ', options);

    return this.currentLanguage$.pipe(map(language => {
      console.log(language);

      return date.toLocaleDateString(language, options);
    }));
  }
}
