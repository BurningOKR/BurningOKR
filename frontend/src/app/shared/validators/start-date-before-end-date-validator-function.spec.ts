import { AbstractControl, FormControl, FormGroup, ValidationErrors } from '@angular/forms';
import { startDateBeforeEndDateValidatorFunction } from './start-date-before-end-date-validator-function';
import { startDateBeforeEndDateError } from './errors/start-date-not-before-end-date-error';

describe('startDateBeforeEndDateValidatorFunction', () => {

  it('should return true if enddate is before startdate', () => {

    const control: AbstractControl = new FormGroup({
      startDate: new FormControl(new Date('02.01.2020')),
      endDate: new FormControl(new Date('01.01.2020'))
    });
    control.markAllAsTouched();

    const actual: ValidationErrors = startDateBeforeEndDateValidatorFunction(control);

    expect(actual)
      .toEqual(startDateBeforeEndDateError);
  });

  it('should return undefined if enddate is after startdate', () => {
    const control: AbstractControl = new FormGroup({
      startDate: new FormControl(new Date('01.01.2020')),
      endDate: new FormControl(new Date('02.01.2020'))
    });
    control.markAllAsTouched();

    const actual: ValidationErrors = startDateBeforeEndDateValidatorFunction(control);

    expect(actual)
      .toBeUndefined();
  });
});
