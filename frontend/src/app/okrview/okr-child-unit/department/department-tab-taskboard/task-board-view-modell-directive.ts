import { Directive, Input } from '@angular/core';
import { Observable } from 'rxjs';
import { ViewTaskBoardEvent } from '../../../../shared/model/events/view-taskboard-event';

@Directive()
export abstract class TaskBoardViewDirective {
  @Input() data$: Observable<ViewTaskBoardEvent>;
  @Input() isInteractive: boolean;
}
