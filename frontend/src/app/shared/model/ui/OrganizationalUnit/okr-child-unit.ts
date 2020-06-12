import { OkrUnit } from './okrUnit';
import { ObjectiveId, OkrUnitId } from '../../id-types';

export abstract class OkrChildUnit extends OkrUnit {
  parentUnitId: OkrUnitId;
  isActive: boolean;
  isParentUnitABranch: boolean;

  constructor(id: number,
              name: string,
              label: string,
              objectives: ObjectiveId[],
              parenUnitId: number,
              isActive: boolean,
              isParentUnitABranch?: boolean) {
    super(id, name, label, objectives);
    this.parentUnitId = parenUnitId;
    this.isActive = isActive;
    this.isParentUnitABranch = isParentUnitABranch;
  }
}
