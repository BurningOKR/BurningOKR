import { CycleState } from '../ui/cycle-unit';
import { CycleId } from '../id-types';

export interface CycleDto {
  id?: CycleId;
  name: string; // required length > 0
  companyIds: number[]; // required
  plannedStartDate: string; // required
  plannedEndDate: string; // required
  cycleState: CycleState;
  isVisible: boolean;
}
