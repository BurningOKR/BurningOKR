import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Dashboard } from '../../../model/ui/dashboard';

@Component({
  selector: 'app-dashboard-card',
  templateUrl: './dashboard-card.component.html',
  styleUrls: ['./dashboard-card.component.scss']
})
export class DashboardCardComponent{
  @Input() dashboard!: Dashboard;
  @Output() deleteDashboard: EventEmitter<Dashboard> = new EventEmitter<Dashboard>();
}
