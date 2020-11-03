import { Component, Input, OnInit } from '@angular/core';
import { ViewTask } from 'src/app/shared/model/ui/view-task';

@Component({
  selector: 'app-department-tab-task-card',
  templateUrl: './department-tab-task-card.component.html',
  styleUrls: ['./department-tab-task-card.component.css']
})
export class DepartmentTabTaskCardComponent implements OnInit {
  @Input() task: ViewTask;

  constructor() { }

  ngOnInit() {
  }

}
