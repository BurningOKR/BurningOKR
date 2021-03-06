import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { LocalGuard } from '../guards/local.guard';
import { ResetPasswordComponent } from './reset-password/reset-password.component';
import { SetPasswordComponent } from './set-password/set-password.component';
import { AuthGuard } from '../guards/auth.guard';
import { AdminRoleGuard } from '../../../admin/admin-role-guard';
import { NotLoggedInGuard } from './guards/not-logged-in.guard';
import { NotInitiliazedGuard } from '../init/not-initiliazed.guard';

const routes: Routes = [
  {path: '*', redirectTo: 'login', pathMatch: 'full'},
  {path: '', redirectTo: 'login', pathMatch: 'full'},
  {path: 'login', component: LoginComponent, canActivate: [LocalGuard, NotLoggedInGuard, NotInitiliazedGuard]},
  {path: 'resetpassword', component: ResetPasswordComponent, canActivate: [LocalGuard]},
  {path: 'setpassword/:emailIdentifier', component: SetPasswordComponent, canActivate: [LocalGuard]},
  {
    path: 'users', loadChildren: () => import('./user-management/user-management.module')
      .then(mod => mod.UserManagementModule),
    canActivate: [LocalGuard, AuthGuard, AdminRoleGuard]
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class LocalAuthRoutingModule {
}
