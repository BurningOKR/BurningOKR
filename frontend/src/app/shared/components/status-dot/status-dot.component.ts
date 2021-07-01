import { Component, Input } from '@angular/core';
import { status } from '../../model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft-status-enum';

@Component({
  selector: 'app-status-dot',
  templateUrl: './status-dot.component.html',
  styleUrls: ['./status-dot.component.css']
})
export class StatusDotComponent {

  enumStatus = status;
  @Input() state: status = status.draft;
}
