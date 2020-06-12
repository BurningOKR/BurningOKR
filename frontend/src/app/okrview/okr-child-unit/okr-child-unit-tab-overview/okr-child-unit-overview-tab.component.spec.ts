// tslint:disable:rxjs-finnish

import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { OkrChildUnitOverviewTabComponent } from './okr-child-unit-overview-tab.component';
import { MaterialTestingModule } from '../../../testing/material-testing.module';
import { ObjectiveViewMapper } from '../../../shared/services/mapper/objective-view.mapper';
import { OkrUnitMapper } from '../../../shared/services/mapper/okr-unit.mapper.service';
import { MatDialog } from '@angular/material/dialog';
import { CdkDragDrop, CdkDropList } from '@angular/cdk/drag-drop';
import { ObjectiveComponent } from '../../objective/objective.component';
import { ObjectiveContentsComponent } from '../../objective/objective-contents/objective-contents.component';
import { RouterTestingModule } from '@angular/router/testing';
import { KeyresultComponent } from '../../keyresult/keyresult.component';
import { OkrDepartment } from '../../../shared/model/ui/OrganizationalUnit/okr-department';
import { of } from 'rxjs';
import { ContextRole } from '../../../shared/model/ui/context-role';
import { CycleState, CycleUnit } from '../../../shared/model/ui/cycle-unit';
import { ViewObjective } from '../../../shared/model/ui/view-objective';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-objective',
  template: '<p>Mock</p>'
})
class MockObjectiveComponent {
  @Input() objective: ViewObjective;
  @Input() objectiveList: ViewObjective[];
  @Input() currentUserRole: ContextRole;
  @Input() cycle: CycleUnit;
  @Input() listNumber: number;
}

const objectiveViewMapper: any = {
  getObjectivesForUnit$: jest.fn()
};

const unitMapper: any = {
  putOkrUnitObjectiveSequence$: jest.fn()
};

const matDialog: any = {

};

let department: OkrDepartment;
let currentUserRole: ContextRole;
let cycle: CycleUnit;

let objectiveList: Partial<ViewObjective>[] = [];

describe('ChildUnitOverviewTabComponent', () => {
  let component: OkrChildUnitOverviewTabComponent;
  let fixture: ComponentFixture<OkrChildUnitOverviewTabComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        OkrChildUnitOverviewTabComponent,
        MockObjectiveComponent,
        CdkDropList
      ],
      imports: [MaterialTestingModule, RouterTestingModule],
      providers: [
        { provide: ObjectiveViewMapper, useValue: objectiveViewMapper },
        { provide: OkrUnitMapper, useValue: unitMapper },
        { provide: MatDialog, useValue: matDialog }
      ]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    objectiveList = [{id: 1}, {id: 2}, {id: 3}];

    objectiveViewMapper.getObjectivesForUnit$.mockReset();
    objectiveViewMapper.getObjectivesForUnit$.mockReturnValue(of(objectiveList));

    unitMapper.putOkrUnitObjectiveSequence$.mockReset();
    unitMapper.putOkrUnitObjectiveSequence$.mockReturnValue(of(null));

    department = new OkrDepartment(
      1,
      'testDepartment',
      [],
      0,
      'test',
      'testMaster',
      'testSponsor',
      [],
      true,
      false
    );

    currentUserRole = new ContextRole();
    cycle = new CycleUnit(1, 'test', [0], new Date(), new Date(), CycleState.ACTIVE, true);
  });

  it('should create', () => {
    fixture = TestBed.createComponent(OkrChildUnitOverviewTabComponent);
    component = fixture.componentInstance;
    component.okrChildUnit = department;
    component.currentUserRole = currentUserRole;
    fixture.detectChanges();

    expect(component)
      .toBeTruthy();
  });

  it('dropObjective moves objective', () => {
    fixture = TestBed.createComponent(OkrChildUnitOverviewTabComponent);
    component = fixture.componentInstance;
    component.okrChildUnit = department;
    component.currentUserRole = currentUserRole;
    fixture.detectChanges();

    // tslint:disable-next-line:no-object-literal-type-assertion
    component.dropObjective({previousIndex: 0, currentIndex: 2} as CdkDragDrop<ViewObjective[]>);

    expect(objectiveList)
      .toEqual([{id: 2}, {id: 3}, {id: 1}]);
  });

  it('dropObjective on same index does not move objective', () => {
    fixture = TestBed.createComponent(OkrChildUnitOverviewTabComponent);
    component = fixture.componentInstance;
    component.okrChildUnit = department;
    component.currentUserRole = currentUserRole;
    fixture.detectChanges();

    // tslint:disable-next-line:no-object-literal-type-assertion
    component.dropObjective({previousIndex: 0, currentIndex: 0} as CdkDragDrop<ViewObjective[]>);

    expect(objectiveList)
      .toEqual([{id: 1}, {id: 2}, {id: 3}]);
  });

  it('dropObjective on very high index moves to back', () => {
    fixture = TestBed.createComponent(OkrChildUnitOverviewTabComponent);
    component = fixture.componentInstance;
    component.okrChildUnit = department;
    component.currentUserRole = currentUserRole;
    fixture.detectChanges();

    // tslint:disable-next-line:no-object-literal-type-assertion
    component.dropObjective({previousIndex: 0, currentIndex: 1337} as CdkDragDrop<ViewObjective[]>);

    expect(objectiveList)
      .toEqual([{id: 2}, {id: 3}, {id: 1}]);
  });

  it('dropObjective on very low index moves to front', () => {
    fixture = TestBed.createComponent(OkrChildUnitOverviewTabComponent);
    component = fixture.componentInstance;
    component.okrChildUnit = department;
    component.currentUserRole = currentUserRole;
    fixture.detectChanges();

    // tslint:disable-next-line:no-object-literal-type-assertion
    component.dropObjective({previousIndex: 2, currentIndex: -1337} as CdkDragDrop<ViewObjective[]>);

    expect(objectiveList)
      .toEqual([{id: 3}, {id: 1}, {id: 2}]);
  });

  it('dropObjective on index out of bounds moves last objective', () => {
    fixture = TestBed.createComponent(OkrChildUnitOverviewTabComponent);
    component = fixture.componentInstance;
    component.okrChildUnit = department;
    component.currentUserRole = currentUserRole;
    fixture.detectChanges();

    // tslint:disable-next-line:no-object-literal-type-assertion
    component.dropObjective({previousIndex: 1337, currentIndex: 0} as CdkDragDrop<ViewObjective[]>);

    expect(objectiveList)
      .toEqual([{id: 3}, {id: 1}, {id: 2}]);
  });

  it('dropObjective on negative index out of bounds moves last objective', () => {
    fixture = TestBed.createComponent(OkrChildUnitOverviewTabComponent);
    component = fixture.componentInstance;
    component.okrChildUnit = department;
    component.currentUserRole = currentUserRole;
    fixture.detectChanges();

    // tslint:disable-next-line:no-object-literal-type-assertion
    component.dropObjective({previousIndex: -1337, currentIndex: 2} as CdkDragDrop<ViewObjective[]>);

    expect(objectiveList)
      .toEqual([{id: 2}, {id: 3}, {id: 1}]);
  });

  // TODO (R.J. 05.06.20) MEHR TESTS
});
