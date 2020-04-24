import { Unit } from './unit.enum';
import { KeyResultId } from '../id-types';

export interface KeyResultDto {
  startValue: number;
  currentValue: number;
  targetValue: number;
  unit: Unit;
  title: string;
  description: string;
  id?: KeyResultId;
  noteIds?: number[];
  parentObjectiveId?: number;
}
