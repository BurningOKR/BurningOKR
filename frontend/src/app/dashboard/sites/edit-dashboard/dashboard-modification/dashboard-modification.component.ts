import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Dashboard } from '../../../model/ui/dashboard';

@Component({
  selector: 'app-dashboard-modification',
  templateUrl: './dashboard-modification.component.html',
  styleUrls: ['./dashboard-modification.component.scss'],
})
export class DashboardModificationComponent {
  @Input() dashboard: Dashboard;
  @Output() updateDashboard: EventEmitter<Dashboard> = new EventEmitter<Dashboard>();

}
