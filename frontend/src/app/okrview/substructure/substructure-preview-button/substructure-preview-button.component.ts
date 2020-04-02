import { Component, Input, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { DepartmentMapper } from '../../../shared/services/mapper/department.mapper';
import { DepartmentUnit } from '../../../shared/model/ui/OrganizationalUnit/department-unit';

@Component({
  selector: 'app-substructure-preview-button',
  templateUrl: './substructure-preview-button.component.html',
  styleUrls: ['./substructure-preview-button.component.scss']
})
export class SubstructurePreviewButtonComponent implements OnInit {
  @Input() departmentId: number;
  @Input() title: string = 'Department';

  department$: Observable<DepartmentUnit>;

  constructor(private departmentMapperService: DepartmentMapper) {
  }

  ngOnInit(): void {
    this.department$ = this.departmentMapperService.getDepartmentById$(this.departmentId);
  }
}
