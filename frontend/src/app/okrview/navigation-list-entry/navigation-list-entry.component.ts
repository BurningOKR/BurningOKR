import { Component, Input, OnInit } from '@angular/core';
import { OkrUnitRole, OkrUnitSchema } from '../../shared/model/ui/okr-unit-schema';
import { CurrentNavigationService } from '../current-navigation.service';

@Component({
  selector: 'app-navigation-list-entry',
  templateUrl: './navigation-list-entry.component.html',
  styleUrls: ['./navigation-list-entry.component.scss']
})
export class NavigationListEntryComponent implements OnInit {
  @Input() schema: OkrUnitSchema;
  @Input() isSecondUnit: boolean = true;

  isOpen = true;

  constructor(private currentNavigationService: CurrentNavigationService) {}

  ngOnInit(): void {
    this.isOpen = !this.currentNavigationService.isStructureMarkedAsClosed(this.schema.id);
  }

  toggleOpen(): void {
    if(this.isOpen){
      this.currentNavigationService.markStructureAsClosed(this.schema);
    } else {
      this.currentNavigationService.markStructureAsOpen(this.schema.id);
    }
    this.isOpen = !this.isOpen;
  }

  selectStructure(): void {
    this.currentNavigationService.markStructureAsSelected(this.schema.id);
  }

  hasChildUnits(): boolean {
    return this.schema.subDepartments.length > 0;
  }

  isSelectedStructure(): boolean {
    return this.currentNavigationService.isStructureSelected(this.schema.id);
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
