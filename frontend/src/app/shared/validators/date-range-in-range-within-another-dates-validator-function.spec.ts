import { dateRangeInRangeWithinAnotherDatesValidatorFunction } from './date-range-in-range-within-another-dates-validator-function';
import { CycleUnit } from '../model/ui/cycle-unit';
import { FormControl, FormGroup, ValidationErrors } from '@angular/forms';
import { dateRangeInRangewithinAnotherDatesError } from './errors/date-range-in-range-within-another-dates-error';

describe('dateRangeInRangeWithinAnotherDatesValidatorFunction', () => {
  let testCycles: CycleUnit[] = [
    new CycleUnit(123, 'testCycle1', [1], new Date('01.01.2000'), new Date('12.31.2001'), undefined, false),
    new CycleUnit(987, 'testCycle2', [2], new Date('01.01.2001'), new Date('12.31.2002'), undefined, false),
  ];

  it('should return undefined, if cycles don\'t overlap', () => {

      const notOverlappingStartDate: Date = new Date('01.01.2003');
      const notOverlappingEndDate: Date = new Date('01.01.2004');

      const control: FormGroup = new FormGroup({
        startDate: new FormControl(notOverlappingStartDate),
        endDate: new FormControl(notOverlappingEndDate)
      });

      const actual: ValidationErrors = dateRangeInRangeWithinAnotherDatesValidatorFunction(
        testCycles
      )
        .call(testCycles, control);

      expect(actual)
        .toBeUndefined();
    }
  );

  it('should return undefined, if dates are not set', () => {

      const control: FormGroup = new FormGroup({
        startDate: new FormControl(),
        endDate: new FormControl()
      });

      const actual: ValidationErrors = dateRangeInRangeWithinAnotherDatesValidatorFunction(
        testCycles
      )
        .call(testCycles, control);

      expect(actual)
        .toBeUndefined();
    }
  );

  it('periodCollidesWithOther should return the object, if cycles overlap', () => {
      const overlappingStartDate: Date = new Date('01.01.2000');
      const overlappingEndDate: Date = new Date('01.31.2001');

      const control: FormGroup = new FormGroup({
        startDate: new FormControl(overlappingStartDate),
        endDate: new FormControl(overlappingEndDate)
      });

      const actual: ValidationErrors = dateRangeInRangeWithinAnotherDatesValidatorFunction(
        testCycles
      )
        .call(testCycles, control);

      expect(actual)
        .toEqual(dateRangeInRangewithinAnotherDatesError);
    }
  );

  it('periodCollidesWithOther should return the object, if cycles overlap 2', () => {
      testCycles = [
        new CycleUnit(123, 'testCycle1', [1], new Date('11.15.2019'), new Date('02.15.2020'), undefined, false),
      ];

      const overlappingStartDate: Date = new Date('02.14.2020');
      const overlappingEndDate: Date = new Date('02.15.2020');

      const control: FormGroup = new FormGroup({
        startDate: new FormControl(overlappingStartDate),
        endDate: new FormControl(overlappingEndDate)
      });

      const actual: { [key: string]: boolean } = dateRangeInRangeWithinAnotherDatesValidatorFunction(
        testCycles
      )
        .call(testCycles, control);
      expect(actual)
        .toEqual(dateRangeInRangewithinAnotherDatesError);
    }
  );

});
