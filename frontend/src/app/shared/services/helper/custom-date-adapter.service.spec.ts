import { CustomDateAdapterService } from './custom-date-adapter.service';
import { Platform } from '@angular/cdk/platform';

describe('CustomDateAdapterService', () => {
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
    const expected: Date = new Date(2019, 4, 15, 12);

    const actual: Date = service.parse(value);

    expect(actual)
      .toEqual(expected);
  });

  it('parse should return a date if input is DD.M.YYYY', () => {
    const value: string = '15.5.2019';
    const expected: Date = new Date(2019, 4, 15, 12);

    const actual: Date = service.parse(value);

    expect(actual)
      .toEqual(expected);
  });

  it('parse should return a date if input is DD.MM.YY', () => {
    const value: string = '15.05.19';
    const expected: Date = new Date(2019, 4, 15, 12);

    const actual: Date = service.parse(value);

    expect(actual)
      .toEqual(expected);
  });

  it('parse should return a date if input is DD.M.YY', () => {
    const value: string = '15.5.19';
    const expected: Date = new Date(2019, 4, 15, 12);

    const actual: Date = service.parse(value);

    expect(actual)
      .toEqual(expected);
  });

  it('parse should return a date if input is DDMMYYYY', () => {
    const value: string = '15052019';
    const expected: Date = new Date(2019, 4, 15, 12);

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
    const value: string = '15.13.2019';
    const expected: Date = undefined;

    const actual: Date = service.parse(value);

    expect(actual)
      .toEqual(expected);
  });

});
