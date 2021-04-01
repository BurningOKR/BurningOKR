import { CdkDragDrop } from "@angular/cdk/drag-drop";
import { Component, OnInit, OnDestroy, Input, Output, EventEmitter } from "@angular/core";

import { Subscription } from "rxjs";
import { TaskBoardViewEventService } from "src/app/okrview/taskboard-services/task-board-view-event.service";
import { TaskStateId } from "src/app/shared/model/id-types";
import { StateTaskMap } from "src/app/shared/model/ui/taskboard/state-task-map";
import { ViewTask } from "src/app/shared/model/ui/taskboard/view-task";
import { ViewTaskState } from "src/app/shared/model/ui/taskboard/view-task-state";
import { ViewKeyResult } from "src/app/shared/model/ui/view-key-result";
import { TaskBoardStateColumnViewHelper } from "src/app/shared/services/helper/task-board/task-board-state-column-view-helper";
import { TaskCardInformation } from "../../department-tab-task-card/department-tab-task-card.component";



export interface MovedTaskUpdater {
  updateTaskListOnNewPosition();
}

export interface TaskBoardDragDropEvent {
  state: ViewTaskState;
  $event: CdkDragDrop<ViewTask[]>;
}

@Component({
  selector: 'app-taskboard-column',
  templateUrl: './taskboard-column.component.html',
  styleUrls: ['./taskboard-column.component.css']
})
export class TaskboardColumnComponent implements OnInit, OnDestroy {
  @Input() public map: StateTaskMap;
  @Input() public id: number;
  @Input() keyResults: ViewKeyResult[];
  @Input() isInteractive: boolean;

  taskCardInformations: TaskCardInformation[] = [];
  subscriptions: Subscription[] = [];

  constructor(
    private taskBoardEventService: TaskBoardViewEventService,
  ) { }

  ngOnInit(): void {
    for (let task of this.map.tasks) {
      this.taskCardInformations.push(this.createTaskInformation(task, this.keyResults));
    }
  }

  ngOnDestroy(): void {
    for (const subscription of this.subscriptions) {
      subscription.unsubscribe();
    }
  }

  updateOrViewTask(task: ViewTask): void {
    this.taskBoardEventService.taskUpdateButtonClick$.next(task);
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

  onDelete(task: ViewTask): void {
    this.taskBoardEventService.taskDeleteButtonClick$.next(task);
  }
}
