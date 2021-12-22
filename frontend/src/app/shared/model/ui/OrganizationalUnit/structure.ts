import { OkrUnit } from './okr-unit';
import { ObjectiveId } from '../../id-types';
import {CompanyUnit} from "./company-unit";
import {OkrUnitSchema} from "../okr-unit-schema";

export class Structure extends CompanyUnit {

  okrUnitSchemas: OkrUnitSchema[];

  constructor(
    id: number,
    name: string,
    childUnitIds: number[],
    objectives: ObjectiveId[],
    cycleId: number,
    label: string,
    unitSchmas: OkrUnitSchema[])
  {
    super(id, name, objectives, childUnitIds, cycleId, label);
    this.okrUnitSchemas = unitSchmas;
  }
}
