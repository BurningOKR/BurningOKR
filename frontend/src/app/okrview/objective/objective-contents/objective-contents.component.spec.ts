import { TestBed } from '@angular/core/testing';
import {
  CUSTOM_ELEMENTS_SCHEMA,
  NO_ERRORS_SCHEMA
} from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { of, Subscription } from 'rxjs';
import { ObjectiveContentsComponent } from './objective-contents.component';
import { ObjectiveViewMapper } from '../../../shared/services/mapper/objective-view.mapper';
import { KeyResultMapper } from '../../../shared/services/mapper/key-result.mapper';
import { MatDialog, MatDialogModule } from '@angular/material';
import { ConfigurationManagerService } from '../../../core/settings/configuration-manager.service';
import { ViewKeyResult } from '../../../shared/model/ui/view-key-result';
import { Unit } from '../../../shared/model/api/unit.enum';
import { ViewObjective } from '../../../shared/model/ui/view-objective';

describe('ObjectiveContentsComponent', () => {
  const getObjectiveByIdMock: jest.Mock<any, any> = jest.fn();

  let fixture: any;
  let component: ObjectiveContentsComponent;

  beforeEach(() => {
    getObjectiveByIdMock.mockReset();

    TestBed.configureTestingModule({
      imports: [ FormsModule, ReactiveFormsModule, MatDialogModule ],
      declarations: [
        ObjectiveContentsComponent,
      ],
      schemas: [ CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA ],
      providers: [
        {
          provide: ObjectiveViewMapper,
          useValue: {
            getObjectiveById$: getObjectiveByIdMock
          }
        },
        { provide: KeyResultMapper, useValue: { } },
        MatDialog,
        { provide: ConfigurationManagerService, useValue: { } }
      ]
    })
      .overrideComponent(ObjectiveContentsComponent, {
    })
      .compileComponents();
    fixture = TestBed.createComponent(ObjectiveContentsComponent);
    component = fixture.debugElement.componentInstance;
    component.subscription = new Subscription();
    component.keyResultList = [new ViewKeyResult(1,  1, 1, 1, Unit.PERCENT, 'test title', ' test description', null, [])];
    component.objective = new ViewObjective(1, 'testName', 'testDescription', 'testRemark', 0, [], true, 0, 0, '', 0, undefined);
  });

  afterEach(() => {
    fixture.destroy();
  });

  it('should run #constructor()', () => {
     expect(component)
       .toBeTruthy();
  });

  it('should run refreshParentObjective()',  () => {
    getObjectiveByIdMock.mockReturnValue(of({}));
    component.objective.hasParentObjective = jest.fn()
      .mockReturnValue(true);

    component.refreshParentObjective();

    expect(component.objective.hasParentObjective)
      .toHaveBeenCalled();
    expect(getObjectiveByIdMock)
      .toHaveBeenCalled();
  });

  it('should set objective.parentObjective to null, if objective.parentObjectiveId is falsy',  () => {
    component.objective.parentObjectiveId = null;

    component.refreshParentObjective();

    expect(component.objective.hasParentObjective())
      .toBe(false);
    expect(component.parentObjective)
      .toBe(null);
  });

  it('should set objective.parentObjective to ViewObjective with equal'
  + ' id to objective.parentObjectiveId, if objective.parentObjectiveId is truthy',  () => {
    const expectedParentObjective: ViewObjective = new ViewObjective(
      5,
      'testName',
      'testDescription',
      'testRemark',
      0,
      [],
      true,
      0,
      0,
      '',
      0,
      undefined
    );
    component.objective.parentObjectiveId = 5;

    getObjectiveByIdMock.mockImplementation(objectiveId => {
      if (objectiveId === 5) {
        return of(expectedParentObjective);
      }

      return of(null);
    });

    component.refreshParentObjective();

    expect(component.parentObjective)
      .toEqual(expectedParentObjective);
  });

});
