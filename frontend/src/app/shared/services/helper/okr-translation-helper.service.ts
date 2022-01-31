import { Injectable } from '@angular/core';
import {TranslateService} from '@ngx-translate/core';
import {DateAdapter} from '@angular/material/core';

@Injectable({
  providedIn: 'root'
})
export class OkrTranslationHelperService {

  constructor(private translateService: TranslateService,
              private dateAdapter: DateAdapter<Date>
  ) { }

  initializeTranslationOnStartup(): void {

    // this language will be used as a fallback when a translation isn't found in the current language
    this.translateService.setDefaultLang('en');

    // the lang to use, if the lang isn't available, it will use the current loader to get them
    this.changeToLanguage('en');
  }

  changeCurrentLanguageTo(language: string): boolean {

    const isLanguageAvailable: boolean = this.translateService.getLangs().includes(language);

    if (isLanguageAvailable) {

      this.changeToLanguage(language);
    }

    return isLanguageAvailable;
  }

  private changeToLanguage(language: string): void {

    this.translateService.use(language);
    this.dateAdapter.setLocale(language);
  }
}
