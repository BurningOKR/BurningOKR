import { Directive, ViewContainerRef } from '@angular/core';

@Directive({
  selector: '[appChartHost]',
})
export class ChartHostDirective {
  constructor(public viewContainerRef: ViewContainerRef) {
  }
}
