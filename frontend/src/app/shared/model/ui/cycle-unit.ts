import { CompanyId, CycleId } from '../id-types';

export class CycleUnit {
  id: CycleId;
  name: string;
  abbreviation: string;
  companyIds: CompanyId[];
  startDate: Date;
  endDate: Date;
  remainingDaysUntilEnd;
  cycleState: CycleState = CycleState.PREPARATION;
  isVisible: boolean;

  constructor(id: CycleId, name: string, companyIds: CompanyId[], startDate: Date, endDate: Date,
              cycleState: CycleState, isVisible: boolean) {
    this.id = id;
    this.name = name;
    this.abbreviation = this.abbreviate();
    this.companyIds = companyIds;
    this.startDate = startDate;
    this.endDate = endDate;
    this.cycleState = cycleState;
    this.isVisible = isVisible;
  }

  static getDefaultCycleUnit(): CycleUnit {
    return new CycleUnit(0, '', [], new Date(), new Date(), CycleState.PREPARATION, true);
  }

  abbreviate(): string {
    let resultingString: string = '';
    if (this.name.length > 20) {
      for (let i: number = 0; i <= 17; i++) {
        resultingString = resultingString + this.name[i];
      }

      return `${resultingString}...`;
    } else {
      return this.name;
    }
  }

  isCycleClosed(): boolean {
    return this.cycleState === CycleState.CLOSED;
  }

  isCycleActive(): boolean {
    return this.cycleState === CycleState.ACTIVE;
  }

  isCycleInPreparation(): boolean {
    return this.cycleState === CycleState.PREPARATION;
  }

  getCurrentCycleProgressNormalized(): number {
    const totalCycleDuration: number = this.endDate.getTime() - this.startDate.getTime();
    const passedCycleDuration: number = Date.now() - this.startDate.getTime();
    let progress: number = passedCycleDuration / totalCycleDuration;
    progress = Math.min(1, Math.max(0, progress));

    return progress;
  }
}

export enum CycleState {
  PREPARATION = 'PREPARATION',
  ACTIVE = 'ACTIVE',
  CLOSED = 'CLOSED'
}
