export interface CorporateObjectiveStructureDto {
  id: number;
  name: string;
  label: string;
  parentStructureId: number;
  corporateObjectiveStructureIds: number[];
  departmentIds: number[];
  objectiveIds: number[];
}
