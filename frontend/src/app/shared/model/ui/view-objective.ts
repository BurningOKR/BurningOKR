import { CommentId, ObjectiveId } from '../id-types';
import { ViewCommentRequiredAttributes } from './view-comment-required-attributes';

export class ViewObjective implements ViewCommentRequiredAttributes {
  id: ObjectiveId;
  name: string;
  description: string;
  remark: string;
  keyResultIdList: number[];
  progress: number;
  isActive: boolean;
  contactPersonId?: string;
  parentObjectiveId: ObjectiveId;
  parentUnitId: number;
  subObjectivesCount: number;
  review?: string;
  commentIdList: CommentId[]; // ToDo(C.K. check usage)

  constructor(id: ObjectiveId, name: string, description: string, remark: string, progress: number, keyResultIdList: number[],
              isActive: boolean, parentObjectiveId: number, parentUnitId: number, contactPersonId: string, subObjectivesCount: number,
              commentIdList: CommentId[], review?: string) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.remark = remark;
    this.keyResultIdList = keyResultIdList;
    this.progress = progress;
    this.isActive = isActive;
    this.parentObjectiveId = parentObjectiveId;
    this.parentUnitId = parentUnitId;
    this.contactPersonId = contactPersonId;
    this.subObjectivesCount = subObjectivesCount;
    this.commentIdList = commentIdList;
    this.review = review;
  }

  hasKeyResults(): boolean {
    return this.keyResultIdList.length !== 0;
  }

  hasParentObjective(): boolean {
    return !!this.parentObjectiveId;
  }
}
