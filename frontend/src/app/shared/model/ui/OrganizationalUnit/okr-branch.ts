import { ObjectiveId, OkrUnitId } from '../../id-types';
import { OkrChildUnit } from './okr-child-unit';
import { ParentUnit } from './parent-unit';

export class OkrBranch extends OkrChildUnit implements ParentUnit {
  okrChildUnitIds: OkrUnitId[];

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
    this.okrChildUnitIds = childUnit;
  }
}
