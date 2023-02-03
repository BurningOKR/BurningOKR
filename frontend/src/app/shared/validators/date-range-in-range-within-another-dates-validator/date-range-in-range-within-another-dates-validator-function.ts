import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';
import { CycleUnit } from '../../model/ui/cycle-unit';
import { dateRangeInRangewithinAnotherDatesError } from './date-range-in-range-within-another-dates-error';
import { AbstractValidator, register } from '../abstract-validator';
import { TranslateService } from '@ngx-translate/core';

export const dateInRangeOfAnotherCycleError: ValidationErrors = {
  dateInRangeOfAnotherCycleError: true,
};

@register
export class DateNotInRangeOfAnotherCycleValidator extends AbstractValidator {

  constructor(private translate: TranslateService) {
    super(
      translate.instant('date-range-in-range-within-another-dates-validator-function.message'),
      dateInRangeOfAnotherCycleError,
    );
  }

  static Validate(cycles: CycleUnit[]): ValidatorFn {
    return (control: AbstractControl): { [key: string]: boolean } => {
      const startDateFromInput: Date = control.get('startDate').value;
      const endDateFromInput: Date = control.get('endDate').value;

      if (startDateFromInput && endDateFromInput) {
        return DateNotInRangeOfAnotherCycleValidator.periodCollidesWithOther(
          startDateFromInput, endDateFromInput, cycles);
      }

      return undefined;
    };
  }

  private static periodCollidesWithOther(
    startDateFromInput: Date,
    endDateFromInput: Date,
    cycles: CycleUnit[],
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

}
