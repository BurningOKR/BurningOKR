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
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { AvatarModule } from 'ngx-avatar';
import { KeyResultMilestoneFormComponent } from './keyresult/key-result-form/key-result-milestone-form/key-result-milestone-form.component';
import { ScrollingModule } from '@angular/cdk/scrolling';
import { AngularResizedEventModule } from 'angular-resize-event';
import { DepartmentTabDescriptionComponent } from './okr-child-unit/department/department-tab-description/department-tab-description.component';
import { DepartmentDescriptionEditFormComponent } from './okr-child-unit/department/department-tab-description/department-description-edit-form/department-description-edit-form.component';
import { DepartmentTabTaskboardComponent } from './okr-child-unit/department/department-tab-taskboard/department-tab-taskboard.component';
import { DepartmentTabTaskCardComponent } from './okr-child-unit/department/department-tab-task-card/department-tab-task-card.component';
import { TaskFormComponent } from './okr-child-unit/department/department-tab-task-form/department-tab-task-form.component';
import { TaskboardColumnComponent } from './okr-child-unit/department/department-tab-taskboard/taskboard-column/taskboard-column.component';
import { InjectableRxStompConfig, RxStompService, rxStompServiceFactory } from '@stomp/ng2-stompjs';
import { myRxStompConfig } from './websockets/rx-stomp-config';
import { TaskboardSwimlaneViewComponent } from './okr-child-unit/department/department-tab-taskboard/taskboard-swimlane-view/taskboard-swimlane-view.component';
import { TopicDraftCreationFormComponent } from './okr-child-unit/okr-child-unit-form/topic-draft-creation-form/topic-draft-creation-form.component';
import { OkrTopicDescriptionFormComponent } from './okr-topic-description-form/okr-topic-description-form.component';
import { AddChildUnitButtonComponent } from './add-child-unit-button/add-child-unit-button.component';

import { TaskboardStateColumnViewComponent } from './okr-child-unit/department/department-tab-taskboard/taskboard-state-column-view/taskboard-state-column-view.component';
import { TaskBoardGeneralHelper } from '../shared/services/helper/task-board/task-board-general-helper';
import { TaskBoardStateColumnViewHelper } from '../shared/services/helper/task-board/task-board-state-column-view-helper';
import { TaskBoardSwimlaneViewHelper } from '../shared/services/helper/task-board/task-board-swimlane-view-helper';
import { TaskboardSwimlaneComponent } from './okr-child-unit/department/department-tab-taskboard/taskboard-swimlane-view/taskboard-swimlane/taskboard-swimlane.component';
import { NgxMatSelectSearchModule } from 'ngx-mat-select-search';

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
        DepartmentTabDescriptionComponent,
        DepartmentDescriptionEditFormComponent,
        DepartmentTabTaskboardComponent,
        DepartmentTabTaskCardComponent,
        TaskFormComponent,
        TaskboardColumnComponent,
        TaskboardSwimlaneViewComponent,
        TaskboardSwimlaneComponent,
        TaskboardStateColumnViewComponent,
        TopicDraftCreationFormComponent,
        OkrTopicDescriptionFormComponent,
        AddChildUnitButtonComponent,
    ],
    entryComponents: [
        CommentViewDialogComponent,
        OkrChildUnitFormComponent,
        KeyResultFormComponent,
        ObjectiveFormComponent,
        DepartmentDescriptionEditFormComponent,
        TaskFormComponent,
        TopicDraftCreationFormComponent,
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
        MatDatepickerModule,
        MatButtonToggleModule,
        NgxMatSelectSearchModule,
    ],
    exports: [
        OkrTopicDescriptionFormComponent
    ],
    providers: [
        {
            provide: InjectableRxStompConfig,
            useValue: myRxStompConfig
        },
        {
            provide: RxStompService,
            useFactory: rxStompServiceFactory,
            deps: [InjectableRxStompConfig]
        },
        TaskBoardGeneralHelper,
        TaskBoardStateColumnViewHelper,
        TaskBoardSwimlaneViewHelper
    ]
})
export class OkrviewModule {
}
