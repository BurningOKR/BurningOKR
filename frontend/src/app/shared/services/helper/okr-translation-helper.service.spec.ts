import { TestBed } from '@angular/core/testing';
import { OkrTranslationHelperService } from './okr-translation-helper.service';
import { TranslateService } from '@ngx-translate/core';
import { MaterialTestingModule } from '../../../testing/material-testing.module';
import { CookieHelperService } from './cookie-helper.service';

describe('OkrTranslationService', () => {
  let service: OkrTranslationHelperService;
  let translateService: TranslateService;
  let cookieHelper: CookieHelperService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ MaterialTestingModule ],
    });
    service = TestBed.inject(OkrTranslationHelperService);
    translateService = TestBed.inject(TranslateService);
    cookieHelper = TestBed.inject(CookieHelperService);

    translateService.addLangs(['en', 'de']);
    cookieHelper.clearAllCookies();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should return english as a default Language', () => {

    expect(service.getInitialLanguage()).toBe('en');
  });

  it('should return german after setting the language', () => {

    service.changeCurrentLanguageTo('de');
    expect(translateService.currentLang).toBe('de');
  });

  it('should return english after setting an incorrect Language', () => {

    service.initializeTranslationOnStartup();
    service.changeCurrentLanguageTo('es');
    expect(translateService.currentLang).toBe('en');
  });

  it('should return german if language cookie is found', () => {

    expect(service.getInitialLanguage()).toBe('en');

    cookieHelper.setCookieValue('language', 'de');
    expect(service.getInitialLanguage()).toBe('de');
  });

  it('should initialize translation', () => {

    service.initializeTranslationOnStartup();

    expect(translateService.currentLang).toBe('en');
    expect(translateService.defaultLang).toBe('en');

    cookieHelper.setCookieValue('language', 'de');
    service.initializeTranslationOnStartup();

    expect(translateService.currentLang).toBe('de');
    expect(translateService.defaultLang).toBe('en');
  });
});
