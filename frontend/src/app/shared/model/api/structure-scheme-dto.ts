import { structureSchemaRole } from '../ui/structure-schema';
import { DepartmentId } from '../id-types';

export interface StructureSchemeDto {
  id: DepartmentId;
  name: string;
  subDepartments: StructureSchemeDto[];
  userRole: structureSchemaRole;
  isActive: boolean;
}
