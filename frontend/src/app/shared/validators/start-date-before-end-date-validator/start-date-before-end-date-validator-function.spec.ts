import { AbstractControl, FormControl, FormGroup, ValidationErrors } from '@angular/forms';
import {
  StartDateBeforeEndDateValidator,
  startDateNotBeforeEndDate,
} from './start-date-before-end-date-validator-function';

describe('StartDateBeforeEndDateValidator', () => {

  it('should return true if enddate is before startdate', () => {

    const control: AbstractControl = new FormGroup({
      startDate: new FormControl(new Date('02.01.2020')),
      endDate: new FormControl(new Date('01.01.2020'))
    });
    control.markAllAsTouched();

    const actual: ValidationErrors = StartDateBeforeEndDateValidator.Validate(control);

    expect(actual)
      .toEqual(startDateNotBeforeEndDate);
  });

  it('should return undefined if enddate is after startdate', () => {
    const control: AbstractControl = new FormGroup({
      startDate: new FormControl(new Date('01.01.2020')),
      endDate: new FormControl(new Date('02.01.2020'))
    });
    control.markAllAsTouched();

    const actual: ValidationErrors = StartDateBeforeEndDateValidator.Validate(control);

    expect(actual)
      .toBeUndefined();
  });
});
