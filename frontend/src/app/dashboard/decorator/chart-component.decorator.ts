import { BaseChartOptions } from '../../shared/model/ui/dashboard/base-chart-options';
import { ConstructorType } from './constructor.type';

export const chartComponentMappings: {
  chartOptionsType: ConstructorType<BaseChartOptions>;
  componentType: ConstructorType<any>;
}[] = [];

export function CustomChartComponent(chartOptionsType: ConstructorType<BaseChartOptions>) {
  return (chartComponentType: ConstructorType<any>): void => {
    chartComponentMappings.push({
      chartOptionsType,
      componentType: chartComponentType,
    });
  };
}
