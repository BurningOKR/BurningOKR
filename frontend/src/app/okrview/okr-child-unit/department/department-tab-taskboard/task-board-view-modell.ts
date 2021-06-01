import { Input } from '@angular/core';
import { Observable } from 'rxjs';
import { ViewTaskBoardEvent } from '../../../../shared/model/events/view-taskboard-event';

export abstract class TaskBoardView {
    @Input() data$: Observable<ViewTaskBoardEvent>;
    @Input() isInteractive: boolean;
}
