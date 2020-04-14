import { Injectable, Injector } from '@angular/core';
import { Fetchable } from '../../shared/decorators/fetchable.decorator';
import { TypeOf } from '../../../typings';

@Injectable({
  providedIn: 'root'
})
export class FetchingService {

  static fetchingServices: TypeOf<Fetchable>[] = [];

  constructor(private injector: Injector) {
  }

  refetchAll(): void {
    FetchingService.fetchingServices.forEach((serviceType: TypeOf<Fetchable>) => {
      const service: Fetchable = this.injector.get(serviceType);
      service.fetchData();
    });
  }

  refetchSingle(fetchableService: TypeOf<Fetchable>): void {
    const service: Fetchable = this.injector.get(fetchableService);
    service.fetchData();
  }
}
