import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { TaskFormComponent, TaskFormData } from './department-tab-task-form.component';
import { ReactiveFormsModule } from '@angular/forms';
import { MaterialTestingModule } from '../../../../testing/material-testing.module';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { MAT_DIALOG_DATA, MatDialog, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { ViewObjective } from '../../../../shared/model/ui/view-objective';
import { DialogComponent } from '../../../../shared/components/dialog-component/dialog.component';
import { ViewTaskState } from '../../../../shared/model/ui/taskboard/view-task-state';
import { ObjectiveViewMapper } from '../../../../shared/services/mapper/objective-view.mapper';
import { UserService } from '../../../../shared/services/helper/user.service';
import { User } from '../../../../shared/model/api/user';
import { Observable, of } from 'rxjs';
import { Component, Input } from '@angular/core';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient } from '@angular/common/http';
import { TaskId } from '../../../../shared/model/id-types';
import { RevisionDto } from '../../../../shared/model/api/revision-dto';
import { AuthenticationService } from '../../../../core/auth/services/authentication.service';
import { LoggerConfig, NGXLogger, NGXLoggerHttpService } from 'ngx-logger';
import { NGXLoggerMock } from 'ngx-logger/testing';

describe('DepartmentTabTaskFormComponent', () => {
  let component: TaskFormComponent;
  let fixture: ComponentFixture<TaskFormComponent>;

  @Component({
    selector: 'app-form-error',
    template: '',
  })
  class ErrorMockComponent {
    @Input() control;
  }

  class UserServiceMock {
    getAllUsers$(): Observable<User> {
      return of();
    }
  }

  class ObjectiveViewMapperMock {
    getObjectivesForUnit$(): Observable<ViewObjective> {
      return of();
    }
  }

  class AuthenticationServiceMock {
    getRevisionsForTask$(taskId: TaskId): Observable<RevisionDto[]> {
      return of([]);
    }
  }

  const matDialogDataMock: TaskFormData = {
    task: null,
    unitId: 1,
    defaultState: new ViewTaskState(1, 'TODO'),
    states: [new ViewTaskState(1, 'TODO')],
    keyResults: [],
    isInteractive: true,
  };

  let httpClient: HttpClient;
  let httpTestingController: HttpTestingController;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [TaskFormComponent, DialogComponent, ErrorMockComponent],
      imports: [NoopAnimationsModule, MatDialogModule, ReactiveFormsModule, MaterialTestingModule, HttpClientTestingModule],
      providers: [
        { provide: MAT_DIALOG_DATA, useValue: matDialogDataMock },
        { provide: MatDialogRef, useValue: {} },
        { provide: ObjectiveViewMapper, useValue: new ObjectiveViewMapperMock() },
        { provide: UserService, useValue: new UserServiceMock() },
        { provide: AuthenticationService, useValue: new AuthenticationServiceMock() },
        { provide: NGXLogger, useClass: NGXLoggerMock },
        {
          provide: MatDialog, useClass: class {
          },
        },
        {
          provide: NGXLoggerHttpService, useClass: class {
          },
        },
        {
          provide: LoggerConfig, useClass: class {
          },
        },
      ],
    })
      .compileComponents();

    httpClient = TestBed.inject(HttpClient);
    httpTestingController = TestBed.inject(HttpTestingController);
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TaskFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
