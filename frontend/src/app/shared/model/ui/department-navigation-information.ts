export class DepartmentNavigationInformation {
  departmentId: number;
  departmentsToClose: number[];
  selectedStructure: number;

  constructor(id: number) {
    this.departmentId = id;
    this.departmentsToClose = [];
  }
}
