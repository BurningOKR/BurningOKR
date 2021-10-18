import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { OkrUnitSchema, OkrUnitRole } from '../../shared/model/ui/okr-unit-schema';
import { CurrentNavigationService } from '../current-navigation.service';
import { DepartmentNavigationInformation } from '../../shared/model/ui/department-navigation-information';

@Component({
  selector: 'app-navigation-list-entry',
  templateUrl: './navigation-list-entry.component.html',
  styleUrls: ['./navigation-list-entry.component.scss']
})
export class NavigationListEntryComponent implements OnInit, OnDestroy {
  @Input() schema: OkrUnitSchema;
  @Input() isSecondUnit: boolean = true;
  @Input() startsOpen: boolean = false;

  currentNavigationInformation = new DepartmentNavigationInformation(-1, []);
  navigationInformationSubscription: Subscription;
  isOpen = false;

  constructor(private currentNavigationService: CurrentNavigationService) {}

  ngOnInit(): void {
    if (this.startsOpen) {
      this.isOpen = true;
    }
    this.navigationInformationSubscription = this.currentNavigationService
      .getCurrentDepartmentNavigationInformation$()
      .subscribe(x => {
        this.currentNavigationInformation = x;
        if (!(this.currentNavigationInformation.departmentsToOpen.indexOf(this.schema.id) !== -1)) {
          this.isOpen = true;
        }
      });
  }

  ngOnDestroy(): void {
    this.navigationInformationSubscription.unsubscribe();
  }

  toggleOpen(): void {
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
