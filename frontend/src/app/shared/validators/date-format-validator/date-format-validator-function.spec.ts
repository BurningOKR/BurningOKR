import { FormControl } from '@angular/forms';
import { DateFormValidator } from './date-format-validator-function';

describe('dateFormatValidatorFunction', () => {

  it('should return error if date is null', () => {
    const control: FormControl = new FormControl(null);

    const actual: any = DateFormValidator.Validate(control);

    expect(actual)
      .toEqual({ dateFormatError: true });
  });

  it('should return error if date is empty', () => {
    const control: FormControl = new FormControl('');

    const actual: any = DateFormValidator.Validate(control);

    expect(actual)
      .toEqual({ dateFormatError: true });
  });

  it('should return undefiend if date is date', () => {
    const control: FormControl = new FormControl(new Date());

    const actual: any = DateFormValidator.Validate(control);

    expect(actual)
      .toBeUndefined();
  });
});
