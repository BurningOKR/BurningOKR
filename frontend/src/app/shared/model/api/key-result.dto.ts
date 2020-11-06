import { Unit } from './unit.enum';
import { KeyResultId } from '../id-types';
import { KeyResultMilestoneDto } from './key-result-milestone.dto';

export interface KeyResultDto {
  startValue: number;
  currentValue: number;
  targetValue: number;
  unit: keyof Unit;
  title: string;
  description: string;
  id?: KeyResultId;
  noteIds?: number[];
  parentObjectiveId?: number;
  keyResultMilestoneDtos?: KeyResultMilestoneDto[];
}
