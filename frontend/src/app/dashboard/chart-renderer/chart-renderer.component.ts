import {
  Component,
  ComponentFactory,
  ComponentFactoryResolver,
  ComponentRef,
  Input,
  OnInit,
  ViewContainerRef,
} from '@angular/core';

import { BaseChartOptions } from '../model/ui/base-chart-options';
import { BaseChartComponent } from '../charts/base-chart/base-chart.component';
import { ConstructorType } from '../decorator/constructor.type';
import { ChartInformationTypeEnum } from '../model/dto/chart-creation-options.dto';
import { LineChartComponent } from '../charts/basic-line-chart/line-chart.component';

@Component({
  selector: 'app-chart-renderer',
  templateUrl: './chart-renderer.component.html',
  styleUrls: ['./chart-renderer.component.scss'],
})
export class ChartRendererComponent implements OnInit {
  @Input() chartOptions!: BaseChartOptions;

  constructor(private componentFactoryResolver: ComponentFactoryResolver, private viewContainerRef: ViewContainerRef) {
  }

  ngOnInit(): void {
    let componentType: ConstructorType<any> = BaseChartComponent;
    switch (this.chartOptions.chartType) {
      case ChartInformationTypeEnum.LINE_PROGRESS:
        componentType = LineChartComponent;
        break;
      case ChartInformationTypeEnum.PIE_TOPICDRAFTOVERVIEW:
        componentType = LineChartComponent; //TODO: Warum ist hier auch LineChartComponent? - Daniel Jonas
        break;
      default:
        throw new Error(
          'Cannot Render Chart. Unknown Chart Type!',
        );
    }

    const componentFactory: ComponentFactory<any> = this.componentFactoryResolver.resolveComponentFactory(componentType);

    const componentRef: ComponentRef<BaseChartComponent<BaseChartOptions>> =
      this.viewContainerRef.createComponent<BaseChartComponent<BaseChartOptions>>(
        componentFactory,
      );
    componentRef.instance.chartOptions = this.chartOptions;
  }
}
