import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { TaskboardColumnComponent } from './taskboard-column.component';
import { SharedModule } from '../../../../../shared/shared.module';
import { MaterialTestingModule } from '../../../../../testing/material-testing.module';
import { Component, Input } from '@angular/core';
import { ViewTaskState } from '../../../../../shared/model/ui/taskboard/view-task-state';
import { CdkDropList, DragDropModule } from '@angular/cdk/drag-drop';

describe('TaskboardColumnComponent', () => {
  let component: TaskboardColumnComponent;
  let fixture: ComponentFixture<TaskboardColumnComponent>;

  @Component({
    selector: 'app-department-tab-task-card',
    template: '',
  })
  class TasKCardMockComponent {
    @Input() taskInformations: any;
    @Input() isInteractive: boolean;
  }

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [TaskboardColumnComponent, TasKCardMockComponent],
      imports: [SharedModule, MaterialTestingModule, DragDropModule],
      providers: [
        CdkDropList,
      ],
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TaskboardColumnComponent);
    component = fixture.componentInstance;
    component.map = { state: new ViewTaskState(1, 'title'), tasks: [] };
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
