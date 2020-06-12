import { CycleId, OkrUnitId } from '../../id-types';
import { OkrUnitDto } from './okrUnit.dto';

export interface CompanyDto extends OkrUnitDto {
  cycleId: CycleId;
  childUnitIds?: OkrUnitId[];
  corporateObjectiveIds?: number[];
}
