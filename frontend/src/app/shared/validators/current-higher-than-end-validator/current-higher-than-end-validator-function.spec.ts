import { FormControl, FormGroup } from '@angular/forms';
import { CurrentHigherThanEndValidator } from './current-higher-than-end-validator-function';

describe('currentHigherThanEndError', () => {

  it('should return error if current is higher than end and end is higher than start', () => {
    const control: FormGroup = new FormGroup({
      current: new FormControl(4),
      end: new FormControl(2),
      start: new FormControl(1)
    });
    const actual: any = CurrentHigherThanEndValidator.Validate(control);

    expect(actual)
      .toEqual({currentHigherThanEndError: true});
  });

  it('should return error if current is smaller than end and end is smaller than start', () => {
    const control: FormGroup = new FormGroup({
      current: new FormControl(4),
      end: new FormControl(5),
      start: new FormControl(10)
    });
    const actual: any = CurrentHigherThanEndValidator.Validate(control);

    expect(actual)
      .toEqual({currentHigherThanEndError: true});
  });

  it('should return no error if current is less than end and end is higher than start', () => {
    const control: FormGroup = new FormGroup({
      current: new FormControl(1),
      end: new FormControl(2),
      start: new FormControl(0)
    });
    const actual: any = CurrentHigherThanEndValidator.Validate(control);

    expect(actual)
      .toBeFalsy();
  });

  it('should return no error if current is higher than end and end is less than start', () => {
    const control: FormGroup = new FormGroup({
      current: new FormControl(3),
      end: new FormControl(2),
      start: new FormControl(10)
    });
    const actual: any = CurrentHigherThanEndValidator.Validate(control);

    expect(actual)
      .toBeFalsy();
  });

  it('should return no error if current is end and end is higher than start', () => {
    const control: FormGroup = new FormGroup({
      current: new FormControl(2),
      end: new FormControl(2),
      start: new FormControl(0)
    });
    const actual: any = CurrentHigherThanEndValidator.Validate(control);

    expect(actual)
      .toBeFalsy();
  });

  it('should return no error if current is end and end is less than start', () => {
    const control: FormGroup = new FormGroup({
      current: new FormControl(2),
      end: new FormControl(2),
      start: new FormControl(10)
    });
    const actual: any = CurrentHigherThanEndValidator.Validate(control);

    expect(actual)
      .toBeFalsy();
  });
});
