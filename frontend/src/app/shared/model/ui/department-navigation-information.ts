export class DepartmentNavigationInformation {
  departmentId: number;
  departmentsToOpen: number[];

  constructor(id: number, departmentsToOpen: number[]) {
    this.departmentId = id;
    this.departmentsToOpen = departmentsToOpen;
  }
}
