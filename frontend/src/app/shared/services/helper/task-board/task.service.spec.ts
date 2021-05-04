import { inject, TestBed } from '@angular/core/testing';
import { ViewTask } from '../../../model/ui/taskboard/view-task';

import { TaskService } from './task.service';

describe('TaskService', () => {
  let service: TaskService;
  const completeTaskList: ViewTask[] = [
    new ViewTask(0, 'test0', 'test0-description', null, 1, 0, 1, null, 1),
    new ViewTask(1, 'test1', 'test1-description', null, 0, 0, 2, null, 1),
    new ViewTask(2, 'test2', 'test2-description', null, 1, 0, 3, null, 1),
    new ViewTask(3, 'test3', 'test3-description', null, 0, 0, 1, 1, 1),
    new ViewTask(4, 'test4', 'test4-description', null, 1, 0, 2, 2, 1),
    new ViewTask(5, 'test5', 'test5-description', null, 0, 0, 3, 3, 1),
    new ViewTask(6, 'test6', 'test6-description', null, 1, 0, 1, 4, 1),
    new ViewTask(7, 'test7', 'test7-description', null, 0, 0, 2, 5, 1),
    new ViewTask(8, 'test8', 'test8-description', null, 2, 0, 3, 6, 1),
    new ViewTask(9, 'test9', 'test9-description', null, 2, 0, 1, 7, 1)
  ];
  let tasksForTesting: ViewTask[];

  beforeEach(() => TestBed.configureTestingModule({
    providers: [TaskService]
  }));

  beforeEach(inject([TaskService], (s: TaskService) => {
    service = s;
    tasksForTesting = s.copyTaskList(completeTaskList);
  }));

  it('should be created', () => {
    expect(service)
      .toBeTruthy();
  });

  it('getFirstTaskId: get id of the first task in array', () => {
    const id: number = service.getFirstTaskId(tasksForTesting);
    const expectedId: number = tasksForTesting[0].id;
    expect(expectedId)
      .toEqual(id);
  });

  it('copy tasklist correctly and the original tasks have not the same reference to the copy', () => {
    const copy: ViewTask[] = service.copyTaskList(tasksForTesting);

    expect(tasksForTesting)
      .toEqual(copy);
    copy[0].title = 'test0-updated';
    copy[0].assignedUserIds = ['1'];
    expect(tasksForTesting[0])
      .not
      .toBe(copy[0]);
  });

  it('By copying a null reference, then the result is null. By copying an empty array, then the result is an empty array', () => {
    const nullCopy: ViewTask[] = service.copyTaskList(null);
    const emptyCopy: ViewTask[] = service.copyTaskList([]);
    expect(nullCopy)
    .toBeNull();
    expect(emptyCopy)
    .toEqual([]);
  });
});
