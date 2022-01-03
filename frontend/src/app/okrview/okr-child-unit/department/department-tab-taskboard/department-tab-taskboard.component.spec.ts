import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { DepartmentTabTaskboardComponent } from './department-tab-taskboard.component';
import { Component, Input } from '@angular/core';
import { of, Subject } from 'rxjs';
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
  // eslint-disable-next-line @angular-eslint/component-selector
  selector: 'app-taskboard-state-column-view',
  template: ''
})
class TaskBoardStateColumnViewMockComponent {
  @Input() data$ = of();
  @Input() isInteractive = false;
}

@Component({
  // eslint-disable-next-line @angular-eslint/component-selector
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


describe('DepartmentTabTaskboardComponent', () => {
  let component: DepartmentTabTaskboardComponent;
  let fixture: ComponentFixture<DepartmentTabTaskboardComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      imports: [MaterialTestingModule, NoopAnimationsModule, MatSnackBarModule, MatDialogModule, ReactiveFormsModule],
      declarations: [
        DepartmentTabTaskboardComponent,
        TaskBoardSwimlaneViewMockComponent,
        TaskBoardStateColumnViewMockComponent,
      ],
      providers: [
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
