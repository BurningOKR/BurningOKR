import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TaskboardColumnComponent } from './taskboard-column.component';
import { SharedModule } from '../../../../../shared/shared.module';
import { MaterialTestingModule } from '../../../../../testing/material-testing.module';
import { CdkDropList, DragDropModule } from '@angular/cdk/drag-drop';
import { CdkDropListContainer } from '@angular/cdk/drag-drop/typings/drop-list-container';
import { Component, Input, OnChanges, OnDestroy, OnInit } from '@angular/core';
import { OkrChildUnit } from '../../../../../shared/model/ui/OrganizationalUnit/okr-child-unit';
import { ContextRole } from '../../../../../shared/model/ui/context-role';
import { CycleUnit } from '../../../../../shared/model/ui/cycle-unit';
import { StateTaskMap } from '../../../../../shared/model/ui/taskboard/state-task-map';
import { ViewKeyResult } from '../../../../../shared/model/ui/view-key-result';
import { ViewTaskState } from '../../../../../shared/model/ui/taskboard/view-task-state';

describe('TaskboardColumnComponent', () => {
  let component: TaskboardColumnComponent;
  let fixture: ComponentFixture<TaskboardColumnComponent>;

  @Component({
    selector: 'app-department-tab-task-card',
    template: ''
  })
  class TasKCardMockComponent {
    @Input() taskInformations: any;
    @Input() isInteractive: boolean;
  }

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [TaskboardColumnComponent, TasKCardMockComponent],
      imports: [SharedModule, MaterialTestingModule, DragDropModule],
      providers: [
        CdkDropList,
      ]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TaskboardColumnComponent);
    component = fixture.componentInstance;
    component.map = {state: new ViewTaskState(1, 'title'), tasks: []};
    component.id = 1;
    component.keyResults = [];
    component.isInteractive = false;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
