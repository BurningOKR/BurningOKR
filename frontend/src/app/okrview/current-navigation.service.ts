import { Injectable } from '@angular/core';
import { DepartmentNavigationInformation } from '../shared/model/ui/department-navigation-information';
import { OkrUnitSchema } from '../shared/model/ui/okr-unit-schema';

@Injectable({
  providedIn: 'root'
})
export class CurrentNavigationService {

  currentNavigationInformation = new DepartmentNavigationInformation(-1);

  getClosedStructures(): DepartmentNavigationInformation{
    return this.currentNavigationInformation;
  }

  isStructureMarkedAsClosed(id: number): boolean {
    return this.currentNavigationInformation.departmentsToClose.includes(id);
  }

  markStructureAsClosed(schema: OkrUnitSchema): void {
    this.currentNavigationInformation.departmentsToClose.push(schema.id);

    schema.subDepartments.filter(subDepartment => !this.getClosedStructures().departmentsToClose.includes(subDepartment.id))
      .forEach(subDepartment => this.markStructureAsClosed(subDepartment));
  }

  markStructureAsOpen(id: number): void {
    const index: number = this.currentNavigationInformation.departmentsToClose.indexOf(id);
    this.currentNavigationInformation.departmentsToClose.splice(index, 1);
  }

  isStructureSelected(id: number): boolean {
    return this.currentNavigationInformation.selectedStructure === id;
  }

  markStructureAsSelected(id: number): void {
    this.currentNavigationInformation.selectedStructure = id;
  }

}
