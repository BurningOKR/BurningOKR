import { DepartmentStructureRole } from '../ui/department-structure';
import { DepartmentId } from './department.dto';

export interface DepartmentStructureDto {
  id: DepartmentId;
  name: string;
  subDepartments: DepartmentStructureDto[];
  userRole: DepartmentStructureRole;
  isActive: boolean;
}
