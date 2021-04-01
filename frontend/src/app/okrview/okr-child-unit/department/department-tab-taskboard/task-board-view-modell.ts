import { Input } from "@angular/core";
import { Observable, Subject } from "rxjs";
import { ViewTaskBoardEvent } from "src/app/shared/model/events/view-taskboard-event";

export abstract class TaskBoardView {
    @Input() public data$: Observable<ViewTaskBoardEvent>;
    @Input() public isInteractive: boolean;
}