import { TestBed } from '@angular/core/testing';
import { CurrentDepartmentStructureService } from './current-department-structure.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA } from '@angular/core';
import { DepartmentStructureMapper } from '../shared/services/mapper/department-structure.mapper';
import { DepartmentStructure, DepartmentStructureRole } from '../shared/model/ui/department-structure';
import { of } from 'rxjs';

const departmentStructureMapperMock: any = {
  getDepartmentStructuresOfDepartment$: jest.fn(),
  getDepartmentStructuresOfCompany$: jest.fn()
};
const testDepartmentStructures: DepartmentStructure[] = [{
  id: 0,
  isActive: true,
  name: '',
  subDepartments: [{
    id: 1,
    isActive: true,
    name: '',
    subDepartments: [],
    userRole: DepartmentStructureRole.MANAGER}
  ],
  userRole: DepartmentStructureRole.MANAGER
}];
let service: CurrentDepartmentStructureService;

describe('DepartmentStructureServiceService', () => {
  beforeEach(() => TestBed.configureTestingModule(
    {
      declarations: [],
      imports: [
        HttpClientTestingModule,
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA],
      providers: [
        {provide: DepartmentStructureMapper, useValue: departmentStructureMapperMock}
      ]
    }));

  beforeEach(() => {
    service = TestBed.get(CurrentDepartmentStructureService);
    departmentStructureMapperMock.getDepartmentStructuresOfDepartment$.mockReset();
    departmentStructureMapperMock.getDepartmentStructuresOfCompany$.mockReset();
    departmentStructureMapperMock.getDepartmentStructuresOfDepartment$.mockReturnValue(of(testDepartmentStructures));
    departmentStructureMapperMock.getDepartmentStructuresOfCompany$.mockReturnValue(of(testDepartmentStructures));
  });

  it('should be created', () => {
    expect(service)
      .toBeTruthy();
  });

  it('should set current department structure by department id', done => {
    service.setCurrentDepartmentStructuresByDepartmentId(testDepartmentStructures[0].id);
    service.getCurrentDepartmentStructures$()
      .subscribe(value => {
        expect(value)
          .toEqual(testDepartmentStructures);
        done();
      });
  });

  it('should set current department structure by company id', done => {
    service.setCurrentDepartmentStructuresByCompanyId(0);
    service.getCurrentDepartmentStructures$()
      .subscribe(value => {
        expect(value)
          .toEqual(testDepartmentStructures);
        done();
      });
  });

  it('should get department structure list with department id', done => {
    service.setCurrentDepartmentStructuresByDepartmentId(0);

    service.getDepartmentStructuresToReachDepartmentWithId$(1)
      .subscribe(value => {
        expect(value)
          .toEqual(testDepartmentStructures);
        done();
      });
  });
});
