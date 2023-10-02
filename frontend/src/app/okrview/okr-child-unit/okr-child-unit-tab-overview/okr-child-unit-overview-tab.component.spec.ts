/* eslint-disable */

import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { OkrChildUnitOverviewTabComponent } from './okr-child-unit-overview-tab.component';
import { MaterialTestingModule } from '../../../testing/material-testing.module';
import { ObjectiveViewMapper } from '../../../shared/services/mapper/objective-view.mapper';
import { OkrUnitService } from '../../../shared/services/mapper/okr-unit.service';
import { MatDialog } from '@angular/material/dialog';
import { CdkDragDrop, CdkDropList } from '@angular/cdk/drag-drop';
import { RouterTestingModule } from '@angular/router/testing';
import { OkrDepartment } from '../../../shared/model/ui/OrganizationalUnit/okr-department';
import { of } from 'rxjs';
import { ContextRole } from '../../../shared/model/ui/context-role';
import { CycleState, CycleUnit } from '../../../shared/model/ui/cycle-unit';
import { ViewObjective } from '../../../shared/model/ui/view-objective';
import { Component, Input } from '@angular/core';
import { SharedModule } from '../../../shared/shared.module';
import { UnitType } from '../../../shared/model/api/OkrUnit/unit-type.enum';

@Component({
  selector: 'app-objective',
  template: '<p>Mock</p>',
})
class MockObjectiveComponent {
  @Input() objective: ViewObjective;
  @Input() objectiveList: ViewObjective[];
  @Input() currentUserRole: ContextRole;
  @Input() cycle: CycleUnit;
  @Input() listNumber: number;
}

const objectiveViewMapper: any = {
  getObjectivesForUnit$: jest.fn(),
};

const unitMapper: any = {
  putOkrUnitObjectiveSequence$: jest.fn(),
};

const matDialog: any = {};

let department: OkrDepartment;
let currentUserRole: ContextRole;
let cycle: CycleUnit;

let objectiveList: Partial<ViewObjective>[] = [];

