import { Component, OnDestroy, OnInit } from '@angular/core';
import { CurrentOkrviewService } from '../current-okrview.service';
import { Subscription } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import { CycleUnit } from '../../shared/model/ui/cycle-unit';

@Component({
  selector: 'app-cycle-list-dropdown',
  templateUrl: './cycle-list-dropdown.component.html',
  styleUrls: ['./cycle-list-dropdown.component.scss']
})
export class CycleListDropdownComponent implements OnInit, OnDestroy {

  cycleListSubscription: Subscription;
  currentCycleSubscription: Subscription;

  currentCycle: CycleUnit;
  currentCycleList: CycleUnit[];

  constructor(private currentOkrViewService: CurrentOkrviewService,
              private router: Router,
              private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.updateCycleList();
  }

  updateCycleList(): void {
    this.cycleListSubscription = this.currentOkrViewService.getCurrentCycleList$()
      .subscribe(cycleList => {
        this.currentCycleList = cycleList.sort((a, b) => {
          return a.startDate > b.startDate ? -1 : 1;
        });
        this.removeAllInvisibleCyclesFromCycleList();
      });
    this.currentCycleSubscription = this.currentOkrViewService.getCurrentCycle$()
      .subscribe(cycle => {
        this.currentCycle = cycle;
      });
  }

  private removeAllInvisibleCyclesFromCycleList(): void {
    this.currentCycleList = this.currentCycleList.filter(cycle => cycle.isVisible);
  }

  onSelectCycle(): void {
    const chosenCompanyId: number = this.currentCycle.companyIds[0];
    this.router.navigate([`../okr/companies/${chosenCompanyId}`], { relativeTo: this.route })
      .catch();
  }

  ngOnDestroy(): void {
    this.cycleListSubscription.unsubscribe();
    this.currentCycleSubscription.unsubscribe();
  }

}
