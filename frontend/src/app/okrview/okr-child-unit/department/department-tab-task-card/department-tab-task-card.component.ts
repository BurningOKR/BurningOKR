import { Component, Input, OnInit } from '@angular/core';
import { Observable, of } from 'rxjs';
import { TaskBoardViewEventService } from '../../../../okrview/taskboard-services/task-board-view-event.service';
import { User } from '../../../..//shared/model/api/user';
import { ViewTask } from '../../../../shared/model/ui/taskboard/view-task';
import { ViewKeyResult } from '../../../../shared/model/ui/view-key-result';

import { UserService } from '../../../../shared/services/helper/user.service';
import { KeyResultMapper } from '../../../../shared/services/mapper/key-result.mapper';

export interface TaskCardInformation {
  task: ViewTask;
  keyResult: ViewKeyResult;
}

@Component({
  selector: 'app-department-tab-task-card',
  templateUrl: './department-tab-task-card.component.html',
  styleUrls: ['./department-tab-task-card.component.css']
})
export class DepartmentTabTaskCardComponent implements OnInit {
  @Input() taskInformations: TaskCardInformation;
  @Input() isInteractive: boolean;

  users: Observable<User>[];
  isActive: boolean;

  constructor(
    private taskboardEventService: TaskBoardViewEventService,
    private userService: UserService,
    private keyResultService: KeyResultMapper,
    private taskBoardEventService: TaskBoardViewEventService
  ) { }

  ngOnInit(): void {
    this.isActive = false;
    this.users = [];

    for (const userid of this.taskInformations.task.assignedUserIds) {
      this.users.push(this.userService.getUserById$(userid));
    }
  }

  setIsActive(isActive: boolean): void {
    this.isActive = isActive;
  }

  updateOrViewTask(): void {
    this.taskBoardEventService.taskUpdateButtonClick$.next(this.taskInformations.task);
  }

  onDelete($event: Event, task: ViewTask): void {
    $event.stopPropagation();

    this.taskboardEventService.taskDeleteButtonClick$.next(task);
  }
}
