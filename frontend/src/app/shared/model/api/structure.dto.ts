import { CompanyId, ObjectiveId, StructureId } from '../id-types';

export interface StructureDto {
  structureId?: StructureId;
  structureName: string;
  label: string;
  objectiveIds?: ObjectiveId[];
}
