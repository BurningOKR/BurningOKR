import { OkrUnit } from './okrUnit';
import { ObjectiveId } from '../../id-types';

export class CompanyUnit extends OkrUnit {

  cycleId: number;
  okrChildUnitIds: number[];

  constructor(id: number, name: string, childUnitIds: number[], objectives: ObjectiveId[], cycleId: number, label: string) {
    super(id, name, label, objectives);
    this.cycleId = cycleId;
    this.okrChildUnitIds = childUnitIds;
  }
}
