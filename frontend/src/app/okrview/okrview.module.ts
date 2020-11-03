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
import { OkrChildUnitPreviewButtonComponent } from './okr-child-unit/okr-child-unit-preview-button/okr-child-unit-preview-button.component';
import { OkrChildUnitOverviewTabComponent } from './okr-child-unit/okr-child-unit-tab-overview/okr-child-unit-overview-tab.component';
import { OkrChildUnitTabComponent } from './okr-child-unit/okr-child-unit-tab/okr-child-unit-tab.component';
import { DepartmentTabTeamComponent } from './okr-child-unit/department/department-tab-team/department-tab-team.component';
import { DepartmentTeamNewUserComponent } from './okr-child-unit/department/department-team-new-user/department-team-new-user.component';
import { OkrChildUnitComponent } from './okr-child-unit/department/okr-child-unit.component';
import { KeyresultComponent } from './keyresult/keyresult.component';
import { MainViewComponent } from './main-view/main-view.component';
import { NavigationSidebarComponent } from './navigation-sidebar/navigation-sidebar.component';
import { ObjectiveContentsComponent } from './objective/objective-contents/objective-contents.component';
import { ObjectiveComponent } from './objective/objective.component';
import { OkrviewRoutingModule } from './okrview-routing.module';
import { NavigationListEntryComponent } from './navigation-list-entry/navigation-list-entry.component';
import { ObjectiveFormComponent } from './objective/objective-form/objective-form.component';
import { OkrChildUnitFormComponent } from './okr-child-unit/okr-child-unit-form/okr-child-unit-form.component';
import { KeyResultFormComponent } from './keyresult/key-result-form/key-result-form.component';
import { MatCheckboxModule, MatSlideToggleModule } from '@angular/material';
import { AvatarModule } from 'ngx-avatar';
import { KeyResultMilestoneFormComponent } from './keyresult/key-result-form/key-result-milestone-form/key-result-milestone-form.component';
import { ScrollingModule } from '@angular/cdk/scrolling';
import { AngularResizedEventModule } from 'angular-resize-event';
import { DepartmentTabTaskboardComponent } from './okr-child-unit/department/department-tab-taskboard/department-tab-taskboard.component';
import { DepartmentTabTaskCardComponent } from './okr-child-unit/department/department-tab-task-card/department-tab-task-card.component';

@NgModule({
  declarations: [
    KeyresultComponent,
    ObjectiveFormComponent,
    OkrChildUnitFormComponent,
    KeyResultFormComponent,
    OkrChildUnitComponent,
    OkrChildUnitOverviewTabComponent,
    DepartmentTabTeamComponent,
    OkrChildUnitTabComponent,
    OkrChildUnitPreviewButtonComponent,
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
    KeyResultMilestoneFormComponent,
    DepartmentTabTaskboardComponent,
    DepartmentTabTaskCardComponent,
  ],
  entryComponents: [
    CommentViewDialogComponent,
    OkrChildUnitFormComponent,
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
        AvatarModule,
        MatCheckboxModule,
        ScrollingModule,
        AngularResizedEventModule,
    ]
})
export class OkrviewModule {}
