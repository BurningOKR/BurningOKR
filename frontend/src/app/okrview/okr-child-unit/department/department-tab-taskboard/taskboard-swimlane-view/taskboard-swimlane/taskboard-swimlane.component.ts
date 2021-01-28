import { Component, OnInit, Input, OnDestroy, Output, EventEmitter } from "@angular/core";

import { Subscription } from "rxjs";
import { filter } from "rxjs/operators";
import { TaskBoardViewEventService } from "src/app/okrview/taskboard-services/task-board-view-event.service";
import { KeyResultId } from "src/app/shared/model/id-types";
import { StateTaskMap } from "src/app/shared/model/ui/taskboard/state-task-map";
import { ViewTask } from "src/app/shared/model/ui/taskboard/view-task";
import { ViewKeyResult } from "src/app/shared/model/ui/view-key-result";
import { TaskBoardDragDropEvent } from "../../taskboard-column/taskboard-column.component";

export interface TaskBoardSwimlaneDragDropEvent {
  keyResult: ViewKeyResult,
  taskboardEvent: TaskBoardDragDropEvent
}

@Component({
  selector: 'app-taskboard-swimlane',
  templateUrl: './taskboard-swimlane.component.html',
  styleUrls: ['./taskboard-swimlane.component.css']
})
export class TaskboardSwimlaneComponent implements OnInit, OnDestroy {
  panelOpenState = true;
  subscriptions: Subscription[] = [];

  @Input() public tasksForStates: StateTaskMap[];
  @Input() public keyResult: ViewKeyResult;

  constructor(
    private taskBoardEventService: TaskBoardViewEventService
  ) { }


  ngOnInit() {
    this.subscriptions.push(
      this.taskBoardEventService.taskDragAndDropInView$
        .pipe(
          filter(event => {
            console.log('TaskboardSwimlaneComponent filter id: ' + this.keyResult.id + ' container id: '+event.$event.container.id);
            console.log(event);
            let idToCompare: string = null;
            if (this.keyResult && this.keyResult.id) {
              idToCompare = `${this.keyResult.id}`;
            }
            console.log(''+ event.$event.container.id=== idToCompare);
            return ''+event.$event.container.id === ''+idToCompare;
          })
        )
        .subscribe(event => {
          console.log('TaskboardSwimlaneComponent subscribe id: ' + this.keyResult.id);
          let keyResult: ViewKeyResult = null;

          if (this.keyResult) {
            keyResult = new ViewKeyResult(
              this.keyResult.id,
              this.keyResult.start,
              this.keyResult.current,
              this.keyResult.end,
              this.keyResult.unit,
              this.keyResult.keyResult,
              this.keyResult.description,
              this.keyResult.parentObjectiveId,
              this.keyResult.commentIdList,
              this.keyResult.viewKeyResultMilestones);
          }

          this.taskBoardEventService.taskInSwimlaneMoved$.next({
            taskboardEvent: event,
            keyResult
          });
        })
    );
  }

  createNewTask() { }

  ngOnDestroy(): void {
    for (const subscription of this.subscriptions) {
      subscription.unsubscribe();
    }
  }
}
