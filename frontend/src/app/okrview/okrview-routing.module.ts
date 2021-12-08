import {TranslateModule} from '@ngx-translate/core';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CompanyComponent } from './company/company.component';
import { OkrChildUnitComponent } from './okr-child-unit/department/okr-child-unit.component';
import { MainViewComponent } from './main-view/main-view.component';
import { AuthGuard } from '../core/auth/guards/auth.guard';

const routes: Routes = [
  {
    path: '',
    component: MainViewComponent,
    children: [
      {
        path: 'departments/:departmentId',
        component: OkrChildUnitComponent
      },
      {
        path: 'companies/:companyId',
        component: CompanyComponent
      }
    ],
    canActivate: [AuthGuard]
  }
];

@NgModule({
  imports: [TranslateModule,RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class OkrviewRoutingModule { }
