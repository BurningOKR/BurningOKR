import { TestBed } from '@angular/core/testing';

import { StructureMapper } from './structure.mapper';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { StructureApiService } from '../api/structure-api.service';
import { of } from 'rxjs';
import { DepartmentDto } from '../../model/api/structure/department.dto';
import { DepartmentUnit } from '../../model/ui/OrganizationalUnit/department-unit';
import { SubStructure } from '../../model/ui/OrganizationalUnit/sub-structure';
import { CorporateObjectiveStructureDto } from '../../model/api/structure/corporate-objective-structure.dto';
import { CorporateObjectiveStructure } from '../../model/ui/OrganizationalUnit/corporate-objective-structure';
import any = jasmine.any;

const structureApiService: any = {
  getSubStructureById$: jest.fn(),
  putSubStructure$: jest.fn(),
  deleteSubStructure$: jest.fn()
};

let departmentDto: DepartmentDto;
let corporateObjectiveStructureDto: CorporateObjectiveStructureDto;
let departmentUnit: DepartmentUnit;

let corporateObjectiveStructure: CorporateObjectiveStructure;

describe('StructureMapper', () => {
  beforeEach(() => TestBed.configureTestingModule({
    imports: [HttpClientTestingModule],
    providers: [
      StructureMapper,
      { provide: StructureApiService, useValue: structureApiService }
    ]
  }));

  beforeEach(() => {
    structureApiService.getSubStructureById$.mockReset();
    structureApiService.putSubStructure$.mockReset();
    structureApiService.deleteSubStructure$.mockReset();

    departmentDto = new DepartmentDto();
    departmentDto.structureId = 1;
    departmentDto.structureName = 'testName';
    departmentDto.label = 'test';
    departmentDto.isActive = true;
    departmentDto.objectiveIds = [1, 2, 3];
    departmentDto.okrMasterId = 'testMaster';
    departmentDto.okrMemberIds = ['member1', 'member2'];
    departmentDto.okrTopicSponsorId = 'testSponsor';
    departmentDto.parentStructureId = 2;
    departmentDto.isParentStructureACorporateObjectiveStructure = true;

    // @ts-ignore
    departmentUnit = new DepartmentUnit();
    departmentUnit.id = 1;
    departmentUnit.label = 'test';
    departmentUnit.name = 'testName';
    departmentUnit.isActive = true;
    departmentUnit.objectives = [1, 2, 3];
    departmentUnit.okrTopicSponsorId = 'testSponsor';
    departmentUnit.okrMemberIds = ['member1', 'member2'];
    departmentUnit.okrMasterId = 'testMaster';
    departmentUnit.parentStructureId = 2;
    departmentUnit.isParentStructureACorporateObjectiveStructure = true;

    corporateObjectiveStructureDto = new CorporateObjectiveStructureDto();
    corporateObjectiveStructureDto.structureId = 2;
    corporateObjectiveStructureDto.structureName = 'testName2';
    corporateObjectiveStructureDto.label = 'testLabel';
    corporateObjectiveStructureDto.isActive = true;
    corporateObjectiveStructureDto.subStructureIds = [1];
    corporateObjectiveStructureDto.parentStructureId = 0;
    corporateObjectiveStructureDto.objectiveIds = [4, 5, 6];

    // @ts-ignore
    corporateObjectiveStructure = new CorporateObjectiveStructure();
    corporateObjectiveStructure.id = 2;
    corporateObjectiveStructure.name = 'testName2';
    corporateObjectiveStructure.label = 'testLabel';
    corporateObjectiveStructure.isActive = true;
    corporateObjectiveStructure.subStructureIds = [1];
    corporateObjectiveStructure.parentStructureId = 0;
    corporateObjectiveStructure.objectives = [4, 5, 6];
  });

  it('should be created', () => {
    const service: StructureMapper = TestBed.get(StructureMapper);
    expect(service)
      .toBeTruthy();
  });

  it('getSubStructureById$ returns Departments', done => {
    structureApiService.getSubStructureById$.mockReturnValue(of(departmentDto));

    const service: StructureMapper = TestBed.get(StructureMapper);

    service.getSubStructureById$(1)
      .subscribe((subStructure: SubStructure) => {
        expect(subStructure instanceof DepartmentUnit)
          .toBeTruthy();
        done();
      });
  });

  it('getSubStructureById$ maps DepartmentDtos to Departments', done => {
    structureApiService.getSubStructureById$.mockReturnValue(of(departmentDto));

    const service: StructureMapper = TestBed.get(StructureMapper);

    service.getSubStructureById$(1)
      .subscribe((subStructure: SubStructure) => {
        if (subStructure instanceof DepartmentUnit) {
          expect(subStructure)
            .toEqual(departmentUnit);
        } else {
          fail('Did not get a DepartmentUnit');
        }
        done();
      });
  });

  it('getSubStructureById$ returns CorporateObjectiveStructures', done => {
    structureApiService.getSubStructureById$.mockReturnValue(of(corporateObjectiveStructureDto));

    const service: StructureMapper = TestBed.get(StructureMapper);

    service.getSubStructureById$(1)
      .subscribe((subStructure: SubStructure) => {
        expect(subStructure instanceof CorporateObjectiveStructure)
          .toBeTruthy();
        done();
      });
  });

  it('getSubStructureById$ maps CorporateObjectiveStructureDtos to CorporateObjectiveStructures', done => {
    structureApiService.getSubStructureById$.mockReturnValue(of(corporateObjectiveStructureDto));

    const service: StructureMapper = TestBed.get(StructureMapper);

    service.getSubStructureById$(1)
      .subscribe((subStructure: SubStructure) => {
        if (subStructure instanceof CorporateObjectiveStructure) {
          expect(subStructure)
            .toEqual(corporateObjectiveStructure);
        } else {
          fail('Did not get a CorporateObjectiveStructure');
        }
        done();
      });
  });

  it('getSubStructureById$ does not map null', done => {
    structureApiService.getSubStructureById$.mockReturnValue(of(null));

    const service: StructureMapper = TestBed.get(StructureMapper);

    service.getSubStructureById$(1)
      .subscribe((subStructure: SubStructure) => {
        expect(subStructure)
          .toBeFalsy();
        done();
      });
  });

  it('putSubStructure$ maps Departments to dtos', done => {
    structureApiService.putSubStructure$.mockReturnValue(of(departmentDto));

    const service: StructureMapper = TestBed.get(StructureMapper);

    service.putSubStructure$(departmentUnit)
      .subscribe((subStructure: SubStructure) => {
        expect(structureApiService.putSubStructure$)
          .toHaveBeenCalledWith(1, any(DepartmentDto));
        done();
      });
  });

  it('putSubStructure$ maps CorporateObjectiveStructures to dtos', done => {
    structureApiService.putSubStructure$.mockReturnValue(of(corporateObjectiveStructureDto));

    const service: StructureMapper = TestBed.get(StructureMapper);

    service.putSubStructure$(corporateObjectiveStructure)
      .subscribe((subStructure: SubStructure) => {
        expect(structureApiService.putSubStructure$)
          .toHaveBeenCalledWith(2, any(CorporateObjectiveStructureDto));
        done();
      });
  });

  it('putSubStructure$ does not map null', done => {
    structureApiService.putSubStructure$.mockReturnValue(of(null));

    const service: StructureMapper = TestBed.get(StructureMapper);

    service.putSubStructure$(corporateObjectiveStructure)
      .subscribe((subStructure: SubStructure) => {
        expect(subStructure)
          .toBeFalsy();
        done();
      });
  });

  it('putSubStructure$ does not accept null', done => {
    structureApiService.putSubStructure$.mockReturnValue(of(null));

    // tslint:disable-next-line:typedef
    const test = () => {
      const service: StructureMapper = TestBed.get(StructureMapper);

      service.putSubStructure$(null)
        .subscribe((subStructure: SubStructure) => {
          fail('No Error has been thrown, although an error should have been thrown.');
          done();
        });
    };

    expect(test)
      .toThrow(TypeError);
    done();

  });

  it('deleteSubStructure$ deletes Department', done => {
    structureApiService.deleteSubStructure$.mockReturnValue(of(true));

    const service: StructureMapper = TestBed.get(StructureMapper);

    service.deleteSubStructure$(departmentUnit)
      .subscribe((deleted: boolean) => {
        expect(deleted)
          .toBeTruthy();
        done();
      });
  });

  it('deleteSubStructure$ deletes CorporateObjectiveStructure', done => {
    structureApiService.deleteSubStructure$.mockReturnValue(of(true));

    const service: StructureMapper = TestBed.get(StructureMapper);

    service.deleteSubStructure$(corporateObjectiveStructure)
      .subscribe((deleted: boolean) => {
        expect(deleted)
          .toBeTruthy();
        done();
      });
  });

  it('deleteSubStructure$ does not delete null', done => {
    structureApiService.deleteSubStructure$.mockReturnValue(of(true));

    const service: StructureMapper = TestBed.get(StructureMapper);

    // tslint:disable-next-line:typedef
    const test = () => {
      service.deleteSubStructure$(null)
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
