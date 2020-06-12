import { TestBed } from '@angular/core/testing';

import { OkrUnitMapper } from './okr-unit.mapper.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { OkrUnitApiService } from '../api/okr-unit-api.service';
import { of } from 'rxjs';
import { OkrDepartmentDto } from '../../model/api/OkrUnit/okrDepartment.dto';
import { OkrDepartment } from '../../model/ui/OrganizationalUnit/okr-department';
import { OkrChildUnit } from '../../model/ui/OrganizationalUnit/okr-child-unit';
import { OkrBranchDto } from '../../model/api/OkrUnit/okr-branch.dto';
import { OkrBranch } from '../../model/ui/OrganizationalUnit/okr-branch';
import any = jasmine.any;

const okrUnitApiService: any = {
  getOkrChildUnitById$: jest.fn(),
  putOkrChildUnit$: jest.fn(),
  deleteOkrChildUnit$: jest.fn()
};

let departmentDto: OkrDepartmentDto;
let okrBranchDto: OkrBranchDto;
let departmentUnit: OkrDepartment;

let okrBranch: OkrBranch;

describe('OkrUnitMapper', () => {
  beforeEach(() => TestBed.configureTestingModule({
    imports: [HttpClientTestingModule],
    providers: [
      OkrUnitMapper,
      { provide: OkrUnitApiService, useValue: okrUnitApiService }
    ]
  }));

  beforeEach(() => {
    okrUnitApiService.getOkrChildUnitById$.mockReset();
    okrUnitApiService.putOkrChildUnit$.mockReset();
    okrUnitApiService.deleteOkrChildUnit$.mockReset();

    departmentDto = new OkrDepartmentDto();
    departmentDto.okrUnitId = 1;
    departmentDto.unitName = 'testName';
    departmentDto.label = 'test';
    departmentDto.isActive = true;
    departmentDto.objectiveIds = [1, 2, 3];
    departmentDto.okrMasterId = 'testMaster';
    departmentDto.okrMemberIds = ['member1', 'member2'];
    departmentDto.okrTopicSponsorId = 'testSponsor';
    departmentDto.parentUnitId = 2;
    departmentDto.isParentUnitABranch = true;

    // @ts-ignore
    departmentUnit = new OkrDepartment();
    departmentUnit.id = 1;
    departmentUnit.label = 'test';
    departmentUnit.name = 'testName';
    departmentUnit.isActive = true;
    departmentUnit.objectives = [1, 2, 3];
    departmentUnit.okrTopicSponsorId = 'testSponsor';
    departmentUnit.okrMemberIds = ['member1', 'member2'];
    departmentUnit.okrMasterId = 'testMaster';
    departmentUnit.parentUnitId = 2;
    departmentUnit.isParentUnitABranch = true;

    okrBranchDto = new OkrBranchDto();
    okrBranchDto.okrUnitId = 2;
    okrBranchDto.unitName = 'testName2';
    okrBranchDto.label = 'testLabel';
    okrBranchDto.isActive = true;
    okrBranchDto.okrChildUnitIds = [1];
    okrBranchDto.parentUnitId = 0;
    okrBranchDto.objectiveIds = [4, 5, 6];

    // @ts-ignore
    okrBranch = new OkrBranch();
    okrBranch.id = 2;
    okrBranch.name = 'testName2';
    okrBranch.label = 'testLabel';
    okrBranch.isActive = true;
    okrBranch.okrChildUnitIds = [1];
    okrBranch.parentUnitId = 0;
    okrBranch.objectives = [4, 5, 6];
  });

  it('should be created', () => {
    const service: OkrUnitMapper = TestBed.get(OkrUnitMapper);
    expect(service)
      .toBeTruthy();
  });

  it('getOkrChildUnitById$ returns Departments', done => {
    okrUnitApiService.getOkrChildUnitById$.mockReturnValue(of(departmentDto));

    const service: OkrUnitMapper = TestBed.get(OkrUnitMapper);

    service.getOkrChildUnitById$(1)
      .subscribe((okrChildUnit: OkrChildUnit) => {
        expect(okrChildUnit instanceof OkrDepartment)
          .toBeTruthy();
        done();
      });
  });

  it('getOkrChildUnitById$ maps DepartmentDtos to Departments', done => {
    okrUnitApiService.getOkrChildUnitById$.mockReturnValue(of(departmentDto));

    const service: OkrUnitMapper = TestBed.get(OkrUnitMapper);

    service.getOkrChildUnitById$(1)
      .subscribe((okrChildUnit: OkrChildUnit) => {
        if (okrChildUnit instanceof OkrDepartment) {
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

    const service: OkrUnitMapper = TestBed.get(OkrUnitMapper);

    service.getOkrChildUnitById$(1)
      .subscribe((okrChildUnit: OkrChildUnit) => {
        expect(okrChildUnit instanceof OkrBranch)
          .toBeTruthy();
        done();
      });
  });

  it('getOkrChildUnitById$ maps OkrBranchDtos to OkrBranch', done => {
    okrUnitApiService.getOkrChildUnitById$.mockReturnValue(of(okrBranchDto));

    const service: OkrUnitMapper = TestBed.get(OkrUnitMapper);

    service.getOkrChildUnitById$(1)
      .subscribe((okrChildUnit: OkrChildUnit) => {
        if (okrChildUnit instanceof OkrBranch) {
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

    const service: OkrUnitMapper = TestBed.get(OkrUnitMapper);

    service.getOkrChildUnitById$(1)
      .subscribe((okrChildUnit: OkrChildUnit) => {
        expect(okrChildUnit)
          .toBeFalsy();
        done();
      });
  });

  it('putOkrChildUnit$ maps Departments to dtos', done => {
    okrUnitApiService.putOkrChildUnit$.mockReturnValue(of(departmentDto));

    const service: OkrUnitMapper = TestBed.get(OkrUnitMapper);

    service.putOkrChildUnit$(departmentUnit)
      .subscribe((okrChildUnit: OkrChildUnit) => {
        expect(okrUnitApiService.putOkrChildUnit$)
          .toHaveBeenCalledWith(1, any(OkrDepartmentDto));
        done();
      });
  });

  it('putOkrChildUnit$ maps OkrBranch to dtos', done => {
    okrUnitApiService.putOkrChildUnit$.mockReturnValue(of(okrBranchDto));

    const service: OkrUnitMapper = TestBed.get(OkrUnitMapper);

    service.putOkrChildUnit$(okrBranch)
      .subscribe((okrChildUnit: OkrChildUnit) => {
        expect(okrUnitApiService.putOkrChildUnit$)
          .toHaveBeenCalledWith(2, any(OkrBranchDto));
        done();
      });
  });

  it('putOkrChildUnit$ does not map null', done => {
    okrUnitApiService.putOkrChildUnit$.mockReturnValue(of(null));

    const service: OkrUnitMapper = TestBed.get(OkrUnitMapper);

    service.putOkrChildUnit$(okrBranch)
      .subscribe((okrChildUnit: OkrChildUnit) => {
        expect(okrChildUnit)
          .toBeFalsy();
        done();
      });
  });

  it('putOkrChildUnit$ does not accept null', done => {
    okrUnitApiService.putOkrChildUnit$.mockReturnValue(of(null));

    // tslint:disable-next-line:typedef
    const test = () => {
      const service: OkrUnitMapper = TestBed.get(OkrUnitMapper);

      service.putOkrChildUnit$(null)
        .subscribe((okrChildUnit: OkrChildUnit) => {
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

    const service: OkrUnitMapper = TestBed.get(OkrUnitMapper);

    service.deleteOkrChildUnit$(departmentUnit)
      .subscribe((deleted: boolean) => {
        expect(deleted)
          .toBeTruthy();
        done();
      });
  });

  it('deleteOkrChildUnit$ deletes OkrBranch', done => {
    okrUnitApiService.deleteOkrChildUnit$.mockReturnValue(of(true));

    const service: OkrUnitMapper = TestBed.get(OkrUnitMapper);

    service.deleteOkrChildUnit$(okrBranch)
      .subscribe((deleted: boolean) => {
        expect(deleted)
          .toBeTruthy();
        done();
      });
  });

  it('deleteOkrChildUnit$ does not delete null', done => {
    okrUnitApiService.deleteOkrChildUnit$.mockReturnValue(of(true));

    const service: OkrUnitMapper = TestBed.get(OkrUnitMapper);

    // tslint:disable-next-line:typedef
    const test = () => {
      service.deleteOkrChildUnit$(null)
        .subscribe((deleted: boolean) => {
          fail('No Error has been thrown, although an error should have been thrown.');
          done();
        });
    };

    expect(test)
      .toThrow(TypeError);
    done();
  });
});
