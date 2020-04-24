import { ObjectiveId } from '../../id-types';

export class CompanyStructure {
  id: number;
  name: string;
  label: string;
  objectives: ObjectiveId[];

  constructor(id: number, name: string, objectives: ObjectiveId[], label: string) {
    this.id = id;
    this.name = name;
    this.objectives = objectives;
    this.label = label;
  }
}
