import { SubStructureDto } from './sub-structure.dto';
import { StructureId } from '../../id-types';

export class CorporateObjectiveStructureDto extends SubStructureDto {
  subStructureIds: StructureId[];
}
