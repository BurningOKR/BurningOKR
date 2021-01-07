import { Component, Input, OnInit } from '@angular/core';
import { OkrDepartment } from '../../../../shared/model/ui/OrganizationalUnit/okr-department';

@Component({
  selector: 'app-department-tab-description',
  templateUrl: './department-tab-description.component.html',
  styleUrls: ['./department-tab-description.component.css']
})
export class DepartmentTabDescriptionComponent implements OnInit {
  @Input() department: OkrDepartment;

  ngOnInit() {
  }

}
