import { Injectable } from "@angular/core";
import { Subject } from "rxjs";
import { ViewTask } from "src/app/shared/model/ui/taskboard/view-task";
import { ViewTaskState } from "src/app/shared/model/ui/taskboard/view-task-state";
import { TaskBoardDragDropEvent } from "../okr-child-unit/department/department-tab-taskboard/taskboard-column/taskboard-column.component";
import { TaskBoardSwimlaneDragDropEvent } from "../okr-child-unit/department/department-tab-taskboard/taskboard-swimlane-view/taskboard-swimlane/taskboard-swimlane.component";


@Injectable({
  providedIn: 'root'
})
export class TaskBoardViewEventService {
  /**
   * Events for taskboard modul. The taskboard component react to them.
   * -------
   */
  taskAddButtonClick$: Subject<ViewTaskState> = new Subject();
  taskDeleteButtonClick$: Subject<ViewTask> = new Subject();
  taskUpdateButtonClick$: Subject<ViewTask> = new Subject();
  taskMovedInView$: Subject<ViewTask> = new Subject();

  /**
   * Events for the taskboard views
   */
  taskDragAndDropInView$: Subject<TaskBoardDragDropEvent> = new Subject(); // Would be emitted by column component, when a taskcard was moved

  taskInSwimlaneMoved$: Subject<TaskBoardSwimlaneDragDropEvent> = new Subject(); // Would be emitted by swimlane component, after the taskDragAndDropInView-Event was emitted
}
