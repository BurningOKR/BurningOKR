import { DateFormatPipe } from './date-format.pipe';
import {OkrTranslationHelperService} from "../services/helper/okr-translation-helper.service";

describe('DateFormatPipe', () => {

  let dateFormatPipe: DateFormatPipe;

  beforeAll ( () => {
    dateFormatPipe = new DateFormatPipe(new OkrTranslationHelperService(undefined, undefined, undefined, undefined));
  });

  it('create an instance', () => {
    expect(dateFormatPipe).toBeTruthy();
  });
});
