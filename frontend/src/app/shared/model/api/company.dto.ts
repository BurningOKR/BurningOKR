import { DepartmentId } from './department.dto';
import { ObjectiveId } from './objective.dto';
import { CycleId } from './cycle.dto';

export type CompanyId = number;

export interface CompanyDto {
  structureId?: CompanyId;
  cycleId: CycleId;
  structureName: string;
  departmentIds?: DepartmentId[];
  corporateObjectiveIds?: number[];
  objectiveIds?: ObjectiveId[];
  label: string;
}
