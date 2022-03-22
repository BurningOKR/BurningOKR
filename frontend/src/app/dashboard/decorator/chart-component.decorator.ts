import { BaseChartOptions } from '../../shared/model/ui/dashboard/base-chart-options';
import { ConstructorType } from './constructor.type';

export const questionComponentMappings: {
  graphType: ConstructorType<BaseChartOptions>;
  componentType: ConstructorType<any>;
}[] = [];

export function GraphComponent(questionType: ConstructorType<BaseChartOptions>) {
  return (graphComponentType: ConstructorType<any>): void => {
    questionComponentMappings.push({
      graphType: questionType,
      componentType: graphComponentType,
    });
  };
}
