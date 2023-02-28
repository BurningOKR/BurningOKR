import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { AuthGuard } from '../core/auth/guards/auth.guard';
import { PlaygroundGuard } from '../core/auth/guards/playground.guard';
import { DashboardComponent } from './sites/dashboard/dashboard.component';
import { EditDashboardComponent } from './sites/edit-dashboard/edit-dashboard.component';

const routes: Routes = [
  { path: ':dashboardId', component: DashboardComponent, canActivate: [AuthGuard, PlaygroundGuard] },
  { path: ':dashboardId/edit-dashboard', component: EditDashboardComponent, canActivate: [AuthGuard, PlaygroundGuard] },
];

@NgModule({
  imports: [TranslateModule, RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class DashboardRoutingModule {
}
