import { Pipe, PipeTransform } from '@angular/core';
import {OkrTranslationHelperService} from "../services/helper/okr-translation-helper.service";
import {map} from "rxjs/operators";
import {Observable} from "rxjs";

@Pipe({
  name: 'dateFormat'
})
export class DateFormatPipe implements PipeTransform {

  currentLanguage$: Observable<string>;
  constructor(private okrTranslationHelper: OkrTranslationHelperService) {
    this.currentLanguage$ = okrTranslationHelper.getCurrentLanguage$();
  }

  transform(date: Date, monthString?: boolean): Observable<string> {
    if (monthString)
      return this.getDateWithMonthString$(date);
    else
      return this.getLocalNumberDate$(date);
  }

  private getLocalNumberDate$ (date: Date): Observable<string> {
    return this.currentLanguage$.pipe(map(language => {
      return date.toLocaleDateString(language);
    }));
  }

  private getDateWithMonthString$ (date: Date): Observable<string> {
    return this.currentLanguage$.pipe(map(language => {
      return date.toLocaleDateString(language, {month: 'short', day: 'numeric', year: 'numeric'});
    }));
  }
}
