import { OkrUnit } from './okrUnit';
import { OkrUnitId, ObjectiveId } from '../../id-types';
import { OkrChildUnit } from './okr-child-unit';

export class OkrBranch extends OkrChildUnit {
  okrUnitIds: OkrUnitId[];

  constructor(
    id: number,
    name: string,
    objectives: ObjectiveId[],
    label: string,
    parentUnitId: number,
    childUnit: OkrUnitId[],
    isActive: boolean,
    isParentUnitABranch?: boolean) {
    super(id, name, label, objectives, parentUnitId, isActive, isParentUnitABranch);
    this.okrUnitIds = childUnit;
  }
}
