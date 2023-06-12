import { Component, EventEmitter, Input, Output } from '@angular/core';
import { OkrBranch } from '../../shared/model/ui/OrganizationalUnit/okr-branch';
import { CompanyUnit } from '../../shared/model/ui/OrganizationalUnit/company-unit';
import { ContextRole } from '../../shared/model/ui/context-role';

@Component({
  selector: 'app-add-child-unit-button',
  templateUrl: './add-child-unit-button.component.html',
  styleUrls: ['./add-child-unit-button.component.scss'],
})
export class AddChildUnitButtonComponent {

  @Input() cycleClosed: boolean;
  @Input() userRole: ContextRole;
  @Input() topicSubject: OkrBranch | CompanyUnit;

  @Output() clickedAddSubDepartment: EventEmitter<void> = new EventEmitter<void>();
  @Output() clickedAddSubBranch: EventEmitter<void> = new EventEmitter<void>();
}
