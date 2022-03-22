import { BaseChartOptions } from '../../shared/model/ui/dashboard/base-chart-options';
import { ConstructorType } from './constructor.type';

export const questionComponentMappings: {
  chartOptionsType: ConstructorType<BaseChartOptions>;
  componentType: ConstructorType<any>;
}[] = [];

export function CustomChartComponent(chartOptionsType: ConstructorType<BaseChartOptions>) {
  return (graphComponentType: ConstructorType<any>): void => {
    questionComponentMappings.push({
      chartOptionsType,
      componentType: graphComponentType,
    });
  };
}
