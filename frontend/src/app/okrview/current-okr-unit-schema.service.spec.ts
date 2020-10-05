import { TestBed } from '@angular/core/testing';
import { CurrentOkrUnitSchemaService } from './current-okr-unit-schema.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA } from '@angular/core';
import { OkrUnitSchemaMapper } from '../shared/services/mapper/okr-unit-schema.mapper';
import { OkrUnitRole, OkrUnitSchema } from '../shared/model/ui/okr-unit-schema';
import { of } from 'rxjs';

describe('CurrentOkrUnitSchemaService', () => {
  const unitSchemaMapperMock: any = {
    getOkrUnitSchemaOfCompany$: jest.fn(),
    getOkrUnitSchemaByUnitId$: jest.fn(),
  };

  const singleUnitSchema: OkrUnitSchema[] = [
    {
      id: 5,
      isActive: true,
      name: 'testUnitSchema',
      subDepartments: [],
      userRole: OkrUnitRole.MEMBER
    }
  ];

  const threeUnitSchema: OkrUnitSchema[] = [
    {
      id: 5,
      isActive: true,
      name: 'testUnitSchema',
      userRole: OkrUnitRole.MEMBER,
      subDepartments: [
        {
          id: 6,
          isActive: true,
          name: 'testUnitSchema2',
          userRole: OkrUnitRole.MANAGER,
          subDepartments: [
            {
              id: 7,
              isActive: true,
              name: 'testUnitSchema3',
              userRole: OkrUnitRole.USER,
              subDepartments: []
            }
          ]
        }
      ]
    }
  ];

  const threeFlatUnitSchema: OkrUnitSchema[] = [
    {
      id: 5,
      isActive: true,
      name: 'testUnitSchema',
      userRole: OkrUnitRole.MEMBER,
      subDepartments: []
    },
    {
      id: 6,
      isActive: true,
      name: 'testUnitSchema2',
      userRole: OkrUnitRole.MANAGER,
      subDepartments: []
    },
    {
      id: 7,
      isActive: true,
      name: 'testUnitSchema3',
      userRole: OkrUnitRole.USER,
      subDepartments: []
    }
  ];

  beforeEach(() => TestBed.configureTestingModule(
    {
      declarations: [],
      imports: [
        HttpClientTestingModule,
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA],
      providers: [
        { provide: OkrUnitSchemaMapper, useValue: unitSchemaMapperMock }
      ]
    }));

  beforeEach(() => {
    unitSchemaMapperMock.getOkrUnitSchemaOfCompany$.mockReset();
    unitSchemaMapperMock.getOkrUnitSchemaOfCompany$.mockReturnValue(of([]));
    unitSchemaMapperMock.getOkrUnitSchemaByUnitId$.mockReset();
    unitSchemaMapperMock.getOkrUnitSchemaByUnitId$.mockReturnValue(of([]));
  });

  it('should be created', () => {
    const service: CurrentOkrUnitSchemaService = TestBed.get(CurrentOkrUnitSchemaService);
    expect(service)
      .toBeTruthy();
  });

  it('should get and set unit schemas by company id, empty list', done => {
    const service: CurrentOkrUnitSchemaService = TestBed.get(CurrentOkrUnitSchemaService);

    service.setCurrentUnitSchemaByCompanyId(1);
    service.getCurrentUnitSchemas$()
      .subscribe((schema: OkrUnitSchema[]) => {
        expect(schema)
          .toEqual([]);

        done();
      });
  });

  it('should get and set unit schemas by company id, singleUnitSchema', done => {
    unitSchemaMapperMock.getOkrUnitSchemaOfCompany$.mockReturnValue(of(singleUnitSchema));

    const service: CurrentOkrUnitSchemaService = TestBed.get(CurrentOkrUnitSchemaService);

    service.setCurrentUnitSchemaByCompanyId(1);
    service.getCurrentUnitSchemas$()
      .subscribe((schema: OkrUnitSchema[]) => {
        expect(schema)
          .toEqual(singleUnitSchema);

        done();
      });
  });

  it('should get and set unit schemas by company id, threeUnitSchemas', done => {
    unitSchemaMapperMock.getOkrUnitSchemaOfCompany$.mockReturnValue(of(threeUnitSchema));

    const service: CurrentOkrUnitSchemaService = TestBed.get(CurrentOkrUnitSchemaService);

    service.setCurrentUnitSchemaByCompanyId(1);
    service.getCurrentUnitSchemas$()
      .subscribe((schema: OkrUnitSchema[]) => {
        expect(schema)
          .toEqual(threeUnitSchema);

        done();
      });
  });

  it('should get and set unit schemas by company id, threeFlatUnitSchemas', done => {
    unitSchemaMapperMock.getOkrUnitSchemaOfCompany$.mockReturnValue(of(threeFlatUnitSchema));

    const service: CurrentOkrUnitSchemaService = TestBed.get(CurrentOkrUnitSchemaService);

    service.setCurrentUnitSchemaByCompanyId(1);
    service.getCurrentUnitSchemas$()
      .subscribe((schema: OkrUnitSchema[]) => {
        expect(schema)
          .toEqual(threeFlatUnitSchema);

        done();
      });
  });

  it('should get and set unit schemas by department id, empty list', done => {
    const service: CurrentOkrUnitSchemaService = TestBed.get(CurrentOkrUnitSchemaService);

    service.setCurrentUnitSchemaByDepartmentId(1);
    service.getCurrentUnitSchemas$()
      .subscribe((schema: OkrUnitSchema[]) => {
        expect(schema)
          .toEqual([]);

        done();
      });
  });

  it('should get and set unit schemas by department id, singleUnitSchema', done => {
    unitSchemaMapperMock.getOkrUnitSchemaByUnitId$.mockReturnValue(of(singleUnitSchema));

    const service: CurrentOkrUnitSchemaService = TestBed.get(CurrentOkrUnitSchemaService);

    service.setCurrentUnitSchemaByDepartmentId(1);
    service.getCurrentUnitSchemas$()
      .subscribe((schema: OkrUnitSchema[]) => {
        expect(schema)
          .toEqual(singleUnitSchema);

        done();
      });
  });

  it('should get and set unit schemas by department id, threeUnitSchemas', done => {
    unitSchemaMapperMock.getOkrUnitSchemaByUnitId$.mockReturnValue(of(threeUnitSchema));

    const service: CurrentOkrUnitSchemaService = TestBed.get(CurrentOkrUnitSchemaService);

    service.setCurrentUnitSchemaByDepartmentId(1);
    service.getCurrentUnitSchemas$()
      .subscribe((schema: OkrUnitSchema[]) => {
        expect(schema)
          .toEqual(threeUnitSchema);

        done();
      });
  });

  it('should get and set unit schemas by department id, threeFlatUnitSchemas', done => {
    unitSchemaMapperMock.getOkrUnitSchemaByUnitId$.mockReturnValue(of(threeFlatUnitSchema));

    const service: CurrentOkrUnitSchemaService = TestBed.get(CurrentOkrUnitSchemaService);

    service.setCurrentUnitSchemaByDepartmentId(1);
    service.getCurrentUnitSchemas$()
      .subscribe((schema: OkrUnitSchema[]) => {
        expect(schema)
          .toEqual(threeFlatUnitSchema);

        done();
      });
  });

  it('should get and set current unit id, companyId', done => {
    const service: CurrentOkrUnitSchemaService = TestBed.get(CurrentOkrUnitSchemaService);

    const id: number = 12;

    service.setCurrentUnitSchemaByCompanyId(id);
    service.getCurrentUnitId$()
      .subscribe((idFromService: number) => {
        expect(idFromService)
          .toBe(id);
        done();
      });
  });

  it('should get and set current unit id, departmentId', done => {
    const service: CurrentOkrUnitSchemaService = TestBed.get(CurrentOkrUnitSchemaService);

    const id: number = 16;

    service.setCurrentUnitSchemaByDepartmentId(id);
    service.getCurrentUnitId$()
      .subscribe((idFromService: number) => {
        expect(idFromService)
          .toBe(id);
        done();
      });
  });

  it('should get parent unit id, empty list, does not emit', done => {
    const service: CurrentOkrUnitSchemaService = TestBed.get(CurrentOkrUnitSchemaService);

    service.setCurrentUnitSchemaByDepartmentId(1);
    service.getCurrentParentUnitId$()
      .subscribe(() => {
        fail();
      });

    setTimeout(() => {
      done();
    }, 1000);
  });

  it('should get parent unit id, has parent unit, emits id', done => {
    unitSchemaMapperMock.getOkrUnitSchemaByUnitId$.mockReturnValue(of(threeUnitSchema));

    const service: CurrentOkrUnitSchemaService = TestBed.get(CurrentOkrUnitSchemaService);

    service.setCurrentUnitSchemaByDepartmentId(6);
    service.getCurrentParentUnitId$()
      .subscribe((id: number) => {
        expect(id)
          .toBe(5);
        done();
      });
  });

  it('should get parent unit id, has no parent unit, does not emit', done => {
    unitSchemaMapperMock.getOkrUnitSchemaByUnitId$.mockReturnValue(of(threeFlatUnitSchema));

    const service: CurrentOkrUnitSchemaService = TestBed.get(CurrentOkrUnitSchemaService);

    service.setCurrentUnitSchemaByDepartmentId(6);
    service.getCurrentParentUnitId$()
      .subscribe(() => {
        fail();
      });

    setTimeout(() => {
      done();
    }, 1000);
  });

  it('should get parent unit id, is not in list, does not emit', done => {
    unitSchemaMapperMock.getOkrUnitSchemaByUnitId$.mockReturnValue(of(threeUnitSchema));

    const service: CurrentOkrUnitSchemaService = TestBed.get(CurrentOkrUnitSchemaService);

    service.setCurrentUnitSchemaByDepartmentId(1);
    service.getCurrentParentUnitId$()
      .subscribe(() => {
        fail();
      });

    setTimeout(() => {
      done();
    }, 1000);
  });

  it('getUnitSchemasToReachUnitWithId, empty list, returns empty list', done => {
    const service: CurrentOkrUnitSchemaService = TestBed.get(CurrentOkrUnitSchemaService);

    service.setCurrentUnitSchemaByDepartmentId(1);
    service.getUnitSchemasToReachUnitWithId$(1)
      .subscribe((schemas: OkrUnitSchema[]) => {
        expect(schemas)
          .toEqual([]);
        done();
      });
  });

  it('getUnitSchemasToReachUnitWithId, three unit schema, returns parent units', done => {
    unitSchemaMapperMock.getOkrUnitSchemaByUnitId$.mockReturnValue(of(threeUnitSchema));

    const service: CurrentOkrUnitSchemaService = TestBed.get(CurrentOkrUnitSchemaService);

    service.setCurrentUnitSchemaByDepartmentId(1);
    service.getUnitSchemasToReachUnitWithId$(7)
      .subscribe((schemas: OkrUnitSchema[]) => {
        expect(schemas)
          .toEqual([
            threeUnitSchema[0],
            threeUnitSchema[0].subDepartments[0]
          ]);
        done();
      });
  });

  it('getUnitSchemasToReachUnitWithId, three unit schema, id not in list, returns empty list', done => {
    unitSchemaMapperMock.getOkrUnitSchemaByUnitId$.mockReturnValue(of(threeUnitSchema));

    const service: CurrentOkrUnitSchemaService = TestBed.get(CurrentOkrUnitSchemaService);

    service.setCurrentUnitSchemaByDepartmentId(1);
    service.getUnitSchemasToReachUnitWithId$(10)
      .subscribe((schemas: OkrUnitSchema[]) => {
        expect(schemas)
          .toEqual([]);
        done();
      });
  });

  it('getUnitSchemasToReachUnitWithId, three unit schema, is parent, returns empty list', done => {
    unitSchemaMapperMock.getOkrUnitSchemaByUnitId$.mockReturnValue(of(threeUnitSchema));

    const service: CurrentOkrUnitSchemaService = TestBed.get(CurrentOkrUnitSchemaService);

    service.setCurrentUnitSchemaByDepartmentId(1);
    service.getUnitSchemasToReachUnitWithId$(5)
      .subscribe((schemas: OkrUnitSchema[]) => {
        expect(schemas)
          .toEqual([]);
        done();
      });
  });

  it('getUnitIdsToReachUnitWithId, empty list, returns empty list', done => {
    const service: CurrentOkrUnitSchemaService = TestBed.get(CurrentOkrUnitSchemaService);

    service.setCurrentUnitSchemaByDepartmentId(1);
    service.getUnitIdsToReachUnitWithId$(1)
      .subscribe((schemas: number[]) => {
        expect(schemas)
          .toEqual([]);
        done();
      });
  });

  it('getUnitIdsToReachUnitWithId, three unit schema, returns parent units', done => {
    unitSchemaMapperMock.getOkrUnitSchemaByUnitId$.mockReturnValue(of(threeUnitSchema));

    const service: CurrentOkrUnitSchemaService = TestBed.get(CurrentOkrUnitSchemaService);

    service.setCurrentUnitSchemaByDepartmentId(1);
    service.getUnitIdsToReachUnitWithId$(7)
      .subscribe((schemas: number[]) => {
        expect(schemas)
          .toEqual([
            threeUnitSchema[0].id,
            threeUnitSchema[0].subDepartments[0].id
          ]);
        done();
      });
  });

  it('getUnitIdsToReachUnitWithId, three unit schema, id not in list, returns empty list', done => {
    unitSchemaMapperMock.getOkrUnitSchemaByUnitId$.mockReturnValue(of(threeUnitSchema));

    const service: CurrentOkrUnitSchemaService = TestBed.get(CurrentOkrUnitSchemaService);

    service.setCurrentUnitSchemaByDepartmentId(1);
    service.getUnitIdsToReachUnitWithId$(10)
      .subscribe((schemas: number[]) => {
        expect(schemas)
          .toEqual([]);
        done();
      });
  });

  it('getUnitIdsToReachUnitWithId, three unit schema, is parent, returns empty list', done => {
    unitSchemaMapperMock.getOkrUnitSchemaByUnitId$.mockReturnValue(of(threeUnitSchema));

    const service: CurrentOkrUnitSchemaService = TestBed.get(CurrentOkrUnitSchemaService);

    service.setCurrentUnitSchemaByDepartmentId(1);
    service.getUnitIdsToReachUnitWithId$(5)
      .subscribe((schemas: number[]) => {
        expect(schemas)
          .toEqual([]);
        done();
      });
  });

  it('updateUnitSchemaTeamRole, empty schema list, does nothing', done => {
    const service: CurrentOkrUnitSchemaService = TestBed.get(CurrentOkrUnitSchemaService);

    service.setCurrentUnitSchemaByDepartmentId(1);
    service.updateSchemaTeamRole(2, OkrUnitRole.MANAGER);

    service.getCurrentUnitSchemas$()
      .subscribe((schema: OkrUnitSchema[]) => {
        expect(schema)
          .toEqual([]);
        done();
      });
  });

  it('updateUnitSchemaTeamRole, three unit schema, updates', done => {
    unitSchemaMapperMock.getOkrUnitSchemaByUnitId$.mockReturnValue(of(threeUnitSchema));

    const service: CurrentOkrUnitSchemaService = TestBed.get(CurrentOkrUnitSchemaService);

    service.setCurrentUnitSchemaByDepartmentId(1);
    service.updateSchemaTeamRole(7, OkrUnitRole.MANAGER);

    service.getCurrentUnitSchemas$()
      .subscribe((schema: OkrUnitSchema[]) => {
        expect(schema)
          .toEqual([
            {
              id: 5,
              isActive: true,
              name: 'testUnitSchema',
              userRole: OkrUnitRole.MEMBER,
              subDepartments: [
                {
                  id: 6,
                  isActive: true,
                  name: 'testUnitSchema2',
                  userRole: OkrUnitRole.MANAGER,
                  subDepartments: [
                    {
                      id: 7,
                      isActive: true,
                      name: 'testUnitSchema3',
                      userRole: OkrUnitRole.MANAGER,
                      subDepartments: []
                    }
                  ]
                }
              ]
            }
          ]);
        done();
      });
  });

  it('updateUnitSchemaTeamRole, three unit schema, id not in list, does nothing', done => {
    unitSchemaMapperMock.getOkrUnitSchemaByUnitId$.mockReturnValue(of(threeUnitSchema));

    const service: CurrentOkrUnitSchemaService = TestBed.get(CurrentOkrUnitSchemaService);

    service.setCurrentUnitSchemaByDepartmentId(1);
    service.updateSchemaTeamRole(10, OkrUnitRole.MANAGER);

    service.getCurrentUnitSchemas$()
      .subscribe((schema: OkrUnitSchema[]) => {
        expect(schema)
          .toEqual(threeUnitSchema);
        done();
      });
  });
// tslint:disable-next-line:max-file-line-count
});
