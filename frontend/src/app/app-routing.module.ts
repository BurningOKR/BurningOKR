import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdminViewComponent } from './admin/admin-view.component';
import { LandingPageNavigationComponent } from './core/landing-page-router/landing-page-navigation.component';
import { AdminRoleGuard } from './admin/admin-role-guard';
import { CycleAdminContainerComponent } from './cycle-admin/cycle-admin-container/cycle-admin-container.component';
import { AuthGuard } from './core/auth/guards/auth.guard';
import { OkrUnitDashboardComponent } from './okr-units/okr-unit-dashboard/okr-unit-dashboard.component';
import { ErrorComponent } from './core/error/error.component';
import { NoMailInformationComponent } from './information/no-mail-information/no-mail-information.component';
import { NotInitiliazedGuard } from './core/auth/init/not-initiliazed.guard';

const routes: Routes = [
  {path: 'demo', loadChildren: () => import('./demo/demo.module')
      .then(mod => mod.DemoModule)
  },
  {
    path: 'okr', loadChildren: () => import('./okrview/okrview.module')
      .then(mod => mod.OkrviewModule),
    canActivate: [NotInitiliazedGuard, AuthGuard]
  },
  { path: 'landingpage', component: LandingPageNavigationComponent, canActivate: [NotInitiliazedGuard, AuthGuard] },
  { path: 'companies', component: OkrUnitDashboardComponent, canActivate: [NotInitiliazedGuard, AuthGuard] },
  { path: 'admin', component: AdminViewComponent, canActivate: [NotInitiliazedGuard, AuthGuard, AdminRoleGuard] },
  {
    path: 'cycle-admin/:companyId',
    component: CycleAdminContainerComponent,
    canActivate: [NotInitiliazedGuard, AuthGuard, AdminRoleGuard]
  },
  {
    path: 'auth', loadChildren: () => import('./core/auth/auth.module')
      .then(mod => mod.AuthModule)
  },
  { path: 'error', component: ErrorComponent },
  { path: 'noMailInformation', component: NoMailInformationComponent },
  { path: '', redirectTo: 'demo', pathMatch: 'full' },
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: false, onSameUrlNavigation: 'reload', scrollPositionRestoration: 'enabled' })],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
