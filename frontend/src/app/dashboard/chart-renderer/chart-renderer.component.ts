import { Component, ComponentFactoryResolver, Input, OnInit, ViewChild } from '@angular/core';
import { BaseChartOptions } from '../../shared/model/ui/dashboard/base-chart-options';
import { BaseChartComponent } from '../charts/base-chart/base-chart.component';
import { chartComponentMappings } from '../decorator/chart-component.decorator';
import { ChartHostDirective } from './chart-host.directive';

@Component({
  selector: 'app-chart-renderer',
  templateUrl: './chart-renderer.component.html',
  styleUrls: ['./chart-renderer.component.scss']
})
export class ChartRendererComponent implements OnInit {
  @Input() chartOptions!: BaseChartOptions;
  @ViewChild(ChartHostDirective, { static: true }) chartHost!: ChartHostDirective;

  constructor(private componentFactoryResolver: ComponentFactoryResolver) {
  }

  ngOnInit(): void {
    // eslint-disable-next-line @typescript-eslint/typedef
    const componentType = chartComponentMappings.find(
      mapping => mapping.chartOptionsType === this.chartOptions.constructor,
    )?.componentType;

    if (!componentType) {
      throw new Error(
        `No Mapping from chartOptions ${this.chartOptions.constructor.name}
        to ComponentType found. Did you add the Decorator to the ChartComponent?`,
      );
    }

    // eslint-disable-next-line @typescript-eslint/typedef
    const componentFactory = this.componentFactoryResolver.resolveComponentFactory(componentType);

    // eslint-disable-next-line @typescript-eslint/typedef
    const componentRef =
      this.chartHost.viewContainerRef.createComponent<BaseChartComponent<BaseChartOptions>>(
        componentFactory,
      );
    componentRef.instance.chartOptions = this.chartOptions;
  }
}
