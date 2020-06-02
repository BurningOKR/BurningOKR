import { structureSchemaRole } from '../../ui/structure-schema';
import { StructureId } from '../../id-types';

export interface StructureSchemeDto {
  id: StructureId;
  name: string;
  subDepartments: StructureSchemeDto[];
  userRole: structureSchemaRole;
  isActive: boolean;
}
