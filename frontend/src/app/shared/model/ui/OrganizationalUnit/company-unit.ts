import { Structure } from './structure';
import { ObjectiveId } from '../../id-types';

export class CompanyUnit extends Structure {

  cycleId: number;
  subStructureIds: number[];

  constructor(id: number, name: string, subStructureIds: number[], objectives: ObjectiveId[], cycleId: number, label: string) {
    super(id, name, label, objectives);
    this.cycleId = cycleId;
    this.subStructureIds = subStructureIds;
  }
}
