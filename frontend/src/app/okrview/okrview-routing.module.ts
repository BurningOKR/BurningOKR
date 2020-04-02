import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CompanyComponent } from './company/company.component';
import { DepartmentComponent } from './substructure/department/department.component';
import { MainViewComponent } from './main-view/main-view.component';
import { AuthGuard } from '../core/auth/guards/auth.guard';

const routes: Routes = [
  {
    path: ``,
    component: MainViewComponent,
    children: [
      {
        path: `departments/:departmentId`,
        component: DepartmentComponent
      },
      {
        path: `companies/:companyId`,
        component: CompanyComponent
      }
    ],
    canActivate: [AuthGuard]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class OkrviewRoutingModule { }
