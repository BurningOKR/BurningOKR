import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TaskboardStateColumnViewComponent } from './taskboard-state-column-view.component';
import { TaskboardColumnComponent } from '../taskboard-column/taskboard-column.component';
import { Component, Input } from '@angular/core';
import { of } from 'rxjs';
import { ViewKeyResult } from '../../../../../shared/model/ui/view-key-result';
import { StateTaskMap } from '../../../../../shared/model/ui/taskboard/state-task-map';
import { TaskBoardStateColumnViewHelper } from '../../../../../shared/services/helper/task-board/task-board-state-column-view-helper';
import { I18n } from '@ngx-translate/i18n-polyfill';

describe('TaskboardStateColumnViewComponent', () => {
  let component: TaskboardStateColumnViewComponent;
  let fixture: ComponentFixture<TaskboardStateColumnViewComponent>;

  @Component({
    // tslint:disable-next-line:component-selector
    selector: 'app-taskboard-column',
    template: ''
  })
  class TaskBoardStateColumnMockComponent {
    @Input() map: StateTaskMap[];
    @Input() isInteractive: boolean;
    @Input() keyResults: ViewKeyResult[];
  }
  const i18nMock: any = jest.fn();

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TaskboardStateColumnViewComponent, TaskBoardStateColumnMockComponent ],
      providers: [
        {provide: TaskBoardStateColumnViewHelper},
        {provide: I18n, useValue: i18nMock},
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
