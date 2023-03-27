import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { AuthGuard } from '../core/auth/guards/auth.guard';
import { PlaygroundGuard } from '../core/auth/guards/playground.guard';
import { DashboardComponent } from './sites/dashboard/dashboard.component';
import { EditDashboardComponent } from './sites/edit-dashboard/edit-dashboard.component';
import { CanDeactivateGuard } from '../core/auth/guards/can-deactivate.guard';

const routes: Routes = [
  { path: ':dashboardId', component: DashboardComponent, canActivate: [AuthGuard, PlaygroundGuard] },
  {
    path: ':dashboardId/edit-dashboard',
    component: EditDashboardComponent,
    canActivate: [AuthGuard, PlaygroundGuard],
    canDeactivate: [CanDeactivateGuard],
  },
];

@NgModule({
  imports: [TranslateModule, RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class DashboardRoutingModule {
}
