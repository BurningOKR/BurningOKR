import { ObjectiveId } from '../id-types';

export interface ObjectiveDto {
  id?: ObjectiveId;
  title: string;
  description: string;
  remark: string;
  isActive: boolean;
  keyResultIds?: number[];
  parentObjectiveId?: number;
  parentStructureId: number;
  contactPersonId?: string;
  subObjectiveIds?: number[];
  review?: string;
}
