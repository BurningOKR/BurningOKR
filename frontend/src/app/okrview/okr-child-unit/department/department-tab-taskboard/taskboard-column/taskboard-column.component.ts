import { CdkDragDrop, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';
import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material';
import { Subscription } from 'rxjs';
import { filter, switchMap, take } from 'rxjs/operators';
import { OkrChildUnit } from 'src/app/shared/model/ui/OrganizationalUnit/okr-child-unit';
import { ViewTask } from 'src/app/shared/model/ui/view-task';
import { ViewTaskState } from 'src/app/shared/model/ui/view-task-state';
import { TaskFormComponent } from '../../department-tab-task-form/department-tab-task-form.component';

@Component({
  selector: 'app-taskboard-column',
  templateUrl: './taskboard-column.component.html',
  styleUrls: ['./taskboard-column.component.css']
})
export class TaskboardColumnComponent implements OnInit, OnDestroy {
  @Input() state: ViewTaskState;
  @Input() public taskList: ViewTask[] = [];
  @Input() childUnit: OkrChildUnit;

  @Output() createTask: EventEmitter<ViewTaskState> = new EventEmitter();
  @Output() updateTask: EventEmitter<ViewTask> = new EventEmitter();
  @Output() deleteTask: EventEmitter<ViewTask> = new EventEmitter();
  @Output() movedTask: EventEmitter<ViewTask> = new EventEmitter();

  subscriptions: Subscription[] = [];

  constructor(private matDialog: MatDialog) { }

  ngOnInit(): void {
    console.log(this.taskList);
  }

  ngOnDestroy(): void {
    for (const subscription of this.subscriptions) {
      subscription.unsubscribe();
    }
  }

  createNewTask(): void {
    this.createTask.emit(this.state);
  }

  openTask(task: ViewTask): void {
    this.updateTask.emit(task);
  }

  dropTask($event: CdkDragDrop<ViewTask[]>): void {
    console.log("drop Task");
    console.log($event);
    if ($event.previousContainer === $event.container) {
      moveItemInArray($event.container.data, $event.previousIndex, $event.currentIndex);
    } else {
      transferArrayItem($event.previousContainer.data,
        $event.container.data,
        $event.previousIndex,
        $event.currentIndex);
    }
    $event.container.data[$event.currentIndex].stateId = this.state.id;
    this.movedTask.emit($event.container.data[$event.currentIndex]);
  }

  onDelete(task: ViewTask) {
    this.deleteTask.emit(task);
  }
}
