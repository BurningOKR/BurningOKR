import { Pipe, PipeTransform } from '@angular/core';
import { Observable } from 'rxjs';

@Pipe({
  name: 'log',
})
export class LogPipe implements PipeTransform {

  transform(value: any): any {
    if (value instanceof Observable) {
      value.subscribe(console.log).unsubscribe();
    }

    return value;
  }
}
