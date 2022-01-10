import { TaskBoardStateColumnViewHelper } from './task-board-state-column-view-helper';
import { ViewKeyResult } from '../../../model/ui/view-key-result';
import { ViewTaskState } from '../../../model/ui/taskboard/view-task-state';
import { ViewTask } from '../../../model/ui/taskboard/view-task';
import { KeyResultStateTaskMap } from '../../../model/ui/taskboard/key-result-state-task-map';
import { Injectable, OnDestroy } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { Subscription } from 'rxjs';

@Injectable()
export class TaskBoardSwimlaneViewHelper extends TaskBoardStateColumnViewHelper implements OnDestroy{

    noKeyResultTitle = '';
    subscriptions: Subscription[] = [];
    translate: TranslateService;

    constructor(translate: TranslateService) {
      super();
      this.translate = translate;
    }

    ngOnDestroy(): void {
        this.subscriptions.forEach(sub => sub.unsubscribe());
    }

    createKeyResultStateTaskMapList(keyResults: ViewKeyResult[], states: ViewTaskState[], tasks: ViewTask[]): KeyResultStateTaskMap[] {
        const emptyKeyResult: ViewKeyResult = new ViewKeyResult(null, null, null, null, null,
          this.noKeyResultTitle, null, null, null, null);

        //ToDo (C.K. 3.1.2022 fix updating doesnt change if rendered once)
        this.subscriptions.push(this.translate.stream('task-board-swimlane-view-helper.no-keyresult-title').subscribe((res: string) => {
          emptyKeyResult.keyResult = res;
        }));

        const copiedTasks: ViewTask[] = [];
        for (const task of tasks) {
            copiedTasks.push(task.copy());
        }

        const map: KeyResultStateTaskMap[] = [];

        const tasksWithoutKeyResults: ViewTask[] = this.findTasksWithoutKeyResult(tasks);
        this.removeTasksFromTaskList(copiedTasks, tasksWithoutKeyResults);
        if (copiedTasks.length >= 0) {
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
        }

        return map;
    }

    getMovedTaskWithNewPositionDataInSwimlane(
        previousIndex: number, previousContainer: ViewTask[],
        currentIndex: number, currentContainer: ViewTask[],
        newTaskState: ViewTaskState, newKeyResult: ViewKeyResult): ViewTask {

        const result: ViewTask = super
            .getMovedTaskWithNewPositionData(previousIndex, previousContainer, currentIndex, currentContainer, newTaskState);

        if (result && newKeyResult && result.assignedKeyResultId !== newKeyResult.id) {
            result.assignedKeyResultId = newKeyResult.id;
        }

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
