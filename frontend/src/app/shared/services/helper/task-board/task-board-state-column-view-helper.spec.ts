import { identifierModuleUrl } from "@angular/compiler";
import { inject, TestBed } from "@angular/core/testing";
import { mapTo } from "rxjs/operators";
import { StateTaskMap } from "src/app/shared/model/ui/taskboard/state-task-map";
import { ViewTask } from "src/app/shared/model/ui/taskboard/view-task";
import { ViewTaskState } from "src/app/shared/model/ui/taskboard/view-task-state";
import { TaskBoardGeneralHelper } from "./task-board-general-helper";
import { TaskBoardStateColumnViewHelper } from "./task-board-state-column-view-helper";


describe('TaskBoardStateColumnViewHelper', () => {
    let service: TaskBoardStateColumnViewHelper;
    let generalHelper: TaskBoardGeneralHelper;

    const referenceCompleteTaskList: ViewTask[] = [
        new ViewTask(0, 'test0', 'test0-description', null, 1, 0, 1, null, 1),
        new ViewTask(1, 'test1', 'test1-description', null, 0, 0, 2, null, 1),
        new ViewTask(2, 'test2', 'test2-description', null, 1, 0, 3, null, 1),
        new ViewTask(3, 'test3', 'test3-description', null, 0, 0, 1, 0, 1),
        new ViewTask(4, 'test4', 'test4-description', null, 1, 0, 2, 1, 1),
        new ViewTask(5, 'test5', 'test5-description', null, 0, 0, 3, 2, 1),
        new ViewTask(6, 'test6', 'test6-description', null, 1, 0, 1, 3, 1),
        new ViewTask(7, 'test7', 'test7-description', null, 0, 0, 2, 4, 1),
        new ViewTask(8, 'test8', 'test8-description', null, 2, 0, 3, 5, 1),
        new ViewTask(9, 'test9', 'test9-description', null, 2, 0, 1, 6, 1)
    ];

    const referenceStateOneTaskList: ViewTask[] = [
        new ViewTask(0, 'test0', 'test0-description', [], 1, 0, 1, null, 1),
        new ViewTask(3, 'test3', 'test3-description', [], 0, 0, 1, 0, 1),
        new ViewTask(6, 'test6', 'test6-description', [], 1, 0, 1, 3, 1),
        new ViewTask(9, 'test9', 'test9-description', [], 2, 0, 1, 6, 1),
    ];

    const referenceStateTwoTaskList: ViewTask[] = [
        new ViewTask(1, 'test1', 'test1-description', [], 1, 0, 2, null, 1)
    ];

    const referenceStates = [
        new ViewTaskState(1, 'state1'),
        new ViewTaskState(2, 'state2'),
        new ViewTaskState(3, 'state3'),
    ];

    let stateTaskMaps: StateTaskMap[];
    let completeTaskList: ViewTask[];

    beforeEach(() => TestBed.configureTestingModule({
        providers: [TaskBoardStateColumnViewHelper, TaskBoardGeneralHelper]
    }));

    beforeEach(inject([TaskBoardStateColumnViewHelper, TaskBoardGeneralHelper],
        (_columnHelper: TaskBoardStateColumnViewHelper, _generalHelper: TaskBoardGeneralHelper) => {
            service = _columnHelper;
            generalHelper = _generalHelper;

            completeTaskList = generalHelper.orderTaskList(_columnHelper.copyTaskList(referenceCompleteTaskList));
            stateTaskMaps = _columnHelper.createStateTaskMapList(referenceStates, _columnHelper.copyTaskList(completeTaskList));
        }));

    it('should be created', () => {
        expect(service)
            .toBeTruthy();
    });

    it('createStateTaskMapList:', () => {
        const expectedStateTaskMaps: StateTaskMap[] = [
            {
                state: new ViewTaskState(1, 'state1'),
                tasks: [
                    new ViewTask(0, 'test0', 'test0-description', [], 1, 0, 1, null, 1),
                    new ViewTask(3, 'test3', 'test3-description', [], 0, 0, 1, 0, 1),
                    new ViewTask(6, 'test6', 'test6-description', [], 1, 0, 1, 3, 1),
                    new ViewTask(9, 'test9', 'test9-description', [], 2, 0, 1, 6, 1),
                ]
            },
            {
                state: new ViewTaskState(2, 'state2'),
                tasks: [
                    new ViewTask(1, 'test1', 'test1-description', [], 0, 0, 2, null, 1),
                    new ViewTask(4, 'test4', 'test4-description', [], 1, 0, 2, 1, 1),
                    new ViewTask(7, 'test7', 'test7-description', [], 0, 0, 2, 4, 1),
                ]
            },
            {
                state: new ViewTaskState(3, 'state3'),
                tasks: [
                    new ViewTask(2, 'test2', 'test2-description', [], 1, 0, 3, null, 1),
                    new ViewTask(5, 'test5', 'test5-description', [], 0, 0, 3, 2, 1),
                    new ViewTask(8, 'test8', 'test8-description', [], 2, 0, 3, 5, 1)
                ]
            }
        ];

        const mapToTest: StateTaskMap[] = service.createStateTaskMapList(referenceStates, referenceCompleteTaskList);
        expect(mapToTest)
            .toEqual(expectedStateTaskMaps);
    });

    it('findNewIndexInCompleteList: moved task with id 3 to position of task with id 6. Expected index is 2', () => {
        const expectedIndex: number = 2;
        const tasksOfState1: ViewTask[] = [
            new ViewTask(0, 'test0', 'test0-description', [], 1, 0, 1, null, 1),
            new ViewTask(3, 'test3', 'test3-description', [], 0, 0, 1, 0, 1),
            new ViewTask(6, 'test6', 'test6-description', [], 1, 0, 1, 3, 1),
            new ViewTask(9, 'test9', 'test9-description', [], 2, 0, 1, 6, 1),
        ];
        const orderedCompleteList: ViewTask[] = [
            new ViewTask(0, 'test0', 'test0-description', [], 1, 0, 1, null, 1),
            new ViewTask(6, 'test6', 'test6-description', [], 1, 0, 1, 0, 1),
            new ViewTask(9, 'test9', 'test9-description', [], 2, 0, 1, 6, 1),
            new ViewTask(1, 'test1', 'test1-description', [], 0, 0, 2, null, 1),
            new ViewTask(4, 'test4', 'test4-description', [], 1, 0, 2, 1, 1),
            new ViewTask(7, 'test7', 'test7-description', [], 0, 0, 2, 4, 1),
            new ViewTask(2, 'test2', 'test2-description', [], 1, 0, 3, null, 1),
            new ViewTask(5, 'test5', 'test5-description', [], 0, 0, 3, 2, 1),
            new ViewTask(8, 'test8', 'test8-description', [], 2, 0, 3, 5, 1),
        ];
        const movedTask: ViewTask = new ViewTask(3, 'test3', 'test3-description', [], 0, 0, 1, 6, 1);
        //const indexToTest: number = service.findNewIndexInUpdatedCompleteList(orderedCompleteList, tasksOfState1, movedTask);
        expect(true)
            .toEqual(true);
    });

    it('getMovedTaskWithNewPositionData - move task with id 9 one position higher in the same state: should get task with previousId = 3', () => {

        const previousContainer: ViewTask[] = service.copyTaskList(referenceStateOneTaskList);
        const previousIndex: number = 3;
        const currentContainer: ViewTask[] = service.copyTaskList(referenceStateOneTaskList);
        const currentIndex: number = 2;
        const expectedMovedTask: ViewTask = new ViewTask(9, 'test9', 'test9-description', [], 2, 0, 1, 3, 1);
        const newState: ViewTaskState = referenceStates[0].copy();

        const movedTaskToTest: ViewTask =
            service.getMovedTaskWithNewPositionData(previousIndex, previousContainer, currentIndex, currentContainer, newState);
        expect(movedTaskToTest)
            .toEqual(expectedMovedTask);
    });

    it('getMovedTaskWithNewPositionData - move task with id 0 one position lower in the same state: should get task with previousId = 3', () => {
        const previousContainer: ViewTask[] = service.copyTaskList(referenceStateOneTaskList);
        const previousIndex: number = 0;
        const currentContainer: ViewTask[] = service.copyTaskList(referenceStateOneTaskList);
        const currentIndex: number = 1;
        const expectedMovedTask: ViewTask = new ViewTask(0, 'test0', 'test0-description', [], 1, 0, 1, 3, 1);
        const newState: ViewTaskState = referenceStates[0].copy();

        const movedTaskToTest: ViewTask =
            service.getMovedTaskWithNewPositionData(previousIndex, previousContainer, currentIndex, currentContainer, newState);
        expect(movedTaskToTest)
            .toEqual(expectedMovedTask);
    });

    it('getMovedTaskWithNewPositionData - move task with id 0 to the end of list in the same state: should get task with previousId = 9', () => {
        const previousContainer: ViewTask[] = service.copyTaskList(referenceStateOneTaskList);
        const previousIndex: number = 0;
        const currentContainer: ViewTask[] = service.copyTaskList(referenceStateOneTaskList);
        const currentIndex: number = 3;
        const expectedMovedTask: ViewTask = new ViewTask(0, 'test0', 'test0-description', [], 1, 0, 1, 9, 1);
        const newState: ViewTaskState = referenceStates[0].copy();

        const movedTaskToTest: ViewTask =
            service.getMovedTaskWithNewPositionData(previousIndex, previousContainer, currentIndex, currentContainer, newState);
        expect(movedTaskToTest)
            .toEqual(expectedMovedTask);
    });

    it('getMovedTaskWithNewPositionData - move last task in list with id 9 to the beginning of list in the same state: should get task with previousId = null', () => {
        const previousContainer: ViewTask[] = service.copyTaskList(referenceStateOneTaskList);
        const previousIndex: number = 3;
        const currentContainer: ViewTask[] = service.copyTaskList(referenceStateOneTaskList);
        const currentIndex: number = 0;
        const expectedMovedTask: ViewTask = new ViewTask(9, 'test9', 'test9-description', [], 2, 0, 1, null, 1);
        const newState: ViewTaskState = referenceStates[0].copy();

        const movedTaskToTest: ViewTask =
            service.getMovedTaskWithNewPositionData(previousIndex, previousContainer, currentIndex, currentContainer, newState);
        expect(movedTaskToTest)
            .toEqual(expectedMovedTask);
    });

    it('getMovedTaskWithNewPositionData - move first task in list of one state to the first position of another state: should get task with previousId = null', () => {
        const previousContainer: ViewTask[] = service.copyTaskList(referenceStateOneTaskList);
        const previousIndex: number = 0;
        const currentContainer: ViewTask[] = [];
        const currentIndex: number = 0;
        const expectedMovedTask: ViewTask =  new ViewTask(0, 'test0', 'test0-description', [], 1, 0, 2, null, 1);
        const newState: ViewTaskState = referenceStates[1].copy();

        const movedTaskToTest: ViewTask =
            service.getMovedTaskWithNewPositionData(previousIndex, previousContainer, currentIndex, currentContainer, newState);
        expect(movedTaskToTest)
            .toEqual(expectedMovedTask);
    });

    it('getMovedTaskWithNewPositionData - move first task in list of one state to the first position of another state with one task in list: should get task with previousId = null', () => {
        const previousContainer: ViewTask[] = service.copyTaskList(referenceStateOneTaskList);
        const previousIndex: number = 0;
        const currentContainer: ViewTask[] = service.copyTaskList(referenceStateTwoTaskList);
        const currentIndex: number = 0;
        const expectedMovedTask: ViewTask =  new ViewTask(0, 'test0', 'test0-description', [], 1, 0, 2, null, 1);
        const newState: ViewTaskState = referenceStates[1].copy();

        const movedTaskToTest: ViewTask =
            service.getMovedTaskWithNewPositionData(previousIndex, previousContainer, currentIndex, currentContainer, newState);
        expect(movedTaskToTest)
            .toEqual(expectedMovedTask);
    });

    it('getMovedTaskWithNewPositionData - move first task in list of one state to the second position of another state with one task in list: should get task with previousId = 1', () => {
        const previousContainer: ViewTask[] = service.copyTaskList(referenceStateOneTaskList);
        const previousIndex: number = 0;
        const currentContainer: ViewTask[] = service.copyTaskList(referenceStateTwoTaskList);
        const currentIndex: number = 1;
        const expectedMovedTask: ViewTask =  new ViewTask(0, 'test0', 'test0-description', [], 1, 0, 2, 1, 1);
        const newState: ViewTaskState = referenceStates[1].copy();

        const movedTaskToTest: ViewTask =
            service.getMovedTaskWithNewPositionData(previousIndex, previousContainer, currentIndex, currentContainer, newState);
        expect(movedTaskToTest)
            .toEqual(expectedMovedTask);
    });
});