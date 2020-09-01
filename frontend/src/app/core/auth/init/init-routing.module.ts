import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { InitStateViewComponent } from './init-state-view/init-state-view.component';
import { InitGuard } from './init.guard';

const routes: Routes = [
  { path: '', component: InitStateViewComponent, canActivate: [InitGuard]}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class InitRoutingModule { }