describe('ChildUnitOverviewTabComponent', () => {
  let component: OkrChildUnitOverviewTabComponent;
  let fixture: ComponentFixture<OkrChildUnitOverviewTabComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [
        OkrChildUnitOverviewTabComponent,
        MockObjectiveComponent,
        CdkDropList,
      ],
      imports: [MaterialTestingModule, RouterTestingModule, SharedModule],
      providers: [
        { provide: ObjectiveViewMapper, useValue: objectiveViewMapper },
        { provide: OkrUnitService, useValue: unitMapper },
        { provide: MatDialog, useValue: matDialog },
      ],
    })
      .compileComponents();
  }));

  beforeEach(() => {
    objectiveList = [{ id: 1 }, { id: 2 }, { id: 3 }];

    objectiveViewMapper.getObjectivesForUnit$.mockReset();
    objectiveViewMapper.getObjectivesForUnit$.mockReturnValue(of(objectiveList));

    unitMapper.putOkrUnitObjectiveSequence$.mockReset();
    unitMapper.putOkrUnitObjectiveSequence$.mockReturnValue(of(null));

    department = {
      id: 1,
      name: 'testDepartment',
      photo: 'base64',
      objectives: [],
      parentUnitId: 0,
      label: 'test',
      okrMasterId: 'testMaster',
      okrTopicSponsorId: 'testSponsor',
      okrMemberIds: [],
      isActive: true,
      isParentUnitABranch: false,
      type: UnitType.DEPARTMENT,
    };

    currentUserRole = new ContextRole();
    cycle = new CycleUnit(1, 'test', [0], new Date(), new Date(), CycleState.ACTIVE, true);

    fixture = TestBed.createComponent(OkrChildUnitOverviewTabComponent);
    component = fixture.componentInstance;
    component.okrChildUnit = department;
    component.currentUserRole = currentUserRole;
    component.objectiveList = [new ViewObjective(
      1,
      'testName',
      'testDescription',
      '',
      0,
      [],
      true,
      null,
      123,
      '321',
      0,
      [],
    )];
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });

  it('dropObjective moves objective', () => {

    // eslint-disable-next-line @typescript-eslint/consistent-type-assertions
    component.dropObjective({ previousIndex: 0, currentIndex: 2 } as CdkDragDrop<ViewObjective[]>);

    expect(objectiveList)
      .toEqual([{ id: 2 }, { id: 3 }, { id: 1 }]);
  });

  it('dropObjective on same index does not move objective', () => {

    // eslint-disable-next-line @typescript-eslint/consistent-type-assertions
    component.dropObjective({ previousIndex: 0, currentIndex: 0 } as CdkDragDrop<ViewObjective[]>);

    expect(objectiveList)
      .toEqual([{ id: 1 }, { id: 2 }, { id: 3 }]);
  });

  it('dropObjective on very high index moves to back', () => {

    // eslint-disable-next-line @typescript-eslint/consistent-type-assertions
    component.dropObjective({ previousIndex: 0, currentIndex: 1337 } as CdkDragDrop<ViewObjective[]>);

    expect(objectiveList)
      .toEqual([{ id: 2 }, { id: 3 }, { id: 1 }]);
  });

  it('dropObjective on very low index moves to front', () => {

    // eslint-disable-next-line @typescript-eslint/consistent-type-assertions
    component.dropObjective({ previousIndex: 2, currentIndex: -1337 } as CdkDragDrop<ViewObjective[]>);

    expect(objectiveList)
      .toEqual([{ id: 3 }, { id: 1 }, { id: 2 }]);
  });

  it('dropObjective on index out of bounds moves last objective', () => {

    // eslint-disable-next-line @typescript-eslint/consistent-type-assertions
    component.dropObjective({ previousIndex: 1337, currentIndex: 0 } as CdkDragDrop<ViewObjective[]>);

    expect(objectiveList)
      .toEqual([{ id: 3 }, { id: 1 }, { id: 2 }]);
  });

  it('dropObjective on negative index out of bounds moves last objective', () => {

    // eslint-disable-next-line @typescript-eslint/consistent-type-assertions
    component.dropObjective({ previousIndex: -1337, currentIndex: 2 } as CdkDragDrop<ViewObjective[]>);

    expect(objectiveList)
      .toEqual([{ id: 2 }, { id: 3 }, { id: 1 }]);
  });

  it('moveObjectiveToTop moves objective to top', () => {

    // eslint-disable-next-line @typescript-eslint/consistent-type-assertions
    component.moveObjectiveToTop(objectiveList[2] as ViewObjective);

    expect(objectiveList)
      .toEqual([{ id: 3 }, { id: 1 }, { id: 2 }]);
  });

  it('moveObjectiveToTop objective already at top, does not change anything', () => {

    // eslint-disable-next-line @typescript-eslint/consistent-type-assertions
    component.moveObjectiveToTop(objectiveList[0] as ViewObjective);

    expect(objectiveList)
      .toEqual([{ id: 1 }, { id: 2 }, { id: 3 }]);
  });

  it('moveObjectiveToBottom moves objective to bottom', () => {

    // eslint-disable-next-line @typescript-eslint/consistent-type-assertions
    component.moveObjectiveToBottom(objectiveList[0] as ViewObjective);

    expect(objectiveList)
      .toEqual([{ id: 2 }, { id: 3 }, { id: 1 }]);
  });

  it('moveObjectiveToBottom objective already at bottom, does not change anything', () => {

    // eslint-disable-next-line @typescript-eslint/consistent-type-assertions
    component.moveObjectiveToBottom(objectiveList[2] as ViewObjective);

    expect(objectiveList)
      .toEqual([{ id: 1 }, { id: 2 }, { id: 3 }]);
  });

  it('queryUpdatedObjectiveOrder puts child unit', () => {
    component.queryUpdatedObjectiveOrder();
    expect(unitMapper.putOkrUnitObjectiveSequence$)
      .toHaveBeenCalledWith(department.id, [1, 2, 3]);
  });

  it('calculateDepartmentOrderedIdList converts list', () => {
    const list: number[] = component.calculateDepartmentOrderedIdList();
    expect(list)
      .toEqual([1, 2, 3]);
  });

  it('calculateDepartmentOrderedIdList returns empty list, when objectives are empty', () => {
    objectiveViewMapper.getObjectivesForUnit$.mockReturnValue(of([]));

    fixture = TestBed.createComponent(OkrChildUnitOverviewTabComponent);
    component = fixture.componentInstance;
    component.okrChildUnit = department;
    component.currentUserRole = currentUserRole;
    fixture.detectChanges();

    const list: number[] = component.calculateDepartmentOrderedIdList();
    expect(list)
      .toEqual([]);
  });

  it('should add new objective to bottom of objectivelist', () => {
    const newObjective: ViewObjective = new ViewObjective(
      2,
      'TestObjectiveAdd 2',
      'TestObjective for addingFeature',
      '',
      1,
      [],
      true,
      null,
      23,
      '321',
      0,
      [],
    );

    component.onObjectiveAdded(newObjective);

    expect(component.objectiveList.pop()).toEqual(newObjective);
    expect(component.okrChildUnit.objectives.pop()).toEqual(newObjective.id);
  });
});
