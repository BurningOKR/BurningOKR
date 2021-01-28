import { Component, Input, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { TaskBoardViewEventService } from 'src/app/okrview/taskboard-services/task-board-view-event.service';
import { User } from 'src/app/shared/model/api/user';
import { ViewTask } from 'src/app/shared/model/ui/taskboard/view-task';
import { ViewKeyResult } from 'src/app/shared/model/ui/view-key-result';

import { UserService } from 'src/app/shared/services/helper/user.service';
import { KeyResultMapper } from 'src/app/shared/services/mapper/key-result.mapper';

@Component({
  selector: 'app-department-tab-task-card',
  templateUrl: './department-tab-task-card.component.html',
  styleUrls: ['./department-tab-task-card.component.css']
})
export class DepartmentTabTaskCardComponent implements OnInit {
  @Input() task: ViewTask;
  users: Observable<User>[];
  isActive: boolean;
  keyResult$: Observable<ViewKeyResult>;

  constructor(private taskboardEventService: TaskBoardViewEventService,
    private userService: UserService,
    private keyResultService: KeyResultMapper,
    private taskBoardEventService: TaskBoardViewEventService
  ) { }

  ngOnInit(): void {
    this.isActive = false;
    this.users = [];

    if (this.task && this.task.assignedKeyResultId) {
      this.keyResult$ = this.keyResultService.getKeyResultById$(this.task.assignedKeyResultId);
    }

    for (const userid of this.task.assignedUserIds) {
      this.users.push(this.userService.getUserById$(userid));
    }
  }

  setIsActive(isActive: boolean): void {
    this.isActive = isActive;
  }

  updateOrViewTask(): void {
    this.taskBoardEventService.taskUpdateButtonClick$.next(this.task);
  }

  onDelete($event: Event, task: ViewTask): void {
    $event.stopPropagation();

    this.taskboardEventService.taskDeleteButtonClick$.next(task);
  }
}
