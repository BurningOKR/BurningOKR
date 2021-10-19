import { TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { ObjectiveViewMapper } from './objective-view.mapper';
import { ObjectiveApiService } from '../api/objective-api.service';
import { ViewObjective } from '../../model/ui/view-objective';
import { ObjectiveDto } from '../../model/api/objective.dto';

const objectiveApiServiceMock: any = {
  getObjectiveById$: jest.fn(),
  getObjectivesForDepartment$: jest.fn(),
  postObjectiveForDepartment$: jest.fn(),
  postObjectiveForUnit$: jest.fn(),
  putObjective$: jest.fn(),
  putObjectiveKeyResultSequence$: jest.fn(),
  deleteObjective$: jest.fn()
};

let service: ObjectiveViewMapper;
let objective: ViewObjective;
let objectiveDto: ObjectiveDto;

describe('ObjectiveViewMapper', () => {
  beforeEach(() => TestBed.configureTestingModule({
    providers: [
      { provide: ObjectiveApiService, useValue: objectiveApiServiceMock }
    ]
  }));

  beforeEach(() => {
    objective = new ViewObjective(1, 'ObjectiveName', 'ObjectiveDesc', 'remark', 100, [], true, 2, 3, 'contactPerson', 0, [], 'review');
    objectiveDto = {
      id: 1,
      title: 'ObjectiveName',
      description: 'ObjectiveDesc',
      remark: 'remark',
      keyResultIds: [],
      isActive: true,
      parentObjectiveId: 2,
      parentUnitId: 3,
      contactPersonId: 'contactPerson',
      review: 'review',
      noteIds: [],
      subObjectiveIds: []
    };

    objectiveApiServiceMock.getObjectiveById$.mockReset();
    objectiveApiServiceMock.getObjectiveById$.mockReturnValue(of(objectiveDto));
    objectiveApiServiceMock.getObjectivesForDepartment$.mockReset();
    objectiveApiServiceMock.getObjectivesForDepartment$.mockReturnValue(of([objectiveDto]));
    objectiveApiServiceMock.postObjectiveForDepartment$.mockReset();
    objectiveApiServiceMock.postObjectiveForDepartment$.mockReturnValue(of(objectiveDto));
    objectiveApiServiceMock.postObjectiveForUnit$.mockReset();
    objectiveApiServiceMock.postObjectiveForUnit$.mockReturnValue(of(objectiveDto));
    objectiveApiServiceMock.putObjective$.mockReset();
    objectiveApiServiceMock.putObjective$.mockReturnValue(of(objectiveDto));
    objectiveApiServiceMock.putObjectiveKeyResultSequence$.mockReset();
    objectiveApiServiceMock.putObjectiveKeyResultSequence$.mockReturnValue(of([]));
    objectiveApiServiceMock.deleteObjective$.mockReset();
    objectiveApiServiceMock.deleteObjective$.mockReturnValue(of(true));
  });

  it('should be created', () => {
    service = TestBed.inject(ObjectiveViewMapper);

    expect(service)
      .toBeTruthy();
  });

  it('getObjectiveById$ should map', done => {
    service = TestBed.inject(ObjectiveViewMapper);

    service.getObjectiveById$(1)
      .subscribe((viewObjective: ViewObjective) => {
        expect(viewObjective)
          .toEqual(objective);
        done();
      });
  });

  it('getObjectiveById$ should call service', done => {
    service = TestBed.inject(ObjectiveViewMapper);

    service.getObjectiveById$(1)
      .subscribe(() => {
        expect(objectiveApiServiceMock.getObjectiveById$)
          .toHaveBeenCalledWith(1);
        done();
      });
  });

  it('getObjectiveForDepartment$ should map', done => {
    service = TestBed.inject(ObjectiveViewMapper);

    service.getObjectivesForDepartment$(1)
      .subscribe((viewObjective: ViewObjective[]) => {
        expect(viewObjective[0])
          .toEqual(objective);
        done();
      });
  });

  it('getObjectiveForDepartment$ should call service', done => {
    service = TestBed.inject(ObjectiveViewMapper);

    service.getObjectivesForDepartment$(1)
      .subscribe(() => {
        expect(objectiveApiServiceMock.getObjectivesForDepartment$)
          .toHaveBeenCalledWith(1);
        done();
      });
  });

  it('postObjectiveForDepartment$ should map', done => {
    service = TestBed.inject(ObjectiveViewMapper);

    service.postObjectiveForDepartment$(1, objective)
      .subscribe((viewObjective: ViewObjective) => {
        expect(viewObjective)
          .toEqual(objective);
        done();
      });
  });

  it('postObjectiveForDepartment$ should call service', done => {
    service = TestBed.inject(ObjectiveViewMapper);

    service.postObjectiveForDepartment$(1, objective)
      .subscribe(() => {
        expect(objectiveApiServiceMock.postObjectiveForDepartment$)
          .toHaveBeenCalled();
        done();
      });
  });

  it('postObjectiveForDepartment$ should map viewObjective to objectiveDto', done => {
    service = TestBed.inject(ObjectiveViewMapper);

    service.postObjectiveForDepartment$(1, objective)
      .subscribe(() => {
        expect(objectiveApiServiceMock.postObjectiveForDepartment$)
          .toHaveBeenCalledWith(1, {
            title: objective.name,
            description: objective.description,
            remark: objective.remark,
            isActive: objective.isActive,
            noteIds: objective.commentIdList,
            parentUnitId: objective.parentUnitId,
            parentObjectiveId: objective.parentObjectiveId,
            contactPersonId: objective.contactPersonId
          });
        done();
      });
  });

  it('postObjectiveForUnit$ should map', done => {
    service = TestBed.inject(ObjectiveViewMapper);

    service.postObjectiveForUnit$(1, objective)
      .subscribe((viewObjective: ViewObjective) => {
        expect(viewObjective)
          .toEqual(objective);
        done();
      });
  });

  it('postObjectiveForUnit$ should call service', done => {
    service = TestBed.inject(ObjectiveViewMapper);

    service.postObjectiveForUnit$(1, objective)
      .subscribe(() => {
        expect(objectiveApiServiceMock.postObjectiveForUnit$)
          .toHaveBeenCalled();
        done();
      });
  });

  it('postObjectiveForUnit$ should map viewObjective to objectiveDto', done => {
    service = TestBed.inject(ObjectiveViewMapper);

    service.postObjectiveForUnit$(1, objective)
      .subscribe(() => {
        expect(objectiveApiServiceMock.postObjectiveForUnit$)
          .toHaveBeenCalledWith(1, {
            title: objective.name,
            description: objective.description,
            remark: objective.remark,
            isActive: objective.isActive,
            noteIds: objective.commentIdList,
            parentUnitId: objective.parentUnitId,
            parentObjectiveId: objective.parentObjectiveId,
            contactPersonId: objective.contactPersonId
          });
        done();
      });
  });

  it('putObjective$ should map', done => {
    service = TestBed.inject(ObjectiveViewMapper);

    service.putObjective$(objective)
      .subscribe((viewObjective: ViewObjective) => {
        expect(viewObjective)
          .toEqual(objective);
        done();
      });
  });

  it('putObjective$ should call service', done => {
    service = TestBed.inject(ObjectiveViewMapper);

    service.putObjective$(objective)
      .subscribe(() => {
        expect(objectiveApiServiceMock.putObjective$)
          .toHaveBeenCalled();
        done();
      });
  });

  it('putObjective$ should map viewObjective to objectiveDto', done => {
    service = TestBed.inject(ObjectiveViewMapper);

    service.putObjective$(objective)
      .subscribe(() => {
        expect(objectiveApiServiceMock.putObjective$)
          .toHaveBeenCalledWith({
            title: objective.name,
            description: objective.description,
            remark: objective.remark,
            isActive: objective.isActive,
            noteIds: objective.commentIdList,
            parentUnitId: objective.parentUnitId,
            parentObjectiveId: objective.parentObjectiveId,
            contactPersonId: objective.contactPersonId
          }, 1);
        done();
      });
  });

  it('putObjectiveKeyResultSequence$ should call service', done => {
    service = TestBed.inject(ObjectiveViewMapper);

    service.putObjectiveKeyResultSequence$(1, [])
      .subscribe(() => {
        expect(objectiveApiServiceMock.putObjectiveKeyResultSequence$)
          .toHaveBeenCalledWith(1, []);
        done();
      });
  });

  it('deleteObjective$ should call service', done => {
    service = TestBed.inject(ObjectiveViewMapper);

    service.deleteObjective$(1)
      .subscribe(() => {
        expect(objectiveApiServiceMock.deleteObjective$)
          .toHaveBeenCalledWith(1);
        done();
      });
  });
});
