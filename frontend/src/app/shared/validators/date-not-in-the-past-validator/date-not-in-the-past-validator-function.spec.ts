import { FormControl, ValidationErrors } from '@angular/forms';
import { dateIsInThePastError, DateNotInThePastValidator } from './date-not-in-the-past-validator-function';

describe('dateNotInThePastValidatorFunction', () => {

  it('should return undefined if date is not in the past', () => {

    const control: FormControl = new FormControl(new Date());

    const actual: ValidationErrors = DateNotInThePastValidator.Validate(control);

    expect(actual)
      .toBeUndefined();
  });

  it('should return error if date is in the past', () => {

    const control: FormControl = new FormControl(new Date('01.01.2000'));

    const actual: ValidationErrors = DateNotInThePastValidator.Validate(control);

    expect(actual)
      .toEqual(dateIsInThePastError);
  });
});
