import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CompanyComponent } from './company/company.component';
import { SubStructureComponent } from './substructure/department/sub-structure.component';
import { MainViewComponent } from './main-view/main-view.component';
import { AuthGuard } from '../core/auth/guards/auth.guard';

const routes: Routes = [
  {
    path: ``,
    component: MainViewComponent,
    children: [
      {
        path: `departments/:departmentId`,
        component: SubStructureComponent
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
