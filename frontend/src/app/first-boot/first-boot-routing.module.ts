import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RegisterAdminComponent } from './register-admin/register-admin.component';
import { AuthGuard } from '../core/auth/guards/auth.guard';

const routes: Routes = [{
  path: '',
  component: RegisterAdminComponent,
  canActivate: [AuthGuard]
}];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class FirstBootRoutingModule {
}
