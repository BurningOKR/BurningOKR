import {
  Component,
  ComponentFactory,
  ComponentFactoryResolver,
  ComponentRef,
  Input,
  OnInit,
  ViewChild,
} from '@angular/core';
import { BaseChartOptions } from '../model/ui/base-chart-options';
import { BaseChartComponent } from '../charts/base-chart/base-chart.component';
import { chartComponentMappings } from '../decorator/chart-component.decorator';
import { ConstructorType } from '../decorator/constructor.type';
import { ChartHostDirective } from './chart-host.directive';

@Component({
  selector: 'app-chart-renderer',
  templateUrl: './chart-renderer.component.html',
  styleUrls: ['./chart-renderer.component.scss'],
})
export class ChartRendererComponent implements OnInit {
  @Input() chartOptions!: BaseChartOptions;
  @ViewChild(ChartHostDirective, { static: true }) chartHost!: ChartHostDirective;

  constructor(private componentFactoryResolver: ComponentFactoryResolver) {
  }

  ngOnInit(): void {
    const componentType: ConstructorType<BaseChartOptions> = chartComponentMappings.find(
      mapping => mapping.chartOptionsType.name === this.chartOptions.chartOptionsName,
    )?.componentType;
    console.log(`Chart Options: ${this.chartOptions}`);
    console.log(`Chart Options Name: ${this.chartOptions.chartOptionsName}`);
    chartComponentMappings.forEach(cC => console.log(`chartComponent: ${cC.chartOptionsType}`));
    chartComponentMappings.forEach(cC => console.log(`chartComponent Name: ${cC.chartOptionsType.name}`));
    console.log(`Component Type: ${componentType.name}`);
    if (!componentType) {
      throw new Error(
        `No Mapping from chartOptions ${this.chartOptions.chartOptionsName}
        to ComponentType found. Did you add the Decorator to the ChartComponent???`,
      );
    }

    const componentFactory: ComponentFactory<any> = this.componentFactoryResolver.resolveComponentFactory(componentType);

    const componentRef: ComponentRef<BaseChartComponent<BaseChartOptions>> =
      this.chartHost.viewContainerRef.createComponent<BaseChartComponent<BaseChartOptions>>(
        componentFactory,
      );
    componentRef.instance.chartOptions = this.chartOptions;
  }
}
