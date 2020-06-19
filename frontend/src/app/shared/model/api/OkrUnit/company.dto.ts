import { CycleId, OkrUnitId } from '../../id-types';
import { OkrUnitDto } from './okr-unit.dto';

export interface CompanyDto extends OkrUnitDto {
  cycleId: CycleId;
  okrChildUnitIds?: OkrUnitId[];
  corporateObjectiveIds?: number[];
}
