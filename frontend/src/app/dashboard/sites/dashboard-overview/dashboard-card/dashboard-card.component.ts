import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Dashboard } from '../../../model/ui/dashboard';
import { UserService } from '../../../../shared/services/helper/user.service';
import { User } from '../../../../shared/model/api/user';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-dashboard-card',
  templateUrl: './dashboard-card.component.html',
  styleUrls: ['./dashboard-card.component.scss'],
})
export class DashboardCardComponent implements OnInit {
  @Input() dashboard!: Dashboard;
  @Output() deleteDashboard: EventEmitter<Dashboard> = new EventEmitter<Dashboard>();
  creator$: Observable<User>;

  constructor(private userMapperService: UserService) {
  }

  ngOnInit(): void {
    this.creator$ = this.userMapperService.getUserById$(this.dashboard.creatorId);
  }
}
