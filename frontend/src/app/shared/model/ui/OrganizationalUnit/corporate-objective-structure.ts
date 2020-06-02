import { Structure } from './structure';
import { StructureId, ObjectiveId } from '../../id-types';
import { SubStructure } from './sub-structure';

export class CorporateObjectiveStructure extends SubStructure {
  subStructureIds: StructureId[];

  constructor(
    id: number,
    name: string,
    objectives: ObjectiveId[],
    label: string,
    parentStructureId: number,
    subStructureIds: StructureId[],
    isActive: boolean) {
    super(id, name, label, objectives, parentStructureId, isActive);
    this.subStructureIds = subStructureIds;
  }
}
