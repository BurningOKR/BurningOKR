import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdminViewComponent } from './admin/admin-view.component';
import { LandingPageNavigationComponent } from './core/landing-page-router/landing-page-navigation.component';
import { LogoutComponent } from './core/auth/components/logout/logout.component';
import { AdminRoleGuard } from './admin/admin-role-guard';
import { CycleAdminContainerComponent } from './cycle-admin/cycle-admin-container/cycle-admin-container.component';
import { AuthGuard } from './core/auth/guards/auth.guard';
import { RedirectComponent } from './core/auth/components/redirect/redirect.component';
import { StructureDashboardComponent } from './structures/structures-dashboard/structure-dashboard.component';
import { ErrorComponent } from './core/error/error.component';
import { LocalGuard } from './core/auth/guards/local.guard';
import { NoMailInformationComponent } from './information/no-mail-information/no-mail-information.component';

const routes: Routes = [
  {path: 'redirect', component: RedirectComponent},
  {path: '', component: LandingPageNavigationComponent, canActivate: [AuthGuard]},
  {path: 'okr', loadChildren: () => import('./okrview/okrview.module')
      .then(mod => mod.OkrviewModule),
   canActivate: [AuthGuard]
  },
  {path: 'landingpage', component: LandingPageNavigationComponent, canActivate: [AuthGuard]},
  {path: 'companies', component: StructureDashboardComponent, canActivate: [AuthGuard]},
  {path: 'admin', component: AdminViewComponent, canActivate: [AuthGuard, AdminRoleGuard]},
  {
    path: 'cycle-admin/:companyId',
    component: CycleAdminContainerComponent,
    canActivate: [AuthGuard, AdminRoleGuard]
  },
  {
    path: 'auth', loadChildren: () => import('./core/auth/local-auth/local-auth.module')
      .then(mod => mod.LocalAuthModule),
    canActivate: [LocalGuard]
  },
  {path: 'logout', component: LogoutComponent},
  {path: 'error', component: ErrorComponent},
  {path: 'noMailInformation', component: NoMailInformationComponent},
  {path: '**', redirectTo: ''}
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: false, onSameUrlNavigation: 'reload'})],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
