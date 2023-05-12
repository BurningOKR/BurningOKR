import { Component, Input, OnDestroy, OnInit } from '@angular/core';

import { Subscription } from 'rxjs';
import { filter } from 'rxjs/operators';
import { TaskBoardViewEventService } from '../../../../../taskboard-services/task-board-view-event.service';
import { StateTaskMap } from '../../../../../../shared/model/ui/taskboard/state-task-map';
import { ViewKeyResult } from '../../../../../../shared/model/ui/view-key-result';
import { TaskBoardDragDropEvent } from '../../taskboard-column/taskboard-column.component';

export interface TaskBoardSwimlaneDragDropEvent {
  keyResult: ViewKeyResult;
  taskboardEvent: TaskBoardDragDropEvent;
}

@Component({
  selector: 'app-taskboard-swimlane',
  templateUrl: './taskboard-swimlane.component.html',
  styleUrls: ['./taskboard-swimlane.component.scss'],
})
export class TaskboardSwimlaneComponent implements OnInit, OnDestroy {
  @Input() tasksForStates: StateTaskMap[];
  @Input() keyResult: ViewKeyResult;
  @Input() keyResultList: ViewKeyResult[];
  @Input() isInteractive: boolean;

  panelOpenState = true;
  subscriptions: Subscription[] = [];

  constructor(
    private taskBoardEventService: TaskBoardViewEventService,
  ) {
  }

  ngOnInit(): void {
    this.subscriptions.push(
      this.taskBoardEventService.taskDragAndDropInView$
        .pipe(
          filter(event => {
            let idToCompare: string = null;
            if (this.keyResult && this.keyResult.id) {
              idToCompare = `${this.keyResult.id}`;
            }

            return `${event.$event.container.id}` === `${idToCompare}`;
          }),
        )
        .subscribe(event => {
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
              this.keyResult.viewKeyResultMilestones,
            );
          }

          this.taskBoardEventService.taskInSwimlaneMoved$.next({
            taskboardEvent: event,
            keyResult,
          });
        }),
    );
  }

  ngOnDestroy(): void {
    for (const subscription of this.subscriptions) {
      subscription.unsubscribe();
    }
  }
}
