import { FetchingService } from '../../core/services/fetching.service';

export interface Fetchable {
  fetchData(): void;
}

export function Fetchable<T extends new(...args: any[]) => Fetchable>(): (constructor: T) => void {
  return function(constructor: T): void {
    FetchingService.fetchingServices.push(constructor);
  };
}
