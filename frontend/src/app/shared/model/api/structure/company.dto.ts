import { CycleId, StructureId } from '../../id-types';
import { StructureDto } from './structure.dto';

export interface CompanyDto extends StructureDto {
  cycleId: CycleId;
  subStructureIds?: StructureId[];
  corporateObjectiveIds?: number[];
}
