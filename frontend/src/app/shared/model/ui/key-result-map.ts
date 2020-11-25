import { Observable } from "rxjs";
import { ViewKeyResult } from "./view-key-result";
import { ViewObjective } from "./view-objective";

export interface KeyResultMap {
    objective: ViewObjective;
    keyResults$: Observable<ViewKeyResult[]>;
}
