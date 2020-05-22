import { AbstractControl, ValidatorFn } from '@angular/forms';
import { CycleUnit } from '../model/ui/cycle-unit';
import { dateRangeInRangewithinAnotherDatesError } from './errors/date-range-in-range-within-another-dates-error';

export const dateRangeInRangeWithinAnotherDatesValidatorFunction: (cycles: CycleUnit[]) =>
  ValidatorFn = (cycles: CycleUnit[]): ValidatorFn => {
  return (control: AbstractControl): {[key: string]: boolean} => {
    const startDateFromInput: Date = control.get('startDate').value;
    const endDateFromInput: Date = control.get('endDate').value;

    if (startDateFromInput && endDateFromInput) {
      return periodCollidesWithOther(startDateFromInput, endDateFromInput, cycles);
      }

    return undefined;
    };
};

export function periodCollidesWithOther(
  startDateFromInput: Date,
  endDateFromInput: Date,
  cycles: CycleUnit[]
): {[key: string]: boolean} {
  for (const cycle of cycles) {
    const startDateFromExistingCycle: Date = cycle.startDate;
    const endDateFromExistingCycle: Date = cycle.endDate;

    if (!(endDateFromInput < startDateFromExistingCycle || endDateFromExistingCycle < startDateFromInput)) {
      return dateRangeInRangewithinAnotherDatesError;
    }
  }

  return undefined;
}
