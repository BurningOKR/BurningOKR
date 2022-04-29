export class DepartmentNavigationInformation {
  departmentId: number;
  departmentsToClose: number[];

  constructor(id: number) {
    this.departmentId = id;
    this.departmentsToClose = [];
  }
}
