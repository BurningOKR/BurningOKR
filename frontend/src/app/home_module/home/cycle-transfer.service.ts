import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { CycleUnit } from '../../shared/model/ui/cycle-unit';

@Injectable({
  providedIn: 'root'
})
export class CycleTransferService {
  constructor() {
    this._transferredCycle$ = new BehaviorSubject<CycleUnit>(CycleUnit.getDefaultCycleUnit());
  }

  private readonly _transferredCycle$: BehaviorSubject<CycleUnit>;

  get transferredCycle$(): BehaviorSubject<CycleUnit> {
    return this._transferredCycle$;
  }

  exportCycle(cycle: CycleUnit): void {
    this._transferredCycle$.next(cycle);
  }
}
