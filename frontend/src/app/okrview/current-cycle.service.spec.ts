import { TestBed } from '@angular/core/testing';

import { CurrentCycleService } from './current-cycle.service';
import { CycleMapper } from '../shared/services/mapper/cycle.mapper';
import { CurrentCompanyService } from './current-company.service';
import { of } from 'rxjs';
import { CompanyUnit } from '../shared/model/ui/OrganizationalUnit/company-unit';
import { CycleState, CycleUnit } from '../shared/model/ui/cycle-unit';

const currentCompanyServiceMock: any = {
  getCurrentCompany$: jest.fn()
};

const cycleMapperMock: any = {
  getCyclesOfCompany$: jest.fn()
};

const currentCompanyMock: CompanyUnit = {
  id: 1,
  corporateObjectiveStructureIds: [],
  cycleId: 2,
  departmentIds: [],
  label: 'test',
  name: 'testCompany',
  objectives: []
};

const cycleUnitsMock: CycleUnit[] = [
  new CycleUnit(2, 'testCycle', [1], new Date(), new Date(), CycleState.ACTIVE, true),
  new CycleUnit(3, 'testCycle3', [], new Date(), new Date(), CycleState.CLOSED, true)
];

describe('CurrentCycleService', () => {
  beforeEach(() => TestBed.configureTestingModule({
    providers: [
      CurrentCycleService,
      { provide: CurrentCompanyService, useValue: currentCompanyServiceMock },
      { provide: CycleMapper, useValue: cycleMapperMock },
    ]
  }));

  beforeEach(() => {
    currentCompanyServiceMock.getCurrentCompany$.mockReset();
    cycleMapperMock.getCyclesOfCompany$.mockReset();
  });

  it('should be created', () => {
    currentCompanyServiceMock.getCurrentCompany$.mockReturnValue(of(currentCompanyMock));
    cycleMapperMock.getCyclesOfCompany$.mockReturnValue(of(cycleUnitsMock));

    const service: CurrentCycleService = TestBed.get(CurrentCycleService);

    expect(service)
      .toBeTruthy();
  });

  it('should get current company', done => {
    currentCompanyServiceMock.getCurrentCompany$.mockReturnValue(of(currentCompanyMock));
    cycleMapperMock.getCyclesOfCompany$.mockReturnValue(of(cycleUnitsMock));

    const service: CurrentCycleService = TestBed.get(CurrentCycleService);

    service.getCurrentCycleList$()
      .subscribe(() => {
        expect(currentCompanyServiceMock.getCurrentCompany$)
          .toHaveBeenCalled();

        done();
      });
  });

  it('should get cycles of company', done => {
    currentCompanyServiceMock.getCurrentCompany$.mockReturnValue(of(currentCompanyMock));
    cycleMapperMock.getCyclesOfCompany$.mockReturnValue(of(cycleUnitsMock));

    const service: CurrentCycleService = TestBed.get(CurrentCycleService);

    service.getCurrentCycleList$()
      .subscribe((cycleList: CycleUnit[]) => {
        expect(cycleList)
          .toEqual(cycleUnitsMock);
        done();
      });
  });

  it('should get nothing when company is null', done => {
    currentCompanyServiceMock.getCurrentCompany$.mockReturnValue(of(null));
    cycleMapperMock.getCyclesOfCompany$.mockReturnValue(of(null));

    const service: CurrentCycleService = TestBed.get(CurrentCycleService);

    service.getCurrentCycleList$()
      .subscribe(() => {
        fail();
      });

    setTimeout(() => {
      done();
    }, 1000);
  });

  it('should get an empty cycleList when cycleList is empty', done => {
    currentCompanyServiceMock.getCurrentCompany$.mockReturnValue(of(currentCompanyMock));
    cycleMapperMock.getCyclesOfCompany$.mockReturnValue(of([]));

    const service: CurrentCycleService = TestBed.get(CurrentCycleService);

    service.getCurrentCycleList$()
      .subscribe((cycleList: CycleUnit[]) => {
        expect(cycleList)
          .toEqual([]);
        done();
      });
  });

  it('should get cycle of company', done => {
    currentCompanyServiceMock.getCurrentCompany$.mockReturnValue(of(currentCompanyMock));
    cycleMapperMock.getCyclesOfCompany$.mockReturnValue(of(cycleUnitsMock));

    const service: CurrentCycleService = TestBed.get(CurrentCycleService);

    service.getCurrentCycle$()
      .subscribe((cycle: CycleUnit) => {
        expect(cycle)
          .toEqual(cycleUnitsMock[0]);
        done();
      });
  });

  it('should get no cycle of company when company is null', done => {
    currentCompanyServiceMock.getCurrentCompany$.mockReturnValue(of(null));
    cycleMapperMock.getCyclesOfCompany$.mockReturnValue(of(null));

    const service: CurrentCycleService = TestBed.get(CurrentCycleService);

    service.getCurrentCycle$()
      .subscribe(() => {
        fail();
      });

    setTimeout(() => {
      done();
    }, 1000);
  });

  it('should get no cycle of company when cycle list is empty', done => {
    currentCompanyServiceMock.getCurrentCompany$.mockReturnValue(of(currentCompanyMock));
    cycleMapperMock.getCyclesOfCompany$.mockReturnValue(of([]));

    const service: CurrentCycleService = TestBed.get(CurrentCycleService);

    service.getCurrentCycle$()
      .subscribe(() => {
        fail();
      });

    setTimeout(() => {
      done();
    }, 1000);
  });
});
