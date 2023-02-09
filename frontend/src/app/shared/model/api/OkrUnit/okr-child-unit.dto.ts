import { OkrUnitDto } from './okr-unit.dto';
import { UnitType } from './unit-type.enum';
import { OkrUnitId } from '../../id-types';

export interface OkrChildUnitDto extends OkrUnitDto {
  __okrUnitType: UnitType;
  parentUnitId: OkrUnitId;
  isActive: boolean;
  isParentUnitABranch: boolean;
}
