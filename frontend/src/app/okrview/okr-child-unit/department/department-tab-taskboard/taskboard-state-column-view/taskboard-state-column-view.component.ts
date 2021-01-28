import { Component, OnDestroy, OnInit } from '@angular/core';

import { Observable, Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { TaskBoardViewEventService } from 'src/app/okrview/taskboard-services/task-board-view-event.service';
import { StateTaskMap } from 'src/app/shared/model/ui/taskboard/state-task-map';
import { ViewTask } from 'src/app/shared/model/ui/taskboard/view-task';
import { TaskBoardStateColumnViewHelper } from 'src/app/shared/services/helper/task-board/task-board-state-column-view-helper';
import { TaskBoardView } from '../task-board-view-modell';
import { TaskBoardDragDropEvent } from '../taskboard-column/taskboard-column.component';

@Component({
  selector: 'app-taskboard-state-column-view',
  templateUrl: './taskboard-state-column-view.component.html',
  styleUrls: ['./taskboard-state-column-view.component.css']
})
export class TaskboardStateColumnViewComponent extends TaskBoardView implements OnInit, OnDestroy {
  subscriptions: Subscription[] = [];
  statesWithTasks$: Observable<StateTaskMap[]>;

  constructor(
    private taskHelper: TaskBoardStateColumnViewHelper,
    private taskBoardEventService: TaskBoardViewEventService
  ) {
    super();
  }

  ngOnInit() {
    console.log("column view");
    this.subscriptions.push(
      this.taskBoardEventService.taskDragAndDropInView$.subscribe(event => this.onTaskMovedInView(event))
    );

    this.statesWithTasks$ = this.data$.pipe(
      filter(value => !!value),
      map(viewdata => {
        console.log('');
        return this.taskHelper.createStateTaskMapList(viewdata.taskStates, viewdata.tasks);
      }));
  }

  ngOnDestroy(): void {
    for (const subscription of this.subscriptions) {
      subscription.unsubscribe();
    }
  }

  onTaskMovedInView(dropEvent: TaskBoardDragDropEvent): void {
    let result: ViewTask;

    console.log('column-view');
    console.log('onTaskMovedInView');
    console.log(dropEvent);


    result = this.taskHelper.getMovedTaskWithNewPositionData(dropEvent.$event.previousIndex, dropEvent.$event.previousContainer.data,
      dropEvent.$event.currentIndex, dropEvent.$event.container.data, dropEvent.state);

    console.log(result);

    this.taskBoardEventService.taskMovedInView$.next(result);
  }

}
