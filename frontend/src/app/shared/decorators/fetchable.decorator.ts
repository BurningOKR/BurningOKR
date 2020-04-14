import { FetchingService } from '../../core/services/fetching.service';
import { TypeOf } from '../../../typings';

export interface Fetchable {
  fetchData(): void;
}

export class FetchingServiceData {
  service: TypeOf<Fetchable>;
  loginRequired: boolean;

  constructor(service: TypeOf<Fetchable>, loggedIn: boolean = true) {
    this.service = service;
    this.loginRequired = loggedIn;
  }
}

export function Fetchable<T extends TypeOf<Fetchable>>(loginRequired?: boolean): (constructor: T) => void {
  return function(constructor: T): void {
    const data: FetchingServiceData = new FetchingServiceData(constructor, loginRequired);

    FetchingService.fetchingServices.push(data);
  };
}
