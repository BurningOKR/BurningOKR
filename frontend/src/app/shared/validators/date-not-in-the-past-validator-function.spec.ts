import { FormControl, ValidationErrors } from '@angular/forms';
import { dateNotInThePastValidatorFunction } from './date-not-in-the-past-validator-function';
import { dateNoInThePastError } from './errors/date-not-in-the-past-error';

describe('dateNotInThePastValidatorFunction', () => {

  it('should return undefined if date is not in the past', () => {

    const control: FormControl = new FormControl(new Date());

    const actual: ValidationErrors = dateNotInThePastValidatorFunction(control);

    expect(actual)
      .toBeUndefined();
  });

  it('should return error if date is in the past', () => {

    const control: FormControl = new FormControl(new Date('01.01.2000'));

    const actual: ValidationErrors = dateNotInThePastValidatorFunction(control);

    expect(actual)
      .toEqual(dateNoInThePastError);
  });
});
