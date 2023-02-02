/* eslint-disable @typescript-eslint/promise-function-async */
import { TranslateModule } from '@ngx-translate/core';
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
import { TopicDraftsComponent } from './topic-drafts/topic-drafts-component/topic-drafts.component';
import { environment } from '../environments/environment';

const routes: Routes = [
  {
    path: 'demo', loadChildren: () => import('./demo/demo.module')
      .then(mod => mod.DemoModule),
  },
  {
    path: 'okr', loadChildren: () => import('./okrview/okrview.module').then(mod => mod.OkrviewModule),
    canActivate: [NotInitiliazedGuard, AuthGuard],
  },
  { path: 'landingpage', component: LandingPageNavigationComponent, canActivate: [NotInitiliazedGuard, AuthGuard] },
  { path: 'companies', component: OkrUnitDashboardComponent, canActivate: [NotInitiliazedGuard, AuthGuard] },
  { path: 'admin', component: AdminViewComponent, canActivate: [NotInitiliazedGuard, AuthGuard, AdminRoleGuard] },
  {
    path: 'cycle-admin/:companyId',
    component: CycleAdminContainerComponent,
    canActivate: [NotInitiliazedGuard, AuthGuard, AdminRoleGuard],
  },
  {
    path: 'submitted-topic-drafts',
    component: TopicDraftsComponent,
    canActivate: [NotInitiliazedGuard, AuthGuard],
  },
  {
    path: 'auth', loadChildren: () => import('./core/auth/auth.module').then(mod => mod.AuthModule),
  },
  { path: 'error', component: ErrorComponent },
  { path: 'noMailInformation', component: NoMailInformationComponent },
  { path: '', redirectTo: environment.playground ? 'demo' : 'landingpage', pathMatch: 'full' },
  { path: '**', redirectTo: environment.playground ? 'landingpage' : '' },
];

@NgModule({
  imports: [TranslateModule, RouterModule.forRoot(routes, {
    useHash: false, onSameUrlNavigation: 'reload',
    relativeLinkResolution: 'legacy', scrollPositionRestoration: 'enabled',
  })],
  exports: [RouterModule],
})
export class AppRoutingModule {
}
