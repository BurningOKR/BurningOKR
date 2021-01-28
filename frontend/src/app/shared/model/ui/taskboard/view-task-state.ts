import { TaskBoardObject } from "./task-board-object";

export class ViewTaskState implements TaskBoardObject {
    id: number;
    name: string;

    constructor(id: number, name: string) {
        this.id = id;
        this.name = name;
    }

    copy(): ViewTaskState {
        return new ViewTaskState(this.id, this.name);
    }
}
