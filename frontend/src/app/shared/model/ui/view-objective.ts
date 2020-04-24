import { ObjectiveId } from '../id-types';

export class ViewObjective {
  id: ObjectiveId;
  name: string;
  description: string;
  remark: string;
  keyResultIdList: number[];
  progress: number;
  isActive: boolean;
  contactPersonId?: string;
  parentObjectiveId: ObjectiveId;
  parentStructureId: number;
  subObjectivesCount: number;
  review?: string;

  constructor(id: ObjectiveId, name: string, description: string, remark: string, progress: number, keyResultIdList: number[],
              isActive: boolean, parentObjectiveId: number, parentStructureId: number, contactPersonId: string, subObjectivesCount: number,
              review?: string) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.remark = remark;
    this.keyResultIdList = keyResultIdList;
    this.progress = progress;
    this.isActive = isActive;
    this.parentObjectiveId = parentObjectiveId;
    this.parentStructureId = parentStructureId;
    this.contactPersonId = contactPersonId;
    this.subObjectivesCount = subObjectivesCount;
    this.review = review;
  }

  hasKeyResults(): boolean {
    return this.keyResultIdList.length !== 0;
  }

  hasParentObjective(): boolean {
    return !!this.parentObjectiveId;
  }
}
