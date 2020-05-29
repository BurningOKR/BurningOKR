import { CompanyId, CycleId, DepartmentId, ObjectiveId } from '../id-types';
import { StructureDto } from './structure.dto';

export interface CompanyDto extends StructureDto {
  cycleId: CycleId;
  departmentIds?: DepartmentId[];
  corporateObjectiveIds?: number[];
}
