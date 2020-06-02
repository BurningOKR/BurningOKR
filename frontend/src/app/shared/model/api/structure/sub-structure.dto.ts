import { StructureDto } from './structure.dto';
import { StructureType } from './structure-type.enum';
import { StructureId } from '../../id-types';

export interface SubStructureDto extends StructureDto {
  __structureType: StructureType;
  parentStructureId: StructureId;
  isActive: boolean;
}
