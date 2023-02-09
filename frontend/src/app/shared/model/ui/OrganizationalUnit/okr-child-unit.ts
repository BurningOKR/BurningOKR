import { OkrUnit } from './okr-unit';
import { OkrUnitId } from '../../id-types';
import { UnitType } from '../../api/OkrUnit/unit-type.enum';

export interface OkrChildUnit extends OkrUnit {
  parentUnitId: OkrUnitId;
  isActive: boolean;
  isParentUnitABranch: boolean;
  type: UnitType;
}
