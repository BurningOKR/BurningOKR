import { ObjectiveId, OkrUnitId } from '../../id-types';

export abstract class OkrUnit {
  id: OkrUnitId;
  name: string;
  label: string;
  objectives: ObjectiveId[];

  constructor(id: number, name: string, label: string, objectives: ObjectiveId[]) {
    this.id = id;
    this.name = name;
    this.label = label;
    this.objectives = objectives;
  }
}
