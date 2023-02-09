import { OkrUnit } from './okr-unit';

export interface CompanyUnit extends OkrUnit {

  cycleId: number;
  okrChildUnitIds: number[];
}
