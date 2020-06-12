import { OkrChildUnitDto } from './okr-child-unit.dto';
import { OkrUnitId } from '../../id-types';

export class OkrBranchDto extends OkrChildUnitDto {
  subUnitIds: OkrUnitId[];
}
