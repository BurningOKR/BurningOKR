import { Component, Input, OnInit } from '@angular/core';
import { ViewTask } from '../../../../shared/model/ui/taskboard/view-task';
import { ViewKeyResult } from 'src/app/shared/model/ui/view-key-result';

import { UserService } from '../../../../shared/services/helper/user.service';
import { KeyResultMapper } from '../../../../shared/services/mapper/key-result.mapper';
import { TaskBoardViewEventService } from '../../../taskboard-services/task-board-view-event.service';

export interface TaskCardInformation {
  task: ViewTask;
  keyResult: ViewKeyResult;
}

@Component({
  selector: 'app-department-tab-task-card',
  templateUrl: './department-tab-task-card.component.html',
  styleUrls: ['./department-tab-task-card.component.scss'],
})
export class DepartmentTabTaskCardComponent implements OnInit {
  @Input() taskInformations: TaskCardInformation;
  @Input() isInteractive: boolean;

  isActive: boolean;

  constructor(
    private userService: UserService,
    private keyResultService: KeyResultMapper,
    private taskBoardEventService: TaskBoardViewEventService,
  ) {
  }

  ngOnInit(): void {
    this.isActive = false;
  }

  setIsActive(isActive: boolean): void {
    this.isActive = isActive;
  }

  updateOrViewTask(): void {
    this.taskBoardEventService.taskUpdateButtonClick$.next(this.taskInformations.task);
  }

  onDelete($event: Event, task: ViewTask): void {
    $event.stopPropagation();

    this.taskBoardEventService.taskDeleteButtonClick$.next(task);
  }
}
