import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { TaskboardStateColumnViewComponent } from './taskboard-state-column-view.component';
import { Component, Input } from '@angular/core';
import { of } from 'rxjs';
import { ViewKeyResult } from '../../../../../shared/model/ui/view-key-result';
import { StateTaskMap } from '../../../../../shared/model/ui/taskboard/state-task-map';
import { TaskBoardStateColumnViewHelper } from '../../../../../shared/services/helper/task-board/task-board-state-column-view-helper';
import { MaterialTestingModule } from '../../../../../testing/material-testing.module';

describe('TaskboardStateColumnViewComponent', () => {
  let component: TaskboardStateColumnViewComponent;
  let fixture: ComponentFixture<TaskboardStateColumnViewComponent>;

  @Component({
    // eslint-disable-next-line @angular-eslint/component-selector
    selector: 'app-taskboard-column',
    template: ''
  })
  class TaskBoardStateColumnMockComponent {
    @Input() map: StateTaskMap[];
    @Input() isInteractive: boolean;
    @Input() keyResults: ViewKeyResult[];
  }

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ TaskboardStateColumnViewComponent, TaskBoardStateColumnMockComponent ],
      imports: [MaterialTestingModule],
      providers: [
        {provide: TaskBoardStateColumnViewHelper},
        ]
    })
    .compileComponents();
  }));

  beforeEach(() => {

    fixture = TestBed.createComponent(TaskboardStateColumnViewComponent);
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
