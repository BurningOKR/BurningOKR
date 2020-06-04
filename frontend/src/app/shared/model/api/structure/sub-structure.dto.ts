import { StructureDto } from './structure.dto';
import { StructureType } from './structure-type.enum';
import { StructureId } from '../../id-types';

export class SubStructureDto extends StructureDto {
  // tslint:disable-next-line:variable-name
  __structureType: StructureType;
  parentStructureId: StructureId;
  isActive: boolean;
}
