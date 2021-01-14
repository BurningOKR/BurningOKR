import { DepartmentDescriptionMapper } from './department-description-mapper';
import { TestBed } from '@angular/core/testing';
import { OkrDepartmentDescriptionDto } from '../../model/api/OkrUnit/okr-department-description.dto';
import { DepartmentDescriptionApiService } from '../api/department-description-api.service';
import { OkrDepartmentDescription } from '../../model/ui/OrganizationalUnit/okr-department-description';
import { of } from 'rxjs';

const departmentDescriptionApiServiceMock: any = {
  getDepartmentDescriptionById$: jest.fn(),
  postDepartmentDescription$: jest.fn(),
  putDepartmentDescription$: jest.fn()
};

let service: DepartmentDescriptionMapper;
let description: OkrDepartmentDescription;
let descriptionDto: OkrDepartmentDescriptionDto;

describe('DepartmentDescriptionMapper', () => {
  beforeEach(() => TestBed.configureTestingModule({
    providers: [
      { provide: DepartmentDescriptionApiService, useValue: departmentDescriptionApiServiceMock }
    ]
  }));

  beforeEach(() => {
    description = new OkrDepartmentDescription(1, 'DescriptionName', '2', ['2', '3', '4'], ['3', '4'], 'acceptanceCriteria',
      'Contributes To', 'Delimination', new Date(1, 1, 2021), 'Dependencies', 'Resources', 'Handover Plan');

    descriptionDto = {
      descriptionId: 1,
      name: 'DescriptionName',
      initiatorId: '2',
      startTeam: ['2', '3', '4'],
      stakeholders: ['3', '4'],
      acceptanceCriteria: 'acceptanceCriteria',
      contributesTo: 'Contributes To',
      delimitation: 'Delimination',
      beginning: new Date(1, 1, 2021),
      dependencies: 'Dependencies',
      resources: 'Resources',
      handoverPlan: 'Handover Plan'
    };

    departmentDescriptionApiServiceMock.getDepartmentDescriptionById$.mockReset();
    departmentDescriptionApiServiceMock.getDepartmentDescriptionById$.mockReturnValue(of(descriptionDto));
    departmentDescriptionApiServiceMock.putDepartmentDescription$.mockReset();
    departmentDescriptionApiServiceMock.putDepartmentDescription$.mockReturnValue(of(descriptionDto));
  });

  it('should be created', () => {
    service = TestBed.get(DepartmentDescriptionMapper);

    expect(service)
      .toBeTruthy();
  });

  it('getDepartmentDescriptionById$ should map', done => {
    service.getDepartmentDescriptionById$(1)
      .subscribe((departmentDescription: OkrDepartmentDescription) => {
        expect(departmentDescription)
          .toEqual(description);
        done();
      });
  });

  it('putDepartmentDescription$ should map', done => {
    service = TestBed.get(DepartmentDescriptionMapper);

    service.putDepartmentDescription$(description)
      .subscribe((departmentDescription: OkrDepartmentDescription) => {
        expect(departmentDescription)
          .toEqual(description);
        done();
      });
  });

  it('putDepartmentDescription$ should call service', done => {
    service = TestBed.get(DepartmentDescriptionMapper);

    service.putDepartmentDescription$(description)
      .subscribe(() => {
        expect(departmentDescriptionApiServiceMock.putDepartmentDescription$)
          .toHaveBeenCalled();
        done();
      });
  });

  it('putDepartmentDescription$ should map description to descriptionDto', done => {
    service = TestBed.get(DepartmentDescriptionMapper);

    service.putDepartmentDescription$(description)
      .subscribe(() => {
        expect(departmentDescriptionApiServiceMock.putDepartmentDescription$)
          .toHaveBeenCalledWith(1, {
            descriptionId: 1,
            name: 'DescriptionName',
            initiatorId: '2',
            startTeam: ['2', '3', '4'],
            stakeholders: ['3', '4'],
            acceptanceCriteria: 'acceptanceCriteria',
            contributesTo: 'Contributes To',
            delimitation: 'Delimination',
            beginning: new Date(1, 1, 2021),
            dependencies: 'Dependencies',
            resources: 'Resources',
            handoverPlan: 'Handover Plan'
          });
        done();
      });
  });
  // TODO: P.B. 07.01.2021 Write more Tests
});
