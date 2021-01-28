import { KeyResultStateTaskMap } from "src/app/shared/model/ui/taskboard/key-result-state-task-map";
import { ViewTask } from "src/app/shared/model/ui/taskboard/view-task";
import { ViewTaskState } from "src/app/shared/model/ui/taskboard/view-task-state";
import { ViewKeyResult } from "src/app/shared/model/ui/view-key-result";
import { TaskBoardStateColumnViewHelper } from "./task-board-state-column-view-helper";


export class TaskBoardSwimlaneViewHelper extends TaskBoardStateColumnViewHelper {
    createKeyResultStateTaskMapList(keyResults: ViewKeyResult[], states: ViewTaskState[], tasks: ViewTask[]): KeyResultStateTaskMap[] {
        const emptyKeyResult: ViewKeyResult = new ViewKeyResult(null, null, null, null, null, 'Kein Key Result', null, null, null, null);

        const copiedTasks: ViewTask[] = [];
        for (const task of tasks) {
            copiedTasks.push(task.copy());
        }

        const map: KeyResultStateTaskMap[] = [];

        const tasksWithoutKeyResults: ViewTask[] = this.findTasksWithoutKeyResult(tasks);
        this.removeTasksFromTaskList(copiedTasks, tasksWithoutKeyResults);
        map.push({ keyResult: emptyKeyResult, statesWithTasks: this.createStateTaskMapList(states, tasksWithoutKeyResults) });

        for (const keyResult of keyResults) {
            const tasksForKeyResult: ViewTask[] = copiedTasks.filter(task => task.assignedKeyResultId === keyResult.id);
            this.removeTasksFromTaskList(copiedTasks, tasksForKeyResult);

            const keyResultMap: KeyResultStateTaskMap = {
                keyResult,
                statesWithTasks: this.createStateTaskMapList(states, tasksForKeyResult)
            };
            map.push(keyResultMap);
        }

        return map;
    }

    getMovedTaskWithNewPositionDataInSwimlane(
        previousIndex: number, previousContainer: ViewTask[],
        currentIndex: number, currentContainer: ViewTask[],
        newTaskState: ViewTaskState, newKeyResult: ViewKeyResult): ViewTask {

        const result: ViewTask = super
            .getMovedTaskWithNewPositionData(previousIndex, previousContainer, currentIndex, currentContainer, newTaskState);
        console.log("getMovedTaskWithNewPositionDataInSwimlane");
        console.log("before keyresult");
        console.log(result);
        if (result && newKeyResult && result.assignedKeyResultId !== newKeyResult.id) {
            result.assignedKeyResultId = newKeyResult.id;
        }
        console.log("after keyresult");
        console.log(result);
        return result;
    }

    findTasksWithoutKeyResult(tasks: ViewTask[]): ViewTask[] {
        let result: ViewTask[];
        if (tasks) {
            result = tasks.filter(task => task.assignedKeyResultId == null);
        }

        return result;
    }
}
