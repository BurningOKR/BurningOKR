import { ChangeDetectorRef, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { NavigationSidebarComponent } from '../navigation-sidebar/navigation-sidebar.component';
import { CycleUnit } from '../../shared/model/ui/cycle-unit';
import { Observable } from 'rxjs';
import { MediaMatcher } from '@angular/cdk/layout';
import { CurrentCycleService } from '../current-cycle.service';

@Component({
  selector: 'app-main-view',
  templateUrl: './main-view.component.html',
  styleUrls: ['./main-view.component.scss'],
})
export class MainViewComponent implements OnInit, OnDestroy {
  @ViewChild('sideBar') sideBar: NavigationSidebarComponent;

  mobileQuery: MediaQueryList;
  currentCycle$: Observable<CycleUnit>;

  private readonly _mobileQueryListener: () => void;

  constructor(
    private changeDetectorRef: ChangeDetectorRef,
    private media: MediaMatcher,
    private currentCycleService: CurrentCycleService,
  ) {
    this.mobileQuery = media.matchMedia('(max-width: 768px)');
    this._mobileQueryListener = () => changeDetectorRef.detectChanges();
    this.mobileQuery.addEventListener('change', this._mobileQueryListener);
  }

  ngOnInit(): void {
    this.currentCycle$ = this.currentCycleService.getCurrentCycle$();
  }

  ngOnDestroy(): void {
    this.mobileQuery.removeEventListener('change', this._mobileQueryListener);
  }

  toggleSideBar(): void {
    this.sideBar.toggleSideNav();
  }
}
