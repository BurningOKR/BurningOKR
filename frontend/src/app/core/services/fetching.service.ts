import { Injectable, Injector } from '@angular/core';
import { Fetchable } from '../../shared/decorators/fetchable.decorator';

type FetchableServiceType = (new(...args: any[]) => Fetchable);

@Injectable({
  providedIn: 'root'
})
export class FetchingService {

  static fetchingServices: FetchableServiceType[] = [];

  constructor(private injector: Injector) {
  }

  refetchAll(): void {
    FetchingService.fetchingServices.forEach((serviceType: FetchableServiceType) => {
      const service: Fetchable = this.injector.get(serviceType);
      service.fetchData();
    });
  }

  refetchSingle(fetchableService: FetchableServiceType): void {
    const service: Fetchable = this.injector.get(fetchableService);
    service.fetchData();
  }
}
