import { NativeDateAdapter } from '@angular/material/core';
import { Injectable } from '@angular/core';

@Injectable() // TODO: shrink and refactor me
export class CustomDateAdapterService extends NativeDateAdapter {
  parse(value: any): Date | undefined {
    if ((typeof value === 'string') && (value.indexOf('.') > -1)) {
      const str: string[] = value.split('.');
      if (str.length < 3 || isNaN(+str[0]) || isNaN(+str[1]) || isNaN(+str[2]) || str[2].length < 2 || +str[1] > 12) {
        return undefined;
      }
      if (+str[2].length < 4) {
        const yearNumber: number = +str[2] + 2000;
        str[2] = `${yearNumber}`;
      }

      return new Date(Number(str[2]), Number(str[1]) - 1, Number(str[0]), 12);
    } else if ((typeof value === 'string') && (value.length === 8)) {
      if (isNaN(Number(value)) || isNaN(Number(value.charAt(0)))) {
        return undefined;
      }
      const year: number = Number(value.substring(4, 8));
      const month: number = Number(value.substring(2, 4));
      const day: number = Number(value.substring(0, 2));

      if (month > 12) {
        return undefined;
      }

      return new Date(year, month - 1, day, 12);
    }

    return undefined;
  }
}
