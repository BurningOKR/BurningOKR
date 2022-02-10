import { DateFormatPipe } from './date-format.pipe';
import { OkrTranslationHelperService } from '../services/helper/okr-translation-helper.service';
import { of } from 'rxjs';

describe('DateFormatPipe', () => {

  let dateFormatPipe: DateFormatPipe;
  const testDate: Date = new Date('11.22.2021');

  beforeAll (() => {
    const okrTranslationHelper: OkrTranslationHelperService = new OkrTranslationHelperService(undefined, undefined, undefined, undefined);
    (okrTranslationHelper as any).currentLanguage$ = of('');
    (okrTranslationHelper as any).translateService = {currentLang: 'test-language'};
    dateFormatPipe = new DateFormatPipe(okrTranslationHelper);
  });

  it('should create an instance', () => {
    expect(dateFormatPipe).toBeTruthy();
  });

  it('should return formatted testDate', async () => {
    dateFormatPipe.currentLanguage$ = of('en');
    let formattedDate: string = await dateFormatPipe.transform(testDate).toPromise();
    expect(formattedDate).toBe('11/22/2021');

    dateFormatPipe.currentLanguage$ = of('de');
    formattedDate = await dateFormatPipe.transform(testDate).toPromise();
    expect(formattedDate).toBe('22.11.2021');
  });

  it('should return formatted testDate with Month as String', async () => {
    dateFormatPipe.currentLanguage$ = of('en');
    let formattedDate: string = await dateFormatPipe.transform(testDate, true).toPromise();
    expect(formattedDate).toBe('Nov 22, 2021');

    dateFormatPipe.currentLanguage$ = of('de');
    formattedDate = await dateFormatPipe.transform(testDate, true).toPromise();
    expect(formattedDate).toBe('22. Nov. 2021');
  });
});
