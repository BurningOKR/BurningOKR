import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ContextRole } from '../../shared/model/ui/context-role';

@Component({
  selector: 'app-add-child-unit-button',
  templateUrl: './add-child-unit-button.component.html',
  styleUrls: ['./add-child-unit-button.component.css']
})
export class AddChildUnitButtonComponent {

  @Input() cycleClosed: boolean;
  @Input() userRole: ContextRole;

  @Output() clickedAddSubDepartment: EventEmitter<void> = new EventEmitter<void>();
  @Output() clickedAddSubBranch: EventEmitter<void> = new EventEmitter<void>();
  @Output() clickedAddTopicDraft: EventEmitter<void> = new EventEmitter<void>();
}
