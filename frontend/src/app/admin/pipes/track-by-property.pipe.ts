import { Pipe, PipeTransform } from '@angular/core';

interface TrackByFunctionCache {
  [propertyName: string]: <T>(index: number, item: T) => any;
}

// Since the resultant TrackBy functions are based purely on a static property name, we
// can cache these Functions across the entire app. No need to generate more than one
// Function for the same property.
// tslint:disable-next-line:no-null-keyword
const cache: TrackByFunctionCache = Object.create(null);

@Pipe({
  name: 'trackByProperty',
  pure: true
})
export class TrackByPropertyPipe implements PipeTransform {

  transform(propertyName: string): (index: number, item: any) => any {
    // Ensure cached function exists.
    if (!cache[propertyName]) {
      cache[propertyName] = function trackByProperty<T>(index: number, item: T): any {
        return (item[propertyName]);
      };
    }

    return (cache[propertyName]);
  }

}
