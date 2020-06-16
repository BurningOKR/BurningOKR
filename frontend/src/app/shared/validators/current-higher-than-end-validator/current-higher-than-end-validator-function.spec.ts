import { FormControl, FormGroup } from '@angular/forms';
import { CurrentHigherThanEndValidator } from './current-higher-than-end-validator-function';

describe('currentHigherThanEndError', () => {

  it('should return error if current is higher than end', () => {
    const control: FormGroup = new FormGroup({
      current: new FormControl(4),
      end: new FormControl(2)
    });
    const actual: any = CurrentHigherThanEndValidator.Validate(control);

    expect(actual)
      .toEqual({currentHigherThanEndError: true});
  });

  it('should return noting if current is less than end', () => {
    const control: FormGroup = new FormGroup({
      current: new FormControl(1),
      end: new FormControl(2)
    });
    const actual: any = CurrentHigherThanEndValidator.Validate(control);

    expect(actual)
      .toBeFalsy();
  });
});
