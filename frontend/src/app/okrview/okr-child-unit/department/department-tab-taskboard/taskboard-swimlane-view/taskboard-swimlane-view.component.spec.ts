import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { TaskboardSwimlaneViewComponent } from './taskboard-swimlane-view.component';
import { Component, Input } from '@angular/core';
import { StateTaskMap } from '../../../../../shared/model/ui/taskboard/state-task-map';
import { ViewKeyResult } from '../../../../../shared/model/ui/view-key-result';
import { SharedModule } from '../../../../../shared/shared.module';
import { MaterialTestingModule } from '../../../../../testing/material-testing.module';
import { TaskBoardSwimlaneViewHelper } from '../../../../../shared/services/helper/task-board/task-board-swimlane-view-helper';
import { TaskBoardViewEventService } from '../../../../taskboard-services/task-board-view-event.service';
import { of } from 'rxjs';

describe('TaskboardSwimlaneViewComponent', () => {
  let component: TaskboardSwimlaneViewComponent;
  let fixture: ComponentFixture<TaskboardSwimlaneViewComponent>;

  @Component({
    selector: 'app-taskboard-swimlane',
    template: ''
  })
  class TaskBoardSwimlaneMockComponent {
    @Input() tasksForStates: StateTaskMap[];
    @Input() keyResult: ViewKeyResult;
    @Input() keyResultList: ViewKeyResult[];
    @Input() isInteractive: boolean;
  }

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      imports: [SharedModule, MaterialTestingModule],
      declarations: [ TaskboardSwimlaneViewComponent, TaskBoardSwimlaneMockComponent ],
      providers: [
        TaskBoardSwimlaneViewHelper,
        TaskBoardViewEventService
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TaskboardSwimlaneViewComponent);
    component = fixture.componentInstance;
    component.data$ = of();
    component.isInteractive = false;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
