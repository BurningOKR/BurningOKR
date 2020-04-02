import {
  dateRangeInRangeWithinAnotherDatesValidatorFunction, periodCollidesWithOther
} from './date-range-in-range-within-another-dates-validator-function';
import { CycleUnit } from '../model/ui/cycle-unit';

describe('dateRangeInRangeWithinAnotherDatesValidatorFunction', () => {
  let testCycles: CycleUnit[] = [
    new CycleUnit(123, 'testCycle1', [1], new Date('01.01.2000'), new Date('12.31.2001'), undefined, false),
    new CycleUnit(987, 'testCycle2', [2], new Date('01.01.2001'), new Date('12.31.2002'), undefined, false),
  ];

  it('periodCollidesWithOther should return undefined, if cycles don\'t overlap', () => {
      const notOverlappingStartDate: Date = new Date('01.01.2003');
      const notOverlappingEndDate: Date = new Date('01.01.2004');

      const actual: {[key: string]: boolean} = periodCollidesWithOther(
        notOverlappingStartDate,
        notOverlappingEndDate,
        testCycles
      );

      expect(actual)
        .toEqual(undefined);
    }
  );

  it('periodCollidesWithOther should return the object, if cycles overlap', () => {
      const overlappingStartDate: Date = new Date('01.01.2000');
      const overlappingEndDate: Date = new Date('01.31.2001');

      const actual: {[key: string]: boolean} = periodCollidesWithOther(
        overlappingStartDate,
        overlappingEndDate,
        testCycles
      );

      expect(actual)
        .toEqual({
          dateRangeWithinAnotherDateRange: true
        });
    }
  );

  it('periodCollidesWithOther should return the object, if cycles overlap 2', () => {
    testCycles = [
      new CycleUnit(123, 'testCycle1', [1], new Date('11.15.2019'), new Date('02.15.2020'), undefined, false),
    ];

    const overlappingStartDate: Date = new Date('02.14.2020');
    const overlappingEndDate: Date = new Date('02.15.2020');

    const actual: {[key: string]: boolean} = periodCollidesWithOther(
        overlappingStartDate,
        overlappingEndDate,
        testCycles
      );

    expect(actual)
        .toEqual({
          dateRangeWithinAnotherDateRange: true
        });
    }
  );

});
