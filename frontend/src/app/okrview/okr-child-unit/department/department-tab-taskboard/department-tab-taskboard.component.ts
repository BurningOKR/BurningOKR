import { Component, Input, OnInit } from '@angular/core';
import { CycleAdminCardComponent } from 'src/app/cycle-admin/cycle-admin-card/cycle-admin-card.component';
import { TaskState } from 'src/app/shared/model/api/task.dto';
import { ContextRole } from 'src/app/shared/model/ui/context-role';
import { CycleUnit } from 'src/app/shared/model/ui/cycle-unit';
import { OkrChildUnit } from 'src/app/shared/model/ui/OrganizationalUnit/okr-child-unit';
import { ViewTask } from 'src/app/shared/model/ui/view-task';
import { TaskMapperService } from 'src/app/shared/services/mapper/task.mapper';

@Component({
  selector: 'app-department-tab-taskboard',
  templateUrl: './department-tab-taskboard.component.html',
  styleUrls: ['./department-tab-taskboard.component.css']
})
export class DepartmentTabTaskboardComponent implements OnInit {
  @Input() childUnit: OkrChildUnit;
  @Input() currentUserRole: ContextRole;
  @Input() cycle: CycleUnit;

  taskListTodo: ViewTask[];
  taskListInProgress: ViewTask[];
  taskListBlocked: ViewTask[];
  taskListFinished: ViewTask[];

  constructor(private taskMapperService: TaskMapperService) { }

  ngOnInit() {
    this.taskMapperService.getTasksForDepartment$(this.childUnit.id)
    .subscribe(tasks => this.taskListTodo = tasks);
  }

}
