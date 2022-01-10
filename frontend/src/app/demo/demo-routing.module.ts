import {TranslateModule} from '@ngx-translate/core';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DemoMainViewComponent } from './demo-main-view/demo-main-view.component';
import { DemoHomeComponent } from './demo-home/demo-home.component';
import { DemoPrivacyPolicyComponent } from './demo-privacy-policy/demo-privacy-policy.component';
import { DemoImprintComponent } from './demo-imprint/demo-imprint.component';
import { DemoCreditsComponent } from './demo-credits/demo-credits.component';

const routes: Routes = [
  {
    path: '', component: DemoMainViewComponent, children: [
      { path: '', component: DemoHomeComponent },
      { path: 'privacy-policy', component: DemoPrivacyPolicyComponent },
      { path: 'imprint', component: DemoImprintComponent },
      { path: 'credits', component: DemoCreditsComponent }
    ]
  }
];

@NgModule({
  imports: [TranslateModule,RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DemoRoutingModule {
}
