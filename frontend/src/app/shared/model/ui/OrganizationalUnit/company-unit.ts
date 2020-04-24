import { CompanyStructure } from './company-structure';
import { ObjectiveId } from '../../id-types';

export class CompanyUnit extends CompanyStructure {

  cycleId: number;
  departmentIds: number[];
  corporateObjectiveStructureIds: number[];

  constructor(
    id: number,
    name: string,
    departmentIds: number[],
    objectives: ObjectiveId[],
    cycleId: number,
    label: string,
    corporateObjectiveStructureIds: number[] = []
  ) {
    super(id, name, objectives, label);
    this.departmentIds = departmentIds;
    this.cycleId = cycleId;
    this.corporateObjectiveStructureIds = corporateObjectiveStructureIds;
  }
}
