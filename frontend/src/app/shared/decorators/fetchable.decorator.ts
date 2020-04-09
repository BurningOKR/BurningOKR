import { FetchingService } from '../../core/services/fetching.service';
import { TypeOf } from '../../../typings';

export interface Fetchable {
  fetchData(): void;
}

export function Fetchable<T extends TypeOf<Fetchable>>(): (constructor: T) => void {
  return function(constructor: T): void {
    FetchingService.fetchingServices.push(constructor);
  };
}
