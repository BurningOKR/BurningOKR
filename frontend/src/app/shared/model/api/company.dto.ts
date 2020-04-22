import { CompanyId, CycleId, DepartmentId, ObjectiveId } from '../id-types';

export interface CompanyDto {
  structureId?: CompanyId;
  cycleId: CycleId;
  structureName: string;
  departmentIds?: DepartmentId[];
  corporateObjectiveIds?: number[];
  objectiveIds?: ObjectiveId[];
  label: string;
}
