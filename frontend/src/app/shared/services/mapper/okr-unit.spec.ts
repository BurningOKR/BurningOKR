import { TestBed } from '@angular/core/testing';
import { OkrUnitService } from './okr-unit.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { OkrUnitApiService } from '../api/okr-unit-api.service';
import { of } from 'rxjs';
import { OkrDepartmentDto } from '../../model/api/OkrUnit/okr-department.dto';
import { OkrDepartment } from '../../model/ui/OrganizationalUnit/okr-department';
import { OkrChildUnit } from '../../model/ui/OrganizationalUnit/okr-child-unit';
import { OkrBranchDto } from '../../model/api/OkrUnit/okr-branch.dto';
import { OkrBranch } from '../../model/ui/OrganizationalUnit/okr-branch';
import { UnitType } from '../../model/api/OkrUnit/unit-type.enum';

const okrUnitApiService: any = {
  getOkrChildUnitById$: jest.fn(),
  putOkrChildUnit$: jest.fn(),
  deleteOkrChildUnit$: jest.fn(),
};

let departmentDto: OkrDepartmentDto;
let okrBranchDto: OkrBranchDto;
let departmentUnit: OkrDepartment;

let okrBranch: OkrBranch;

describe('OkrUnitService', () => {
  beforeEach(() => TestBed.configureTestingModule({
    imports: [HttpClientTestingModule],
    providers: [
      OkrUnitService,
      { provide: OkrUnitApiService, useValue: okrUnitApiService },
    ],
  }));

  beforeEach(() => {
    okrUnitApiService.getOkrChildUnitById$.mockReset();
    okrUnitApiService.putOkrChildUnit$.mockReset();
    okrUnitApiService.deleteOkrChildUnit$.mockReset();

    departmentDto = {
      okrUnitId: 1,
      unitName: 'testName',
      photo: 'base64',
      label: 'test',
      isActive: true,
      objectiveIds: [1, 2, 3],
      okrMasterId: 'testMaster',
      okrMemberIds: ['member1', 'member2'],
      okrTopicSponsorId: 'testSponsor',
      parentUnitId: 2,
      isParentUnitABranch: true,
      __okrUnitType: UnitType.DEPARTMENT,
    };

    departmentUnit = {
      id: 1,
      name: 'testName',
      photo: 'base64',
      label: 'test',
      isActive: true,
      objectives: [1, 2, 3],
      okrMasterId: 'testMaster',
      okrMemberIds: ['member1', 'member2'],
      okrTopicSponsorId: 'testSponsor',
      parentUnitId: 2,
      isParentUnitABranch: true,
      type: UnitType.DEPARTMENT,
    };

    okrBranchDto = {
      __okrUnitType: UnitType.BRANCH,
      okrUnitId: 2,
      unitName: 'testName2',
      photo: 'base64',
      label: 'testLabel',
      isActive: true,
      okrChildUnitIds: [1],
      parentUnitId: 0,
      objectiveIds: [4, 5, 6],
      isParentUnitABranch: true,
    };

    okrBranch = {
      type: UnitType.BRANCH,
      id: 2,
      name: 'testName2',
      photo: 'base64',
      label: 'testLabel',
      isActive: true,
      okrChildUnitIds: [1],
      parentUnitId: 0,
      objectives: [4, 5, 6],
      isParentUnitABranch: true,
    };
  });

  it('should be created', () => {
    const service: OkrUnitService = TestBed.inject(OkrUnitService);
    expect(service)
      .toBeTruthy();
  });

  it('getOkrChildUnitById$ returns Departments', done => {
    okrUnitApiService.getOkrChildUnitById$.mockReturnValue(of(departmentDto));

    const service: OkrUnitService = TestBed.inject(OkrUnitService);

    service.getOkrChildUnitById$(1)
      .subscribe((okrChildUnit: OkrChildUnit) => {
        expect(okrChildUnit.type === UnitType.DEPARTMENT)
          .toBeTruthy();
        done();
      });
  });

  it('getOkrChildUnitById$ maps DepartmentDtos to Departments', done => {
    okrUnitApiService.getOkrChildUnitById$.mockReturnValue(of(departmentDto));

    const service: OkrUnitService = TestBed.inject(OkrUnitService);

    service.getOkrChildUnitById$(1)
      .subscribe((okrChildUnit: OkrChildUnit) => {
        if (okrChildUnit.type === UnitType.DEPARTMENT) {
          expect(okrChildUnit)
            .toEqual(departmentUnit);
        } else {
          fail('Did not get a OkrDepartment');
        }
        done();
      });
  });

  it('getOkrChildUnitById$ returns OkrBranch', done => {
    okrUnitApiService.getOkrChildUnitById$.mockReturnValue(of(okrBranchDto));

    const service: OkrUnitService = TestBed.inject(OkrUnitService);

    service.getOkrChildUnitById$(1)
      .subscribe((okrChildUnit: OkrChildUnit) => {
        expect(okrChildUnit.type === UnitType.BRANCH)
          .toBeTruthy();
        done();
      });
  });

  it('getOkrChildUnitById$ maps OkrBranchDtos to OkrBranch', done => {
    okrUnitApiService.getOkrChildUnitById$.mockReturnValue(of(okrBranchDto));

    const service: OkrUnitService = TestBed.inject(OkrUnitService);

    service.getOkrChildUnitById$(1)
      .subscribe((okrChildUnit: OkrChildUnit) => {
        if (okrChildUnit.type === UnitType.BRANCH) {
          expect(okrChildUnit)
            .toEqual(okrBranch);
        } else {
          fail('Did not get a OkrBranch');
        }
        done();
      });
  });

  it('getOkrChildUnitById$ does not map null', done => {
    okrUnitApiService.getOkrChildUnitById$.mockReturnValue(of(null));

    const service: OkrUnitService = TestBed.inject(OkrUnitService);

    service.getOkrChildUnitById$(1)
      .subscribe((okrChildUnit: OkrChildUnit) => {
        expect(okrChildUnit)
          .toBeFalsy();
        done();
      });
  });

  it('putOkrChildUnit$ maps Departments to dtos', done => {
    okrUnitApiService.putOkrChildUnit$.mockReturnValue(of(departmentDto));

    const service: OkrUnitService = TestBed.inject(OkrUnitService);

    service.putOkrChildUnit$(departmentUnit)
      .subscribe(() => {
        expect(okrUnitApiService.putOkrChildUnit$)
          .toHaveBeenCalledWith(1, departmentDto, true);
        done();
      });
  });

  it('putOkrChildUnit$ maps OkrBranch to dtos', done => {
    okrUnitApiService.putOkrChildUnit$.mockReturnValue(of(okrBranchDto));

    const service: OkrUnitService = TestBed.inject(OkrUnitService);

    service.putOkrChildUnit$(okrBranch)
      .subscribe(() => {
        expect(okrUnitApiService.putOkrChildUnit$)
          .toHaveBeenCalledWith(2, okrBranchDto, true);
        done();
      });
  });

  it('putOkrChildUnit$ does not map null', done => {
    okrUnitApiService.putOkrChildUnit$.mockReturnValue(of(null));

    const service: OkrUnitService = TestBed.inject(OkrUnitService);

    service.putOkrChildUnit$(okrBranch)
      .subscribe((okrChildUnit: OkrChildUnit) => {
        expect(okrChildUnit)
          .toBeFalsy();
        done();
      });
  });

  it('putOkrChildUnit$ does not accept null', done => {
    okrUnitApiService.putOkrChildUnit$.mockReturnValue(of(null));

    const test: () => void = () => {
      const service: OkrUnitService = TestBed.inject(OkrUnitService);

      service.putOkrChildUnit$(null)
        .subscribe(() => {
          fail('No Error has been thrown, although an error should have been thrown.');
          done();
        });
    };

    expect(test)
      .toThrow(TypeError);
    done();

  });

  it('deleteOkrChildUnit$ deletes Department', done => {
    okrUnitApiService.deleteOkrChildUnit$.mockReturnValue(of(true));

    const service: OkrUnitService = TestBed.inject(OkrUnitService);

    service.deleteOkrChildUnit$(departmentUnit)
      .subscribe((deleted: boolean) => {
        expect(deleted)
          .toBeTruthy();
        done();
      });
  });

  it('deleteOkrChildUnit$ deletes OkrBranch', done => {
    okrUnitApiService.deleteOkrChildUnit$.mockReturnValue(of(true));

    const service: OkrUnitService = TestBed.inject(OkrUnitService);

    service.deleteOkrChildUnit$(okrBranch)
      .subscribe((deleted: boolean) => {
        expect(deleted)
          .toBeTruthy();
        done();
      });
  });

  it('deleteOkrChildUnit$ does not delete null', done => {
    okrUnitApiService.deleteOkrChildUnit$.mockReturnValue(of(true));

    const service: OkrUnitService = TestBed.inject(OkrUnitService);

    // eslint-disable-next-line
    const test = () => {
      service.deleteOkrChildUnit$(null)
        .subscribe(() => {
          fail('No Error has been thrown, although an error should have been thrown.');
          done();
        });
    };

    expect(test)
      .toThrow(TypeError);
    done();
  });
});
