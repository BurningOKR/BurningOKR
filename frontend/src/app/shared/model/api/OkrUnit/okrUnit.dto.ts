import { ObjectiveId, OkrUnitId } from '../../id-types';

export class OkrUnitDto {
  unitId?: OkrUnitId;
  unitName: string;
  label: string;
  objectiveIds?: ObjectiveId[];
}
