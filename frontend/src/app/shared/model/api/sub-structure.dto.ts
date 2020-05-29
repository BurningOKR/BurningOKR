import { StructureDto } from './structure.dto';
import { StructureType } from './structure-type.enum';
import { DepartmentId, StructureId } from '../id-types';

export interface SubStructureDto extends StructureDto {
  structureType: StructureType;
  parentStructureId: StructureId;
  subDepartmentIds: DepartmentId[];
  isActive: boolean;
}
