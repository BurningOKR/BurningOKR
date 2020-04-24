import { CompanyStructure } from './company-structure';
import { DepartmentId, ObjectiveId } from '../../id-types';

export class CorporateObjectiveStructure extends CompanyStructure {
  parentStructureId: number;
  departmentIds: DepartmentId[];
  corporateObjectiveStructureIds: number[];

  constructor(id: number, name: string, objectives: ObjectiveId[], label: string, parentStructureId: number,
              departmentIds: DepartmentId[], corporateObjectiveStructureIds: number[]) {
    super(id, name, objectives, label);
    this.parentStructureId = parentStructureId;
    this.departmentIds = departmentIds;
    this.corporateObjectiveStructureIds = corporateObjectiveStructureIds;
  }
}
