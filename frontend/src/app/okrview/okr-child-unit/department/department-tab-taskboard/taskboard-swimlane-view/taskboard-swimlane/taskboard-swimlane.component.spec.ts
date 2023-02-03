import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { TaskboardSwimlaneComponent } from './taskboard-swimlane.component';
import { MaterialTestingModule } from '../../../../../../testing/material-testing.module';
import { Component, Input } from '@angular/core';
import { StateTaskMap } from '../../../../../../shared/model/ui/taskboard/state-task-map';
import { ViewKeyResult } from '../../../../../../shared/model/ui/view-key-result';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { Unit } from '../../../../../../shared/model/api/unit.enum';

describe('TaskboardSwimlaneComponent', () => {
  let component: TaskboardSwimlaneComponent;
  let fixture: ComponentFixture<TaskboardSwimlaneComponent>;

  @Component({
    selector: 'app-taskboard-column',
    template: '',
  })
  class TaskBoardColumnMockComponent {
    @Input() map: StateTaskMap;
    @Input() id: number;
    @Input() keyResults: ViewKeyResult[];
    @Input() isInteractive: boolean;
  }

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      imports: [MaterialTestingModule, NoopAnimationsModule],
      declarations: [TaskboardSwimlaneComponent, TaskBoardColumnMockComponent],
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TaskboardSwimlaneComponent);
    component = fixture.componentInstance;
    component.tasksForStates = [];
    component.keyResult = new ViewKeyResult(1, 1, 1, 2, Unit.NUMBER, 'Title', '', 1, [], []);
    component.keyResultList = [component.keyResult];
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
