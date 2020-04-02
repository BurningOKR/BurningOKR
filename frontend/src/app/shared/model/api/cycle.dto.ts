import { CycleState } from '../ui/cycle-unit';

export type CycleId = number;

export interface CycleDto {
  id?: CycleId;
  name: string; // required length > 0
  companyIds: number[]; // required
  plannedStartDate: number[]; // required
  plannedEndDate: number[]; // required
  cycleState: CycleState;
  isVisible: boolean;
}
