import { Component, OnDestroy, OnInit } from '@angular/core';
import { Observable, Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { TaskBoardViewEventService } from '../../../../taskboard-services/task-board-view-event.service';
import { StateTaskMap } from '../../../../../shared/model/ui/taskboard/state-task-map';
import { ViewTask } from '../../../../../shared/model/ui/taskboard/view-task';
import { ViewTaskState } from '../../../../../shared/model/ui/taskboard/view-task-state';
import { ViewKeyResult } from '../../../../../shared/model/ui/view-key-result';
import { TaskBoardStateColumnViewHelper } from '../../../../../shared/services/helper/task-board/task-board-state-column-view-helper';
import { TaskBoardViewDirective } from '../task-board-view-modell-directive';
import { TaskBoardDragDropEvent } from '../taskboard-column/taskboard-column.component';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-taskboard-state-column-view',
  templateUrl: './taskboard-state-column-view.component.html',
  styleUrls: ['./taskboard-state-column-view.component.css'],
})
export class TaskboardStateColumnViewComponent extends TaskBoardViewDirective implements OnInit, OnDestroy {
  subscriptions: Subscription[] = [];
  statesWithTasks$: Observable<StateTaskMap[]>;
  keyResults: ViewKeyResult[] = [];

  constructor(
    private taskHelper: TaskBoardStateColumnViewHelper,
    private taskBoardEventService: TaskBoardViewEventService,
    private translate: TranslateService,
  ) {
    super();
  }

  ngOnInit(): void {
    this.subscriptions.push(
      this.taskBoardEventService.taskDragAndDropInView$.subscribe(event => this.onTaskMovedInView(event)),
    );

    this.statesWithTasks$ = this.data$.pipe(
      filter(value => !!value),
      map(viewdata => {
        this.keyResults = viewdata.keyResults;
        const defaultStateText: string = this.translate.instant('taskboard-state-column-view.default-state-text');
        const defaultMap: StateTaskMap = { state: new ViewTaskState(null, 'defaultStateText'), tasks: null };

        return this.taskHelper.createStateTaskMapList(viewdata.taskStates, viewdata.tasks, defaultMap);
      }),
    );
  }

  ngOnDestroy(): void {
    for (const subscription of this.subscriptions) {
      subscription.unsubscribe();
    }
  }

  onTaskMovedInView(dropEvent: TaskBoardDragDropEvent): void {

    const result: ViewTask = this.taskHelper
      .getMovedTaskWithNewPositionData(dropEvent.$event.previousIndex, dropEvent.$event.previousContainer.data,
        dropEvent.$event.currentIndex, dropEvent.$event.container.data, dropEvent.state,
      );

    this.taskBoardEventService.taskMovedInView$.next(result);
  }

}
