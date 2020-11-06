import { Unit } from '../api/unit.enum';
import { CompanyId, KeyResultId, ObjectiveId } from '../id-types';
import { ViewKeyResultMilestone } from './view-key-result-milestone';

export class ViewKeyResult {
  start: number;
  current: number;
  end: number;
  unit: Unit;
  keyResult: string;
  description: string;
  commentIdList: CompanyId[];
  id: KeyResultId;
  parentObjectiveId: ObjectiveId;
  viewKeyResultMilestones: ViewKeyResultMilestone[];

  constructor(
    id: KeyResultId,
    start: number,
    current: number,
    end: number,
    unit: Unit,
    keyResultTitle: string,
    description: string,
    parentObjectiveId: ObjectiveId,
    commentIdList: CompanyId[],
    viewKeyResultMilestones: ViewKeyResultMilestone[],
  ) {
    this.id = id;
    this.start = start;
    this.current = current;
    this.end = end;
    this.unit = unit;
    this.keyResult = keyResultTitle;
    this.description = description;
    this.parentObjectiveId = parentObjectiveId;
    this.commentIdList = commentIdList;
    this.viewKeyResultMilestones = viewKeyResultMilestones;
  }

  getProgress(): number {
    return (this.current - this.start) / (this.end - this.start);
  }

  getProgressNormalized(): number {
    return Math.max(0, Math.min(1, this.getProgress()));
  }
}
