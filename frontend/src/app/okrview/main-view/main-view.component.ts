import { ChangeDetectorRef, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { NavigationSidebarComponent } from '../navigation-sidebar/navigation-sidebar.component';
import { CycleUnit } from '../../shared/model/ui/cycle-unit';
import { Observable } from 'rxjs';
import { MediaMatcher } from '@angular/cdk/layout';
import { CurrentCycleService } from '../current-cycle.service';

@Component({
  selector: 'app-main-view',
  templateUrl: './main-view.component.html',
  styleUrls: ['./main-view.component.scss']
})
export class MainViewComponent implements OnInit, OnDestroy {
  @ViewChild('sideBar', { static: false }) sideBar: NavigationSidebarComponent;

  mobileQuery: MediaQueryList;
  private readonly _mobileQueryListener: () => void;

  currentCycle$: Observable<CycleUnit>;

  constructor(
    private changeDetectorRef: ChangeDetectorRef,
    private media: MediaMatcher,
    private currentCycleService: CurrentCycleService
  ) {
    this.mobileQuery = media.matchMedia('(max-width: 700px)');
    this._mobileQueryListener = () => changeDetectorRef.detectChanges();
    this.mobileQuery.addListener(this._mobileQueryListener);
  }

  ngOnInit(): void {
    this.currentCycle$ = this.currentCycleService.getCurrentCycle$();
  }

  ngOnDestroy(): void {
    this.mobileQuery.removeListener(this._mobileQueryListener);
  }

  toggleSideBar(): void {
    this.sideBar.toggleOpenSideNav();
  }
}
