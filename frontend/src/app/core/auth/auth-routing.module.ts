/* eslint-disable @typescript-eslint/promise-function-async */
import { TranslateModule } from '@ngx-translate/core';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LogoutComponent } from './logout/logout.component';

const routes: Routes = [
  { path: 'logout', component: LogoutComponent },
];

@NgModule({
  imports: [TranslateModule, RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class AuthRoutingModule {
}
