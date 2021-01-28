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

  subscriptions: Subscription[] = [];

  constructor(
    private taskBoardEventService: TaskBoardViewEventService,
  ) { }

  ngOnInit(): void { }

  ngOnDestroy(): void {
    for (const subscription of this.subscriptions) {
      subscription.unsubscribe();
    }
  }

  createNewTask(): void {
    console.log("Column: create Task Button Click");
    this.taskBoardEventService.taskAddButtonClick$.next(this.map.state);
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


  onDelete(task: ViewTask) {
    this.taskBoardEventService.taskDeleteButtonClick$.next(task);
  }
}
