import { FormControl, FormGroup, ValidationErrors } from '@angular/forms';
import { StartDateNotEqualEndDateValidator, startEqualsEndError } from './start-not-equal-end-validator-function';

describe('startNotEqualEndValidatorFunction', () => {

  it('return true if the start- and enddate are equal', () => {
    const control: FormGroup = new FormGroup({
      start: new FormControl(1),
      end: new FormControl(1)
    });

    const actual: ValidationErrors = StartDateNotEqualEndDateValidator.Validate(control);

    expect(actual)
      .toEqual(
        startEqualsEndError
      );

  });

  it('return undefined if the start- and enddate are not equal', () => {
    const control: FormGroup = new FormGroup({
      start: new FormControl(1),
      end: new FormControl(2)
    });

    const actual: ValidationErrors = StartDateNotEqualEndDateValidator.Validate(control);

    expect(actual)
      .toBeUndefined();

  });

});
