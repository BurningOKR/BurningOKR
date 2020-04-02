import { DragDropModule } from '@angular/cdk/drag-drop';
import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatBadgeModule } from '@angular/material/badge';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatDialogModule } from '@angular/material/dialog';
import { MatDividerModule } from '@angular/material/divider';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatMenuModule } from '@angular/material/menu';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSelectModule } from '@angular/material/select';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatSliderModule } from '@angular/material/slider';
import { MatTabsModule } from '@angular/material/tabs';
import { MatTooltipModule } from '@angular/material/tooltip';
import { SharedModule } from '../shared/shared.module';
import { CommentCardComponent } from './comment/comment-card/comment-card.component';
import { CommentViewDialogComponent } from './comment/comment-view-dialog/comment-view-dialog.component';
import { CompanyComponent } from './company/company.component';
import { CycleListDropdownComponent } from './cycle-list-dropdown/cycle-list-dropdown.component';
import { SubstructurePreviewButtonComponent } from './substructure/substructure-preview-button/substructure-preview-button.component';
import { SubstructureOverviewTabComponent } from './substructure/substructure-tab-overview/substructure-overview-tab.component';
import { SubStructuresTabComponent } from './substructure/substructures-tab/sub-structures-tab.component';
import { DepartmentTabTeamComponent } from './substructure/department/department-tab-team/department-tab-team.component';
import { DepartmentTeamNewUserComponent } from './substructure/department/department-team-new-user/department-team-new-user.component';
import { DepartmentComponent } from './substructure/department/department.component';
import { KeyresultComponent } from './keyresult/keyresult.component';
import { MainViewComponent } from './main-view/main-view.component';
import { NavigationSidebarComponent } from './navigation-sidebar/navigation-sidebar.component';
import { ObjectiveContentsComponent } from './objective/objective-contents/objective-contents.component';
import { ObjectiveComponent } from './objective/objective.component';
import { OkrviewRoutingModule } from './okrview-routing.module';
import { NavigationListEntryComponent } from './navigation-list-entry/navigation-list-entry.component';
import { ObjectiveFormComponent } from './objective/objective-form/objective-form.component';
import { SubstructureFormComponent } from './substructure/substructure-form/substructure-form.component';
import { KeyResultFormComponent } from './keyresult/key-result-form/key-result-form.component';
import { MatSlideToggleModule } from '@angular/material';
import { CorporateObjectiveStructureComponent } from './substructure/corporate-objective-structure/corporate-objective-structure.component';
import { AvatarModule } from 'ngx-avatar';

@NgModule({
  declarations: [
    KeyresultComponent,
    ObjectiveFormComponent,
    SubstructureFormComponent,
    KeyResultFormComponent,
    DepartmentComponent,
    SubstructureOverviewTabComponent,
    DepartmentTabTeamComponent,
    SubStructuresTabComponent,
    SubstructurePreviewButtonComponent,
    ObjectiveContentsComponent,
    ObjectiveComponent,
    DepartmentTeamNewUserComponent,
    KeyresultComponent,
    MainViewComponent,
    CompanyComponent,
    NavigationSidebarComponent,
    NavigationListEntryComponent,
    CommentViewDialogComponent,
    CommentCardComponent,
    CycleListDropdownComponent,
    CorporateObjectiveStructureComponent
  ],
  entryComponents: [
    CommentViewDialogComponent,
    SubstructureFormComponent,
    KeyResultFormComponent,
    ObjectiveFormComponent,
    ],
  imports: [
    CommonModule,
    SharedModule,
    OkrviewRoutingModule,
    MatCardModule,
    MatTabsModule,
    MatButtonModule,
    MatProgressSpinnerModule,
    MatProgressBarModule,
    MatIconModule,
    MatTooltipModule,
    MatDividerModule,
    MatMenuModule,
    MatExpansionModule,
    MatBadgeModule,
    MatSliderModule,
    MatSidenavModule,
    MatDialogModule,
    MatInputModule,
    MatSelectModule,
    FormsModule,
    DragDropModule,
    ReactiveFormsModule,
    MatSlideToggleModule,
    AvatarModule
  ]
})
export class OkrviewModule {}
