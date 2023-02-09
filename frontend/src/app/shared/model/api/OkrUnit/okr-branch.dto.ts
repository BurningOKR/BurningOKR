import { OkrChildUnitDto } from './okr-child-unit.dto';
import { OkrUnitId } from '../../id-types';

export interface OkrBranchDto extends OkrChildUnitDto {
  okrChildUnitIds: OkrUnitId[];
}
