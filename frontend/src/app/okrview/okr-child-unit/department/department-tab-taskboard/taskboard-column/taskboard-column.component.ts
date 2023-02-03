import { CdkDragDrop } from '@angular/cdk/drag-drop';
import { Component, Input, OnDestroy, OnInit } from '@angular/core';

import { Subscription } from 'rxjs';

import { TaskCardInformation } from '../../department-tab-task-card/department-tab-task-card.component';
import { ViewTaskState } from '../../../../../shared/model/ui/taskboard/view-task-state';
import { ViewTask } from '../../../../../shared/model/ui/taskboard/view-task';
import { StateTaskMap } from '../../../../../shared/model/ui/taskboard/state-task-map';
import { ViewKeyResult } from '../../../../../shared/model/ui/view-key-result';
import { TaskBoardViewEventService } from '../../../../taskboard-services/task-board-view-event.service';

export interface TaskBoardDragDropEvent {
  state: ViewTaskState;
  $event: CdkDragDrop<ViewTask[]>;
}

@Component({
  selector: 'app-taskboard-column',
  templateUrl: './taskboard-column.component.html',
  styleUrls: ['./taskboard-column.component.scss'],
})
export class TaskboardColumnComponent implements OnInit, OnDestroy {
  @Input() map: StateTaskMap;
  @Input() id: number;
  @Input() keyResults: ViewKeyResult[];
  @Input() isInteractive: boolean;

  taskCardInformations: TaskCardInformation[] = [];
  subscriptions: Subscription[] = [];

  constructor(
    private taskBoardEventService: TaskBoardViewEventService,
  ) {
  }

  ngOnInit(): void {
    for (const task of this.map.tasks) {
      this.taskCardInformations.push(this.createTaskInformation(task, this.keyResults));
    }
  }

  ngOnDestroy(): void {
    for (const subscription of this.subscriptions) {
      subscription.unsubscribe();
    }
  }

  dropTask($event: CdkDragDrop<ViewTask[]>): void {
    const dropEvent: TaskBoardDragDropEvent = {
      $event,
      state: this.map.state,
    };
    this.taskBoardEventService.taskDragAndDropInView$.next(dropEvent);
  }

  createTaskInformation(task: ViewTask, availableKeyResults: ViewKeyResult[]): TaskCardInformation {
    return { keyResult: availableKeyResults.find(keyResult => keyResult.id === task.assignedKeyResultId), task };
  }
}
