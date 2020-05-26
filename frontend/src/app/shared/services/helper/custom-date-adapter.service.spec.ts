import { CustomDateAdapterService } from './custom-date-adapter.service';
import { Platform } from '@angular/cdk/platform';

describe('CustomDateAdapterService', () => {

  const january: number = 0;
  const febuary: number = 1;
  const march: number = 2;
  const april: number = 3;
  const may: number = 4;
  const june: number = 5;
  const july: number = 6;
  const august: number = 7;
  const september: number = 8;
  const october: number = 9;
  const november: number = 10;
  const december: number = 11;


  let service: CustomDateAdapterService;

  beforeEach(() => {
    service = new CustomDateAdapterService('DE-de-custom', new Platform());
  });

  it('parse should return undefined if parameter is a random string', () => {
    const value: string = 'i am a date';
    const expected: Date = undefined;

    const actual: Date = service.parse(value);

    expect(actual)
      .toBe(expected);
  });

  it('parse should return undefined if parameter is undefined', () => {
    const value: string = undefined;
    const expected: Date = undefined;

    const actual: Date = service.parse(value);

    expect(actual)
      .toBe(expected);
  });

  it('parse should return undefined if parameter is an date within an object', () => {
    const value: object = {
      date: new Date()
    };
    const expected: Date = undefined;

    const actual: Date = service.parse(value);

    expect(actual)
      .toBe(expected);
  });

  it('parse should return undefined if parameter is a number within an object', () => {
    const value: object = {
      datetime: 123123
    };
    const expected: Date = undefined;

    const actual: Date = service.parse(value);

    expect(actual)
      .toBe(expected);
  });

  it('parse should not return a date if parameter is a zero', () => {
    const value: number = 0;
    const expected: Date = undefined;

    const actual: Date = service.parse(value);

    expect(actual)
      .toEqual(expected);
  });

  it('parse should not return a date if parameter is greater than 0', () => {
    const value: number = 100000;
    const expected: Date = undefined;

    const actual: Date = service.parse(value);

    expect(actual)
      .toEqual(expected);
  });

  it('parse should not return a date if parameter is less then 0', () => {
    const value: number = -100000;
    const expected: Date = undefined;

    const actual: Date = service.parse(value);

    expect(actual)
      .toEqual(expected);
  });

  it('parse should return a date if input is DD.MM.YYYY', () => {
    const value: string = '15.05.2019';
    const expected: Date = new Date(2019, may, 15, 12);

    const actual: Date = service.parse(value);

    expect(actual)
      .toEqual(expected);
  });

  it('parse should return a date if input is DD.M.YYYY', () => {
    const value: string = '15.5.2019';
    const expected: Date = new Date(2019, may, 15, 12);

    const actual: Date = service.parse(value);

    expect(actual)
      .toEqual(expected);
  });

  it('parse should return a date if input is DD.MM.YY', () => {
    const value: string = '15.05.19';
    const expected: Date = new Date(2019, may, 15, 12);

    const actual: Date = service.parse(value);

    expect(actual)
      .toEqual(expected);
  });

  it('parse should return a date if input is DD.M.YY', () => {
    const value: string = '15.5.19';
    const expected: Date = new Date(2019, may, 15, 12);

    const actual: Date = service.parse(value);

    expect(actual)
      .toEqual(expected);
  });

  it('parse should return a date if input is DDMMYYYY', () => {
    const value: string = '15052019';
    const expected: Date = new Date(2019, may, 15, 12);

    const actual: Date = service.parse(value);

    expect(actual)
      .toEqual(expected);
  });

  it('parse should return undefined if input is DD.MM.YYYY and Month > 12', () => {
    const value: string = '15.13.2019';
    const expected: undefined = undefined;

    const actual: Date = service.parse(value);

    expect(actual)
      .toEqual(expected);
  });

  it('parse should return undefined if input is DDMMYYYY and Month > 12', () => {
    const value: string = '15132019';
    const expected: Date = undefined;

    const actual: Date = service.parse(value);

    expect(actual)
      .toEqual(expected);
  });

  it('parse should return a date on 01.01.19 edgecase', () => {
    const value: string = '01012019';
    const expected: Date = new Date(2019, january, 1, 12);

    const acutal: Date = service.parse(value);

    expect(acutal)
      .toEqual(expected);
  });

  it('parse should return a date on 28.02.2019 edgecase', () => {
    const value: string = '28022019';
    const expected: Date = new Date(2019, febuary, 28, 12);

    const acutal: Date = service.parse(value);

    expect(acutal)
      .toEqual(expected);
  });

  it('parse should return a date on 31.12.2019 edgecase', () => {
    const value: string = '31122019';
    const expected: Date = new Date(2019, december, 31, 12);

    const acutal: Date = service.parse(value);

    expect(acutal)
      .toEqual(expected);
  });

});
