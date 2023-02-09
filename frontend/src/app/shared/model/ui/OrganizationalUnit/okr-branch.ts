import { OkrUnitId } from '../../id-types';
import { OkrChildUnit } from './okr-child-unit';

export interface OkrBranch extends OkrChildUnit {
  okrChildUnitIds: OkrUnitId[];
}
