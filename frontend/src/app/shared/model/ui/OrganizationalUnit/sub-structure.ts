import { Structure } from './structure';
import { ObjectiveId, StructureId } from '../../id-types';

export abstract class SubStructure extends Structure {
  parentStructureId: StructureId;
  isActive: boolean;

  constructor(id: number, name: string, label: string, objectives: ObjectiveId[], parentStructureId: number, isActive: boolean) {
    super(id, name, label, objectives);
    this.parentStructureId = parentStructureId;
    this.isActive = isActive;
  }
}
