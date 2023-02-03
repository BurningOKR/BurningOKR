import { inject, TestBed } from '@angular/core/testing';
import { ViewTask } from '../../../model/ui/taskboard/view-task';
import { TaskBoardGeneralHelper } from './task-board-general-helper';

describe('TaskBoardGeneralHelper', () => {
  let service: TaskBoardGeneralHelper;
  const completeTaskList: ViewTask[] = [
    new ViewTask(0, 'test0', 'test0-description', [], 1, 0, 1, null, 1),
    new ViewTask(1, 'test1', 'test1-description', [], 0, 0, 2, null, 1),
    new ViewTask(2, 'test2', 'test2-description', [], 1, 0, 3, null, 1),
    new ViewTask(3, 'test3', 'test3-description', [], 0, 0, 1, 0, 1),
    new ViewTask(4, 'test4', 'test4-description', [], 1, 0, 2, 1, 1),
    new ViewTask(5, 'test5', 'test5-description', [], 0, 0, 3, 2, 1),
    new ViewTask(6, 'test6', 'test6-description', [], 1, 0, 1, 3, 1),
    new ViewTask(7, 'test7', 'test7-description', [], 0, 0, 2, 4, 1),
    new ViewTask(8, 'test8', 'test8-description', [], 2, 0, 3, 5, 1),
    new ViewTask(9, 'test9', 'test9-description', [], 2, 0, 1, 6, 1),
  ];

  const completeTaskListReverted: ViewTask[] = [
    new ViewTask(0, 'test0', 'test0-description', [], 1, 0, 1, null, 1),
    new ViewTask(1, 'test1', 'test1-description', [], 0, 0, 2, null, 1),
    new ViewTask(2, 'test2', 'test2-description', [], 1, 0, 3, null, 1),
    new ViewTask(3, 'test3', 'test3-description', [], 0, 0, 1, 0, 1),
    new ViewTask(4, 'test4', 'test4-description', [], 1, 0, 2, 1, 1),
    new ViewTask(5, 'test5', 'test5-description', [], 0, 0, 3, 2, 1),
    new ViewTask(6, 'test6', 'test6-description', [], 1, 0, 1, 3, 1),
    new ViewTask(7, 'test7', 'test7-description', [], 0, 0, 2, 4, 1),
    new ViewTask(8, 'test8', 'test8-description', [], 2, 0, 3, 5, 1),
    new ViewTask(9, 'test9', 'test9-description', [], 2, 0, 1, 6, 1),
  ];

  const simpleCompleteTaskList: ViewTask[] = [
    new ViewTask(3, 'test3', 'test3-description', [], 0, 0, 1, 0, 1),
    new ViewTask(0, 'test0', 'test0-description', [], 1, 0, 1, null, 1),
    new ViewTask(6, 'test6', 'test6-description', [], 1, 0, 1, 3, 1),
  ];

  const referenceOneStateTaskList: ViewTask[] = [
    new ViewTask(0, 'test0', 'test0-description', [], 1, 0, 1, null, 1),
    new ViewTask(3, 'test3', 'test3-description', [], 0, 0, 1, 0, 1),
    new ViewTask(6, 'test6', 'test6-description', [], 1, 0, 1, 3, 1),
    new ViewTask(9, 'test9', 'test9-description', [], 2, 0, 1, 6, 1),
  ];
  let revertedTasksForTesting: ViewTask[];
  let tasksForTesting: ViewTask[];
  let simpleTaskListForTesting: ViewTask[];

  beforeEach(() => TestBed.configureTestingModule({
    providers: [TaskBoardGeneralHelper],
  }));

  beforeEach(inject([TaskBoardGeneralHelper], (s: TaskBoardGeneralHelper) => {
    service = s;
    tasksForTesting = s.copyTaskList(completeTaskList);
    revertedTasksForTesting = s.copyTaskList(completeTaskListReverted.reverse());
    simpleTaskListForTesting = s.copyTaskList(simpleCompleteTaskList);
  }));

  it('should be created', () => {
    expect(service)
      .toBeTruthy();
  });

  it('orderTaskList: order list with tasks in multiple states by state and previousId', () => {
    const expectedOrderedList: ViewTask[] = [
      new ViewTask(0, 'test0', 'test0-description', [], 1, 0, 1, null, 1),
      new ViewTask(3, 'test3', 'test3-description', [], 0, 0, 1, 0, 1),
      new ViewTask(6, 'test6', 'test6-description', [], 1, 0, 1, 3, 1),
      new ViewTask(9, 'test9', 'test9-description', [], 2, 0, 1, 6, 1),
      new ViewTask(1, 'test1', 'test1-description', [], 0, 0, 2, null, 1),
      new ViewTask(4, 'test4', 'test4-description', [], 1, 0, 2, 1, 1),
      new ViewTask(7, 'test7', 'test7-description', [], 0, 0, 2, 4, 1),
      new ViewTask(2, 'test2', 'test2-description', [], 1, 0, 3, null, 1),
      new ViewTask(5, 'test5', 'test5-description', [], 0, 0, 3, 2, 1),
      new ViewTask(8, 'test8', 'test8-description', [], 2, 0, 3, 5, 1),
    ];
    const tasksToTest: ViewTask[] = service.orderTaskList(tasksForTesting);
    const tasksReversedToTest: ViewTask[] = service.orderTaskList(revertedTasksForTesting);
    expect(tasksToTest)
      .toEqual(expectedOrderedList);
    expect(tasksReversedToTest)
      .toEqual(expectedOrderedList);
  });

  it('orderTaskList: order simple list with tasks in one state by state and previousId', () => {
    const expected: ViewTask[] = [
      new ViewTask(0, 'test0', 'test0-description', [], 1, 0, 1, null, 1),
      new ViewTask(3, 'test3', 'test3-description', [], 0, 0, 1, 0, 1),
      new ViewTask(6, 'test6', 'test6-description', [], 1, 0, 1, 3, 1),
    ];
    const tasksToTest: ViewTask[] = service.orderTaskList(simpleTaskListForTesting);
    expect(tasksToTest)
      .toEqual(expected);
  });

  it('removeTaskAndUpdateTaskList - task list with only one state: delete last task in list', () => {
    const completeList: ViewTask[] = service.copyTaskList(referenceOneStateTaskList);
    const expectedList: ViewTask[] = [
      new ViewTask(0, 'test0', 'test0-description', [], 1, 0, 1, null, 1),
      new ViewTask(3, 'test3', 'test3-description', [], 0, 0, 1, 0, 1),
      new ViewTask(6, 'test6', 'test6-description', [], 1, 0, 1, 3, 1),
    ];
    const task: ViewTask = new ViewTask(9, 'test9', 'test9-description', [], 2, 0, 1, null, 1);
    const listToTest: ViewTask[] = service.removeTaskAndUpdateTaskList(completeList, task);
    expect(listToTest)
      .toEqual(expectedList);
  });

  it('addTaskAndUpdateTaskList - task list with only one state: Add task at beginning of the list', () => {
    const completeList: ViewTask[] = [
      new ViewTask(0, 'test0', 'test0-description', [], 1, 0, 1, null, 1),
      new ViewTask(3, 'test3', 'test3-description', [], 0, 0, 1, 0, 1),
      new ViewTask(6, 'test6', 'test6-description', [], 1, 0, 1, 3, 1),
    ];
    const expectedList: ViewTask[] = [
      new ViewTask(9, 'test9', 'test9-description', [], 2, 0, 1, null, 1),
      new ViewTask(0, 'test0', 'test0-description', [], 1, 0, 1, 9, 1),
      new ViewTask(3, 'test3', 'test3-description', [], 0, 0, 1, 0, 1),
      new ViewTask(6, 'test6', 'test6-description', [], 1, 0, 1, 3, 1),
    ];
    const task: ViewTask = new ViewTask(9, 'test9', 'test9-description', [], 2, 0, 1, null, 1);
    const listToTest: ViewTask[] = service.addTaskAndUpdateTaskList(completeList, task);
    expect(listToTest)
      .toEqual(expectedList);
  });

  it('addTaskAndUpdateTaskList - task list with only one state: Add task at end of the list', () => {
    const completeList: ViewTask[] = [
      new ViewTask(0, 'test0', 'test0-description', [], 1, 0, 1, null, 1),
      new ViewTask(3, 'test3', 'test3-description', [], 0, 0, 1, 0, 1),
      new ViewTask(6, 'test6', 'test6-description', [], 1, 0, 1, 3, 1),
    ];
    const expectedList: ViewTask[] = [
      new ViewTask(0, 'test0', 'test0-description', [], 1, 0, 1, null, 1),
      new ViewTask(3, 'test3', 'test3-description', [], 0, 0, 1, 0, 1),
      new ViewTask(6, 'test6', 'test6-description', [], 1, 0, 1, 3, 1),
      new ViewTask(9, 'test9', 'test9-description', [], 2, 0, 1, 6, 1),
    ];
    const task: ViewTask = new ViewTask(9, 'test9', 'test9-description', [], 2, 0, 1, 6, 1);
    const listToTest: ViewTask[] = service.addTaskAndUpdateTaskList(completeList, task);
    expect(listToTest)
      .toEqual(expectedList);
  });

  it(
    'addTaskAndUpdateTaskList - task list with only one state: Add task between tasks with id 3 and 0 of the list',
    () => {
      const completeList: ViewTask[] = [
        new ViewTask(0, 'test0', 'test0-description', [], 1, 0, 1, null, 1),
        new ViewTask(3, 'test3', 'test3-description', [], 0, 0, 1, 0, 1),
        new ViewTask(6, 'test6', 'test6-description', [], 1, 0, 1, 3, 1),
      ];
      const expectedList: ViewTask[] = [
        new ViewTask(0, 'test0', 'test0-description', [], 1, 0, 1, null, 1),
        new ViewTask(9, 'test9', 'test9-description', [], 2, 0, 1, 0, 1),
        new ViewTask(3, 'test3', 'test3-description', [], 0, 0, 1, 9, 1),
        new ViewTask(6, 'test6', 'test6-description', [], 1, 0, 1, 3, 1),
      ];
      const task: ViewTask = new ViewTask(9, 'test9', 'test9-description', [], 2, 0, 1, 0, 1);
      const listToTest: ViewTask[] = service.addTaskAndUpdateTaskList(completeList, task);
      expect(listToTest)
        .toEqual(expectedList);
    },
  );

  it(
    'addTaskAndUpdateTaskList - task list with multiple states: Add task between tasks with id 4 and 9 of the list',
    () => {
      const completeList: ViewTask[] = [
        new ViewTask(0, 'test0', 'test0-description', [], 1, 0, 1, null, 1),
        new ViewTask(3, 'test3', 'test3-description', [], 0, 0, 1, 0, 1),
        new ViewTask(6, 'test6', 'test6-description', [], 1, 0, 1, 3, 1),
        new ViewTask(9, 'test9', 'test9-description', [], 2, 0, 1, 6, 1),
        new ViewTask(4, 'test4', 'test4-description', [], 1, 0, 2, null, 1),
        new ViewTask(7, 'test7', 'test7-description', [], 0, 0, 2, 4, 1),
        new ViewTask(2, 'test2', 'test2-description', [], 1, 0, 3, null, 1),
        new ViewTask(5, 'test5', 'test5-description', [], 0, 0, 3, 2, 1),
        new ViewTask(8, 'test8', 'test8-description', [], 2, 0, 3, 5, 1),
      ];
      const expectedOrderedList: ViewTask[] = [
        new ViewTask(0, 'test0', 'test0-description', [], 1, 0, 1, null, 1),
        new ViewTask(3, 'test3', 'test3-description', [], 0, 0, 1, 0, 1),
        new ViewTask(6, 'test6', 'test6-description', [], 1, 0, 1, 3, 1),
        new ViewTask(9, 'test9', 'test9-description', [], 2, 0, 1, 6, 1),
        new ViewTask(1, 'test1', 'test1-description', [], 0, 0, 2, null, 1),
        new ViewTask(4, 'test4', 'test4-description', [], 1, 0, 2, 1, 1),
        new ViewTask(7, 'test7', 'test7-description', [], 0, 0, 2, 4, 1),
        new ViewTask(2, 'test2', 'test2-description', [], 1, 0, 3, null, 1),
        new ViewTask(5, 'test5', 'test5-description', [], 0, 0, 3, 2, 1),
        new ViewTask(8, 'test8', 'test8-description', [], 2, 0, 3, 5, 1),
      ];
      const task: ViewTask = new ViewTask(1, 'test1', 'test1-description', [], 0, 0, 2, null, 1);
      const listToTest: ViewTask[] = service.addTaskAndUpdateTaskList(completeList, task);
      expect(listToTest)
        .toEqual(expectedOrderedList);
    },
  );

  it('addTaskAndUpdateTaskList - task list with multiple states: Add task at beginning of the list', () => {
    const completeList: ViewTask[] = [
      new ViewTask(3, 'test3', 'test3-description', [], 0, 0, 1, null, 1),
      new ViewTask(6, 'test6', 'test6-description', [], 1, 0, 1, 3, 1),
      new ViewTask(9, 'test9', 'test9-description', [], 2, 0, 1, 6, 1),
      new ViewTask(1, 'test1', 'test1-description', [], 0, 0, 2, null, 1),
      new ViewTask(4, 'test4', 'test4-description', [], 1, 0, 2, 1, 1),
      new ViewTask(7, 'test7', 'test7-description', [], 0, 0, 2, 4, 1),
      new ViewTask(2, 'test2', 'test2-description', [], 1, 0, 3, null, 1),
      new ViewTask(5, 'test5', 'test5-description', [], 0, 0, 3, 2, 1),
      new ViewTask(8, 'test8', 'test8-description', [], 2, 0, 3, 5, 1),
    ];
    const expectedOrderedList: ViewTask[] = [
      new ViewTask(0, 'test0', 'test0-description', [], 1, 0, 1, null, 1),
      new ViewTask(3, 'test3', 'test3-description', [], 0, 0, 1, 0, 1),
      new ViewTask(6, 'test6', 'test6-description', [], 1, 0, 1, 3, 1),
      new ViewTask(9, 'test9', 'test9-description', [], 2, 0, 1, 6, 1),
      new ViewTask(1, 'test1', 'test1-description', [], 0, 0, 2, null, 1),
      new ViewTask(4, 'test4', 'test4-description', [], 1, 0, 2, 1, 1),
      new ViewTask(7, 'test7', 'test7-description', [], 0, 0, 2, 4, 1),
      new ViewTask(2, 'test2', 'test2-description', [], 1, 0, 3, null, 1),
      new ViewTask(5, 'test5', 'test5-description', [], 0, 0, 3, 2, 1),
      new ViewTask(8, 'test8', 'test8-description', [], 2, 0, 3, 5, 1),
    ];
    const task: ViewTask = new ViewTask(0, 'test0', 'test0-description', [], 1, 0, 1, null, 1);
    const listToTest: ViewTask[] = service.addTaskAndUpdateTaskList(completeList, task);
    expect(listToTest)
      .toEqual(expectedOrderedList);
  });

  it('addTaskAndUpdateTaskList - task list with multiple states: Add task at end of the list', () => {
    const completeList: ViewTask[] = [
      new ViewTask(0, 'test0', 'test0-description', [], 1, 0, 1, null, 1),
      new ViewTask(3, 'test3', 'test3-description', [], 0, 0, 1, 0, 1),
      new ViewTask(6, 'test6', 'test6-description', [], 1, 0, 1, 3, 1),
      new ViewTask(9, 'test9', 'test9-description', [], 2, 0, 1, 6, 1),
      new ViewTask(1, 'test1', 'test1-description', [], 0, 0, 2, null, 1),
      new ViewTask(4, 'test4', 'test4-description', [], 1, 0, 2, 1, 1),
      new ViewTask(7, 'test7', 'test7-description', [], 0, 0, 2, 4, 1),
      new ViewTask(2, 'test2', 'test2-description', [], 1, 0, 3, null, 1),
      new ViewTask(5, 'test5', 'test5-description', [], 0, 0, 3, 2, 1),
    ];
    const expectedOrderedList: ViewTask[] = [
      new ViewTask(0, 'test0', 'test0-description', [], 1, 0, 1, null, 1),
      new ViewTask(3, 'test3', 'test3-description', [], 0, 0, 1, 0, 1),
      new ViewTask(6, 'test6', 'test6-description', [], 1, 0, 1, 3, 1),
      new ViewTask(9, 'test9', 'test9-description', [], 2, 0, 1, 6, 1),
      new ViewTask(1, 'test1', 'test1-description', [], 0, 0, 2, null, 1),
      new ViewTask(4, 'test4', 'test4-description', [], 1, 0, 2, 1, 1),
      new ViewTask(7, 'test7', 'test7-description', [], 0, 0, 2, 4, 1),
      new ViewTask(2, 'test2', 'test2-description', [], 1, 0, 3, null, 1),
      new ViewTask(5, 'test5', 'test5-description', [], 0, 0, 3, 2, 1),
      new ViewTask(8, 'test8', 'test8-description', [], 2, 0, 3, 5, 1),
    ];
    const task: ViewTask = new ViewTask(8, 'test8', 'test8-description', [], 2, 0, 3, 5, 1);
    const listToTest: ViewTask[] = service.addTaskAndUpdateTaskList(completeList, task);
    expect(listToTest)
      .toEqual(expectedOrderedList);
  });

  it('addTaskAndUpdateTaskList - task list with multiple states: Add task between tasks with id 2 and 8 list', () => {
    const completeList: ViewTask[] = [
      new ViewTask(0, 'test0', 'test0-description', [], 1, 0, 1, null, 1),
      new ViewTask(3, 'test3', 'test3-description', [], 0, 0, 1, 0, 1),
      new ViewTask(6, 'test6', 'test6-description', [], 1, 0, 1, 3, 1),
      new ViewTask(9, 'test9', 'test9-description', [], 2, 0, 1, 6, 1),
      new ViewTask(1, 'test1', 'test1-description', [], 0, 0, 2, null, 1),
      new ViewTask(4, 'test4', 'test4-description', [], 1, 0, 2, 1, 1),
      new ViewTask(7, 'test7', 'test7-description', [], 0, 0, 2, 4, 1),
      new ViewTask(2, 'test2', 'test2-description', [], 1, 0, 3, null, 1),
      new ViewTask(8, 'test8', 'test8-description', [], 2, 0, 3, 2, 1),
    ];
    const expectedOrderedList: ViewTask[] = [
      new ViewTask(0, 'test0', 'test0-description', [], 1, 0, 1, null, 1),
      new ViewTask(3, 'test3', 'test3-description', [], 0, 0, 1, 0, 1),
      new ViewTask(6, 'test6', 'test6-description', [], 1, 0, 1, 3, 1),
      new ViewTask(9, 'test9', 'test9-description', [], 2, 0, 1, 6, 1),
      new ViewTask(1, 'test1', 'test1-description', [], 0, 0, 2, null, 1),
      new ViewTask(4, 'test4', 'test4-description', [], 1, 0, 2, 1, 1),
      new ViewTask(7, 'test7', 'test7-description', [], 0, 0, 2, 4, 1),
      new ViewTask(2, 'test2', 'test2-description', [], 1, 0, 3, null, 1),
      new ViewTask(5, 'test5', 'test5-description', [], 0, 0, 3, 2, 1),
      new ViewTask(8, 'test8', 'test8-description', [], 2, 0, 3, 5, 1),
    ];
    const task: ViewTask = new ViewTask(5, 'test5', 'test5-description', [], 0, 0, 3, 2, 1);
    const listToTest: ViewTask[] = service.addTaskAndUpdateTaskList(completeList, task);
    expect(listToTest)
      .toEqual(expectedOrderedList);
  });
});
