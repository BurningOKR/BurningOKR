import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

import { ViewTask } from 'src/app/shared/model/ui/view-task';

@Component({
  selector: 'app-department-tab-task-card',
  templateUrl: './department-tab-task-card.component.html',
  styleUrls: ['./department-tab-task-card.component.css']
})
export class DepartmentTabTaskCardComponent implements OnInit {
  @Input() task: ViewTask;
  @Output() deleteEmitter: EventEmitter<ViewTask> = new EventEmitter();
  isActive: boolean;

  ngOnInit(): void {
    this.isActive = false;
  }

  setIsActive(isActive: boolean): void {
    this.isActive = isActive;
  }

  onDelete($event: Event, task: ViewTask): void {
    $event.stopPropagation();
    $event.preventDefault();
    console.log("onDelete");

    this.deleteEmitter.emit(task);
  }
}
