import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LogoutComponent } from './logout/logout.component';
import { LocalGuard } from './guards/local.guard';

const routes: Routes = [
  { path: 'logout', component: LogoutComponent },
  {
    path: 'init', loadChildren: () => import('./init/init.module')
      .then(mod => mod.InitModule)
  },
  {
    path: '', loadChildren: () => import('./local-auth/local-auth.module')
      .then(mod => mod.LocalAuthModule),
    canActivate: [LocalGuard]
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AuthRoutingModule {
}
