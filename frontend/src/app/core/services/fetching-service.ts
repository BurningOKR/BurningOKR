import { Injectable, Injector } from '@angular/core';
import { Fetchable } from '../../shared/decorators/fetchable.decorator';

@Injectable({
  providedIn: 'root'
})
export class FetchingService {

  static fetchingServices: (new(...args: any[]) => Fetchable)[] = [];

  constructor(private injector: Injector) {

  }

  refetchAll(): void {
    FetchingService.fetchingServices.forEach((serviceType: any) => {
      const serice: Fetchable = this.injector.get(serviceType);
      serice.fetchData();
    });
  }
}
