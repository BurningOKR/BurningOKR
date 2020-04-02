import { Injectable } from '@angular/core';
import { DepartmentUnit } from '../../../shared/model/ui/OrganizationalUnit/department-unit';

@Injectable({
  providedIn: 'root'
})
export class InactiveTeamService {
  private _inactiveTeams: number[] = [];

  handleTeam(department: DepartmentUnit): void {
    const alreadyContainsDepartment: boolean = this._inactiveTeams.some(id => id === department.id);
    if (!department.isActive && !alreadyContainsDepartment) {
      this._inactiveTeams.push(department.id);
    } else if (department.isActive && alreadyContainsDepartment) {
      this.removeInactiveTeam(department.id);
    }
  }

  private removeInactiveTeam(id: number): void {
    const index: number = this._inactiveTeams.indexOf(id);
    if (index >= 0) {
      this._inactiveTeams.splice(index, 1);
    }
  }
}
