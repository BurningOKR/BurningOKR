import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { DepartmentTabTaskboardComponent } from './department-tab-taskboard.component';
import { I18n } from '@ngx-translate/i18n-polyfill';
import { Component, CUSTOM_ELEMENTS_SCHEMA, Input, NO_ERRORS_SCHEMA } from '@angular/core';
import { BehaviorSubject, of, Subject } from 'rxjs';
import { MaterialTestingModule } from '../../../../testing/material-testing.module';
import { TaskMapperService } from '../../../../shared/services/mapper/task.mapper';
import { TaskStateMapper } from '../../../../shared/services/mapper/task-state.mapper';
import { TaskBoardGeneralHelper } from '../../../../shared/services/helper/task-board/task-board-general-helper';
import { TaskBoardViewEventService } from '../../../taskboard-services/task-board-view-event.service';
import { KeyResultMapper } from '../../../../shared/services/mapper/key-result.mapper';
import { RxStompService } from '@stomp/ng2-stompjs';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { ReactiveFormsModule } from '@angular/forms';

@Component({
  // tslint:disable-next-line:component-selector
  selector: 'app-taskboard-state-column-view',
  template: ''
})
class TaskBoardStateColumnViewMockComponent {
  @Input() data$ = of();
  @Input() isInteractive = false;
}

@Component({
  // tslint:disable-next-line:component-selector
  selector: 'app-taskboard-swimlane-view',
  template: ''
})
class TaskBoardSwimlaneViewMockComponent {
  @Input() data$ = of();
  @Input() isInteractive = false;
}

const taskMapperServiceMock: any = {};
const taskStateMapperMock: any = {};
const taskBoardGeneralHelperMock: any = {};
const taskBoardViewEventServiceMock: any = {};
const keyResultMapperMock: any = {};
const rxStompServiceMock: any = {
  webSocketErrors$: new Subject<any>(),
  connectionState$: new Subject<any>()
};

const matDialogMock: any = {};

const matSnackBarMock: any = {
  open(params: any, actions: any, config: any): any {
    return of(true);
  }
};

const i18nMock: any = jest.fn();

describe('DepartmentTabTaskboardComponent', () => {
  let component: DepartmentTabTaskboardComponent;
  let fixture: ComponentFixture<DepartmentTabTaskboardComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [MaterialTestingModule, NoopAnimationsModule, MatSnackBarModule, MatDialogModule, ReactiveFormsModule],
      declarations: [
        DepartmentTabTaskboardComponent,
        TaskBoardSwimlaneViewMockComponent,
        TaskBoardStateColumnViewMockComponent,
      ],
      providers: [
        {provide: I18n, useValue: i18nMock},
        {provide: MatDialog, useValue: matDialogMock},
        {provide: MatSnackBar, useValue: matSnackBarMock},
        {provide: TaskMapperService, useValue: taskMapperServiceMock},
        {provide: TaskStateMapper, useValue: taskStateMapperMock},
        {provide: TaskBoardGeneralHelper, useValue: taskBoardGeneralHelperMock},
        {provide: TaskBoardViewEventService, useValue: taskBoardViewEventServiceMock},
        {provide: KeyResultMapper, useValue: keyResultMapperMock},
        {provide: RxStompService, useValue: rxStompServiceMock},
      ]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DepartmentTabTaskboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
