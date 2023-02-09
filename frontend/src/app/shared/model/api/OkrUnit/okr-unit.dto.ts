import { ObjectiveId, OkrUnitId } from '../../id-types';

export interface OkrUnitDto {
  okrUnitId?: OkrUnitId;
  unitName: string;
  label: string;
  objectiveIds?: ObjectiveId[];
}
