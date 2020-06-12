import { ObjectiveId, OkrUnitId } from '../../id-types';

export class OkrUnitDto {
  okrUnitId?: OkrUnitId;
  unitName: string;
  label: string;
  objectiveIds?: ObjectiveId[];
}
