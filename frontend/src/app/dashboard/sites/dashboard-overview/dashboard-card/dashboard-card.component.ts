import { Component, Input } from '@angular/core';
import { Dashboard } from '../../../model/ui/dashboard';

@Component({
  selector: 'app-dashboard-card',
  templateUrl: './dashboard-card.component.html',
  styleUrls: ['./dashboard-card.component.scss']
})
export class DashboardCardComponent{
  @Input() dashboard!: Dashboard;
}
