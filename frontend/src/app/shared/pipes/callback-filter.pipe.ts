import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'callbackFilter'
})
export class CallbackFilterPipe implements PipeTransform {

  transform(values: any, filter: any, callback: (value: any, filter: any) => boolean): any {
      return values.filter(value => callback(value, filter));
  }

}
