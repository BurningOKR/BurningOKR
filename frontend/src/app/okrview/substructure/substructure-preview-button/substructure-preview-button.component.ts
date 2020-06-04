import { Component, Input, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { DepartmentMapper } from '../../../shared/services/mapper/department.mapper';
import { DepartmentUnit } from '../../../shared/model/ui/OrganizationalUnit/department-unit';
import { SubStructure } from '../../../shared/model/ui/OrganizationalUnit/sub-structure';
import { StructureMapper } from '../../../shared/services/mapper/structure.mapper';

@Component({
  selector: 'app-substructure-preview-button',
  templateUrl: './substructure-preview-button.component.html',
  styleUrls: ['./substructure-preview-button.component.scss']
})
export class SubstructurePreviewButtonComponent implements OnInit {
  @Input() structureId: number;
  @Input() title: string = 'Department';

  subStructure$: Observable<SubStructure>;

  constructor(private structureMapperService: StructureMapper) {
  }

  ngOnInit(): void {
    this.subStructure$ = this.structureMapperService.getSubStructureById$(this.structureId);
  }
}
