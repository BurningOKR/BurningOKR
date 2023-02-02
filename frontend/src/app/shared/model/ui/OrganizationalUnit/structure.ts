import { OkrUnit } from './okr-unit';
import { ObjectiveId } from '../../id-types';

export class Structure extends OkrUnit {

  substructures: Structure[];

  constructor(
    id: number,
    name: string,
    label: string,
    objectives: ObjectiveId[],
    substructures: Structure[]) {
    super(id, name, label, objectives);
    this.substructures = substructures;
  }
}
