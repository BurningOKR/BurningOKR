import { ObjectiveId, OkrUnitId } from '../../id-types';

export interface OkrUnit {
  id: OkrUnitId;
  name: string;
  photo: string;
  label: string;
  objectives: ObjectiveId[];
}
