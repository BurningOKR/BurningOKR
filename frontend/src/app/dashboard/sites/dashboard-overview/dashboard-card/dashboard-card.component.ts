import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Dashboard } from '../../../model/ui/dashboard';
import { UserService } from '../../../../shared/services/helper/user.service';
import { User } from '../../../../shared/model/api/user';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';

@Component({
  selector: 'app-dashboard-card',
  templateUrl: './dashboard-card.component.html',
  styleUrls: ['./dashboard-card.component.scss'],
})
export class DashboardCardComponent implements OnInit {
  @Input() dashboard!: Dashboard;
  @Output() deleteDashboard: EventEmitter<Dashboard> = new EventEmitter<Dashboard>();
  creator$: Observable<User>;

  // chosenCycleWithHistoryCompany: CycleWithHistoryCompany;

  constructor(private userMapperService: UserService, private router: Router) {
  }

  ngOnInit(): void {
    this.creator$ = this.userMapperService.getUserById$(this.dashboard.creatorId);
  }

  selectDashboard(dashboard_id: number): void {
    this.router.navigate(
      [`dashboard/${dashboard_id}`], { replaceUrl: true },
    ).catch();
  }
}
