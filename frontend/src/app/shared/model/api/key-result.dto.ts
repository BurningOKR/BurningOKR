import { Unit } from './unit.enum';

export type KeyResultId = number;

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
