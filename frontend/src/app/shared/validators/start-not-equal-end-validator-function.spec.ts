import { FormControl, FormGroup, ValidationErrors } from '@angular/forms';
import { startNotEqualEndValidatorFunction } from './start-not-equal-end-validator-function';
import { startEqualsEndError } from './errors/start-not-equal-end-validator-error';

describe('startNotEqualEndValidatorFunction', () => {

  it('return true if the start- and enddate are equal', () => {
    const control: FormGroup = new FormGroup({
      start: new FormControl(1),
      end: new FormControl(1)
    });

    const actual: ValidationErrors = startNotEqualEndValidatorFunction(control);

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

    const actual: ValidationErrors = startNotEqualEndValidatorFunction(control);

    expect(actual)
      .toBeUndefined();

  });

});
