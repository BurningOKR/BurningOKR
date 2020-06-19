import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import { CycleUnit } from '../../shared/model/ui/cycle-unit';
import { CurrentCycleService } from '../current-cycle.service';
import { map } from 'rxjs/operators';

@Component({
  selector: 'app-cycle-list-dropdown',
  templateUrl: './cycle-list-dropdown.component.html',
  styleUrls: ['./cycle-list-dropdown.component.scss']
})
export class CycleListDropdownComponent implements OnInit {

  currentCycle$: Observable<CycleUnit>;
  currentCycleList$: Observable<CycleUnit[]>;

  constructor(private currentCycleService: CurrentCycleService,
              private router: Router,
              private route: ActivatedRoute
  ) {
  }

  ngOnInit(): void {
    this.updateCycleList();
  }

  updateCycleList(): void {
    this.currentCycleList$ = this.currentCycleService.getCurrentCycleList$()
      .pipe(
        map(cycleList => {
            const newCycleList: CycleUnit[] = cycleList.sort((a, b) => {
              return a.startDate > b.startDate ? -1 : 1;
            });

            return this.removeAllInvisibleCyclesFromCycleList(newCycleList);
          }
        )
      );

    this.currentCycle$ = this.currentCycleService.getCurrentCycle$();
  }

  private removeAllInvisibleCyclesFromCycleList(cycleList: CycleUnit[]): CycleUnit[] {
    return cycleList.filter(cycle => cycle.isVisible);
  }

  onSelectCycle(cycleUnit: CycleUnit): void {
    const chosenCompanyId: number = cycleUnit.companyIds[0];
    this.router.navigate([`../okr/companies/${chosenCompanyId}`], {relativeTo: this.route})
      .catch();
  }

}
