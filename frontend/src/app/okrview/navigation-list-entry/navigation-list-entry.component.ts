import { Component, Input, OnInit } from '@angular/core';
import { OkrUnitSchema, OkrUnitRole } from '../../shared/model/ui/okr-unit-schema';
import { CurrentNavigationService } from '../current-navigation.service';
import { DepartmentNavigationInformation } from '../../shared/model/ui/department-navigation-information';

@Component({
  selector: 'app-navigation-list-entry',
  templateUrl: './navigation-list-entry.component.html',
  styleUrls: ['./navigation-list-entry.component.scss']
})
export class NavigationListEntryComponent implements OnInit {
  @Input() schema: OkrUnitSchema;
  @Input() isSecondUnit: boolean = true;

  currentNavigationInformation = new DepartmentNavigationInformation(-1);
  isOpen = true;

  constructor(private currentNavigationService: CurrentNavigationService) {}

  ngOnInit(): void {
    if(this.currentNavigationService.getClosedStructures().departmentsToClose.includes(this.schema.id)){
      this.isOpen = false;
    }
  }

  toggleOpen(): void {
    if(this.isOpen){
      this.currentNavigationService.markStructureAsClosed(this.schema);
    } else {
      this.currentNavigationService.markStructureAsOpen(this.schema.id);
    }
    this.isOpen = !this.isOpen;
  }

  hasChildUnits(): boolean {
    return this.schema.subDepartments.length > 0;
  }

  isCurrentDepartment(): boolean {
    return this.currentNavigationInformation.departmentId === this.schema.id;
  }

  isMemberOfUnit(): boolean {
    return this.schema.userRole === OkrUnitRole.MEMBER;
  }

  isManagerOfUnit(): boolean {
    return this.schema.userRole === OkrUnitRole.MANAGER;
  }

  isUnitTeam(): boolean {
    return this.schema.isTeam;
  }

  isUnitSubstructure(): boolean {
    return !this.schema.isTeam;
  }
}
