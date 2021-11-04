import { OkrUnitDto } from './okr-unit.dto';
import { UnitType } from './unit-type.enum';
import { OkrUnitId } from '../../id-types';

export class OkrChildUnitDto extends OkrUnitDto {
  // eslint-disable-next-line @typescript-eslint/naming-convention, no-underscore-dangle, id-blacklist, id-match
  __okrUnitType: UnitType;
  parentUnitId: OkrUnitId;
  isActive: boolean;
  isParentUnitABranch?: boolean;
}
