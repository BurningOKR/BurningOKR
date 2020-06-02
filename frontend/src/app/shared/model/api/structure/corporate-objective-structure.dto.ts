import { SubStructureDto } from './sub-structure.dto';
import { StructureId } from '../../id-types';

export interface CorporateObjectiveStructureDto extends SubStructureDto {
  subStructureIds: StructureId[];
}
