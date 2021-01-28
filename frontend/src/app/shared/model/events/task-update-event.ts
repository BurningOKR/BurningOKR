import { ViewTask } from "../ui/taskboard/view-task";

export class TaskUpdateEvent {
    newTask: ViewTask;
    oldTask: ViewTask;
}