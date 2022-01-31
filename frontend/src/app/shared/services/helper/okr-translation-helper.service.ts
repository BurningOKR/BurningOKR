import { Injectable } from '@angular/core';
import {TranslateService} from '@ngx-translate/core';
import {DateAdapter} from '@angular/material/core';
import {CookieHelperService} from './cookie-helper.service';

@Injectable({
  providedIn: 'root'
})
export class OkrTranslationHelperService {

  constructor(private translateService: TranslateService,
              private dateAdapter: DateAdapter<Date>,
              private cookieHelper: CookieHelperService
  ) { }

  initializeTranslationOnStartup(): void {

    // this language will be used as a fallback when a translation isn't found in the current language
    this.translateService.setDefaultLang('en');

    // the lang to use, if the lang isn't available, it will use the current loader to get them
    this.changeToLanguage(this.getInitialLanguage());
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
    this.cookieHelper.setCookieValue('language', language, 30);
  }

  private getInitialLanguage(): string {

    if (this.cookieHelper.isCookieSet('language')) {

      return this.cookieHelper.getCookieValue('language');
    } else {

      return 'en';
    }
  }
}
