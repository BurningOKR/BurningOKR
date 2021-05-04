import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subject, Observable, Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { TaskBoardViewEventService } from '../../../../taskboard-services/task-board-view-event.service';
import { KeyResultStateTaskMap } from '../../../../../shared/model/ui/taskboard/key-result-state-task-map';
import { ViewTask } from '../../../../../shared/model/ui/taskboard/view-task';
import { ViewTaskState } from '../../../../../shared/model/ui/taskboard/view-task-state';
import { ViewKeyResult } from '../../../../../shared/model/ui/view-key-result';
import { TaskBoardSwimlaneViewHelper } from '../../../../../shared/services/helper/task-board/task-board-swimlane-view-helper';
import { TaskBoardView } from '../task-board-view-modell';
import { TaskBoardSwimlaneDragDropEvent } from './taskboard-swimlane/taskboard-swimlane.component';

export interface TaskBoardSwimlaneViewData {
  states: ViewTaskState[];
  maps: KeyResultStateTaskMap[];
  keyResults: ViewKeyResult[];
}

@Component({
  selector: 'app-taskboard-swimlane-view',
  templateUrl: './taskboard-swimlane-view.component.html',
  styleUrls: ['./taskboard-swimlane-view.component.css']
})
export class TaskboardSwimlaneViewComponent extends TaskBoardView implements OnInit, OnDestroy {
  dataEmitter$: Subject<KeyResultStateTaskMap> = new Subject();
  viewData$: Observable<TaskBoardSwimlaneViewData>;

  states$: Observable<ViewTaskState[]>;

  subscriptions: Subscription[] = [];

  constructor(
    private taskHelper: TaskBoardSwimlaneViewHelper,
    private taskBoardEventService: TaskBoardViewEventService
  ) {
    super();
  }

  ngOnDestroy(): void {
    for (const subscription of this.subscriptions) {
      subscription.unsubscribe();
    }
  }

  ngOnInit(): void {
    this.viewData$ = this.data$.pipe(
      filter(value => !!value),
      map(viewData => {
        const data: TaskBoardSwimlaneViewData = {
          states: viewData.taskStates,
          maps: this.taskHelper.createKeyResultStateTaskMapList(viewData.keyResults, viewData.taskStates, viewData.tasks),
          keyResults: viewData.keyResults
        };

        return data;
      })
    );

    this.subscriptions.push(this.taskBoardEventService.taskInSwimlaneMoved$.subscribe(event => this.onTaskMovedInView(event)));
  }

  onTaskMovedInView(dropEvent: TaskBoardSwimlaneDragDropEvent): void {
    const result: ViewTask = this.taskHelper
      .getMovedTaskWithNewPositionDataInSwimlane(
        dropEvent.taskboardEvent.$event.previousIndex,
        dropEvent.taskboardEvent.$event.previousContainer.data,
        dropEvent.taskboardEvent.$event.currentIndex,
        dropEvent.taskboardEvent.$event.container.data,
        dropEvent.taskboardEvent.state,
        dropEvent.keyResult
      );

    this.taskBoardEventService.taskMovedInView$.next(result);
  }
}
