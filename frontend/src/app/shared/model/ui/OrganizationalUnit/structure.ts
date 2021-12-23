import { OkrUnit } from './okr-unit';
import { ObjectiveId } from '../../id-types';
import {CompanyUnit} from "./company-unit";
import {OkrUnitSchema} from "../okr-unit-schema";

export class Structure extends OkrUnit {

  substructure: Structure[];

  constructor(
    id: number,
    name: string,
    label: string,
    objectives: ObjectiveId[],
    substructure: Structure[])
  {
    super(id, name, label, objectives);
    this.substructure = substructure;
  }
}
