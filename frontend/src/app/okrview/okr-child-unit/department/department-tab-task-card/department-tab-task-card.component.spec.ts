import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { DepartmentTabTaskCardComponent, TaskCardInformation } from './department-tab-task-card.component';
import { Component, Input } from '@angular/core';
import { MaterialTestingModule } from '../../../../testing/material-testing.module';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { SharedModule } from '../../../../shared/shared.module';
import { Observable, of } from 'rxjs';
import { User } from '../../../../shared/model/api/user';
import { UserService } from '../../../../shared/services/helper/user.service';
import { TaskBoardViewEventService } from '../../../taskboard-services/task-board-view-event.service';
import { KeyResultMapper } from '../../../../shared/services/mapper/key-result.mapper';
import { ViewTask } from '../../../../shared/model/ui/taskboard/view-task';
import { ViewKeyResult } from '../../../../shared/model/ui/view-key-result';
import { Unit } from '../../../../shared/model/api/unit.enum';

describe('DepartmentTabTaskCardComponent', () => {
  let component: DepartmentTabTaskCardComponent;
  let fixture: ComponentFixture<DepartmentTabTaskCardComponent>;

  @Component({
    // eslint-disable-next-line @angular-eslint/component-selector
    selector: 'ngx-avatars',
    template: '',
  })
  class NgxAvatarMockComponent {
    @Input() value: any;
    @Input() name: any;
    @Input() size: any;
    @Input() src: any;
  }

  class UserServiceMock {
    getUserById$(userid: any): Observable<User[]> {
      const users: User[] = [
        new User('1', 'Max', 'Mustermann', 'mmustermann@email.com', 'Consultant', 'IT', ''),
        new User('2', 'Bastian', 'Pastewka', 'bpastewka@email.com', 'Consultant', 'IT', ''),
        new User('3', 'Bernhard', 'Hoecker', 'bhoecker@email.com', 'Consultant', 'IT', ''),
      ];

      return of(users);
    }
  }

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [NgxAvatarMockComponent, DepartmentTabTaskCardComponent],
      imports: [MaterialTestingModule, NoopAnimationsModule, SharedModule],
      providers: [
        { provide: UserService, useValue: new UserServiceMock() },
        { provide: TaskBoardViewEventService, useValue: {} },
        { provide: KeyResultMapper, useValue: {} },
      ],
    })
      .compileComponents();
  }));

  beforeEach(() => {
    const task: ViewTask = new ViewTask(1, 'Test', '', ['1'], 1, 1, 1, null, 1);
    const keyResult: ViewKeyResult = new ViewKeyResult(1, 1, 1, 3, Unit.EURO, 'Titel', '', 1, [], []);
    const taskCardInformations: TaskCardInformation = {
      task,
      keyResult,
    };

    fixture = TestBed.createComponent(DepartmentTabTaskCardComponent);
    component = fixture.componentInstance;
    component.taskInformations = taskCardInformations;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
