import { ChangeDetectorRef, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { NavigationSidebarComponent } from '../navigation-sidebar/navigation-sidebar.component';
import { CurrentOkrviewService } from '../current-okrview.service';
import { CycleUnit } from '../../shared/model/ui/cycle-unit';
import { Observable } from 'rxjs';
import { MediaMatcher } from '@angular/cdk/layout';

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
    private currentOkrViewService: CurrentOkrviewService
  ) {
    this.mobileQuery = media.matchMedia('(max-width: 700px)');
    this._mobileQueryListener = () => changeDetectorRef.detectChanges();
    this.mobileQuery.addListener(this._mobileQueryListener);
  }

  ngOnInit(): void {
    this.currentCycle$ = this.currentOkrViewService.getCurrentCycle$();
  }

  ngOnDestroy(): void {
    this.mobileQuery.removeListener(this._mobileQueryListener);
  }

  toggleSideBar(): void {
    this.sideBar.toggleOpenSideNav();
  }
}
