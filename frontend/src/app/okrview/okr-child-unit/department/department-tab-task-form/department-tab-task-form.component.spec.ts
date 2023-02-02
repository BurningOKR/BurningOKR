import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { TaskFormComponent, TaskFormData } from './department-tab-task-form.component';
import { ReactiveFormsModule } from '@angular/forms';
import { MaterialTestingModule } from '../../../../testing/material-testing.module';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { ViewObjective } from '../../../../shared/model/ui/view-objective';
import { DialogComponent } from '../../../../shared/components/dialog-component/dialog.component';
import { ViewTaskState } from '../../../../shared/model/ui/taskboard/view-task-state';
import { ObjectiveViewMapper } from '../../../../shared/services/mapper/objective-view.mapper';
import { UserService } from '../../../../shared/services/helper/user.service';
import { User } from '../../../../shared/model/api/user';
import { Observable, of } from 'rxjs';
import { Component, Input } from '@angular/core';

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

  const matDialogDataMock: TaskFormData = {
    task: null,
    unitId: 1,
    defaultState: new ViewTaskState(1, 'TODO'),
    states: [new ViewTaskState(1, 'TODO')],
    keyResults: [],
    isInteractive: true,
  };

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [TaskFormComponent, DialogComponent, ErrorMockComponent],
      imports: [NoopAnimationsModule, MatDialogModule, ReactiveFormsModule, MaterialTestingModule],
      providers: [
        { provide: MAT_DIALOG_DATA, useValue: matDialogDataMock },
        { provide: MatDialogRef, useValue: {} },
        { provide: ObjectiveViewMapper, useValue: new ObjectiveViewMapperMock() },
        { provide: UserService, useValue: new UserServiceMock() },

      ],
    })
      .compileComponents();
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
