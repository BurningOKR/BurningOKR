import { BaseChartOptions } from '../model/ui/base-chart-options';
import { ConstructorType } from './constructor.type';

export const chartComponentMappings: {
  chartOptionsType: ConstructorType<BaseChartOptions>;
  componentType: ConstructorType<any>;
}[] = [];

export function CustomChartComponent(chartOptionsType: ConstructorType<BaseChartOptions>) {
  console.log(`Hello World! chartOptionsType: ${chartOptionsType}`);

  return (chartComponentType: ConstructorType<any>): void => {
    chartComponentMappings.push({
      chartOptionsType,
      componentType: chartComponentType,
    });
    console.log(`(Inside Return) chartComponentType Name: ${chartComponentType.name}`);
  };
}
