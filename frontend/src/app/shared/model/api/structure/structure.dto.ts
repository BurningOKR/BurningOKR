import { ObjectiveId, StructureId } from '../../id-types';

export class StructureDto {
  structureId?: StructureId;
  structureName: string;
  label: string;
  objectiveIds?: ObjectiveId[];
}
