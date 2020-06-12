import { OkrUnitDto } from './okrUnit.dto';
import { UnitType } from './unit-type.enum';
import { OkrUnitId } from '../../id-types';

export class OkrChildUnitDto extends OkrUnitDto {
  // tslint:disable-next-line:variable-name
  __okrUnitType: UnitType;
  parentUnitId: OkrUnitId;
  isActive: boolean;
  isParentUnitABranch?: boolean;
}
