import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';
import { CycleUnit } from '../../model/ui/cycle-unit';
import { dateRangeInRangewithinAnotherDatesError } from './date-range-in-range-within-another-dates-error';
import { AbstractValidator, register } from '../abstract-validator';
import { I18n } from '@ngx-translate/i18n-polyfill';

export const dateInRangeOfAnotherCycleError: ValidationErrors = {
  dateInRangeOfAnotherCycleError: true
};

@register
export class DateNotInRangeOfAnotherCycleValidator extends AbstractValidator {

  constructor(private i18n: I18n) {
    super(i18n({
      id: 'dateInRangeOfAnotherCycleError',
      description: 'Date is in the date-range of another cycle',
      value: 'Das Datum liegt innerhalb der Zeitspanne eines anderen Zyklus.'
    }), dateInRangeOfAnotherCycleError);
  }

  static Validate(cycles: CycleUnit[]): ValidatorFn {
    return (control: AbstractControl): { [key: string]: boolean } => {
      const startDateFromInput: Date = control.get('startDate').value;
      const endDateFromInput: Date = control.get('endDate').value;

      if (startDateFromInput && endDateFromInput) {
        return periodCollidesWithOther(startDateFromInput, endDateFromInput, cycles);
      }

      return undefined;
    };
  }
}

export function periodCollidesWithOther(
  startDateFromInput: Date,
  endDateFromInput: Date,
  cycles: CycleUnit[]
): { [key: string]: boolean } {
  for (const cycle of cycles) {
    const startDateFromExistingCycle: Date = cycle.startDate;
    const endDateFromExistingCycle: Date = cycle.endDate;

    if (!(endDateFromInput < startDateFromExistingCycle || endDateFromExistingCycle < startDateFromInput)) {
      return dateRangeInRangewithinAnotherDatesError;
    }
  }

  return undefined;
}
