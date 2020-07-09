import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DemoMainViewComponent } from './demo-main-view/demo-main-view.component';
import { DemoHomeComponent } from './demo-home/demo-home.component';

const routes: Routes = [
  {
    path: '', component: DemoMainViewComponent, children: [
      { path: '', component: DemoHomeComponent }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DemoRoutingModule {
}
