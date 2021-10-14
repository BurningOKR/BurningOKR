import { OkrUnitRole } from '../../ui/okr-unit-schema';
import { OkrUnitId } from '../../id-types';

export interface OkrUnitSchemaDto {
  id: OkrUnitId;
  name: string;
  subDepartments: OkrUnitSchemaDto[];
  userRole: OkrUnitRole;
  isActive: boolean;
  isTeam: boolean;
}
