import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LogoutComponent } from './logout/logout.component';

const routes: Routes = [
  { path: 'logout', component: LogoutComponent },
  // { path: 'init', }
  { path: '', loadChildren: () => import('./local-auth/local-auth.module')
      .then(mod => mod.LocalAuthModule) },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AuthRoutingModule { }
