import {TranslateModule} from '@ngx-translate/core';
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
import { DemoGuard } from '../../../demo/demo.guard';

const routes: Routes = [
  {path: '*', redirectTo: 'login', pathMatch: 'full'},
  {path: '', redirectTo: 'login', pathMatch: 'full'},
  {path: 'login', component: LoginComponent, canActivate: [LocalGuard, NotLoggedInGuard, NotInitiliazedGuard, DemoGuard]},
  {path: 'resetpassword', component: ResetPasswordComponent, canActivate: [LocalGuard, DemoGuard]},
  {path: 'setpassword/:emailIdentifier', component: SetPasswordComponent, canActivate: [LocalGuard, DemoGuard]},
  {
    path: 'users', loadChildren: async () => import('./user-management/user-management.module') //TODO testen
      .then(mod => mod.UserManagementModule),
    canActivate: [LocalGuard, AuthGuard, AdminRoleGuard]
  },
];

@NgModule({
  imports: [TranslateModule,RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class LocalAuthRoutingModule {
}
