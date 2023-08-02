import {TestBed} from '@angular/core/testing';

import {CurrentCompanyService} from './current-company.service';
import {CompanyMapper} from '../shared/services/mapper/company.mapper';
import {CompanyUnit} from '../shared/model/ui/OrganizationalUnit/company-unit';
import {Observable, of} from 'rxjs';
import {take} from 'rxjs/operators';

const companyMapperMock: any = {
  getCompanyById$: jest.fn(getCompanyByIdMock$),
  getParentCompanyOfOkrUnits$: jest.fn(getParentCompanyOfOkrUnitsMock$),
};

function getCompanyByIdMock$(companyId: number): Observable<CompanyUnit> {
  return of(testCompanyList.filter(c => c.id === companyId).pop());
}

function getParentCompanyOfOkrUnitsMock$(departmentId: number): Observable<CompanyUnit> {
  return of(testCompanyList.filter(c => c.okrChildUnitIds.includes(departmentId)).pop());
}

const testCompany: CompanyUnit = {
  id: 1,
  okrChildUnitIds: [0],
  cycleId: 1,
  label: '',
  name: '',
  photo: '',
  objectives: [],
};
const testCompany2: CompanyUnit = {
  id: 2,
  okrChildUnitIds: [1],
  cycleId: 1,
  label: '',
  name: '',
  photo: '',
  objectives: [],
};
const testCompanyList: CompanyUnit[] = [testCompany, testCompany2];

let service: CurrentCompanyService;

describe('CurrentCompanyService', () => {
  beforeEach(() => TestBed.configureTestingModule({
    providers: [
      { provide: CompanyMapper, useValue: companyMapperMock },
    ],
  }));

  beforeEach(() => {
    service = TestBed.inject(CurrentCompanyService);
    companyMapperMock.getCompanyById$.mockReset();
    companyMapperMock.getCompanyById$.mockImplementation(getCompanyByIdMock$);
    companyMapperMock.getParentCompanyOfOkrUnits$.mockImplementation(getParentCompanyOfOkrUnitsMock$);
  });

  it('should be created', () => {
    expect(service)
      .toBeTruthy();
  });

  it('should set current company by company id', done => {
    service.setCurrentCompanyByCompanyId(testCompany.id);
    (service as any).currentCompany$.asObservable().subscribe(value => {
      expect(value).toEqual(testCompany);
      done();
    });
  });

  it('setCurrentCompanyByCompanyId gets undefined parameter set current company to undefined', done => {
    service.setCurrentCompanyByCompanyId(undefined);
    (service as any).currentCompany$.asObservable().subscribe(value => {
      expect(value).toEqual(undefined);
      done();
    });
  });

  it('setCurrentCompanyByCompanyId gets null parameter set current company to undefined', done => {
    service.setCurrentCompanyByCompanyId(null);
    (service as any).currentCompany$.asObservable().subscribe(value => {
      expect(value).toEqual(undefined);
      done();
    });
  });

  it('setCurrentCompanyByCompanyId gets unknown id as parameter set current company to undefined', done => {
    service.setCurrentCompanyByCompanyId(213412421);
    (service as any).currentCompany$.asObservable().subscribe(value => {
      expect(value).toEqual(undefined);
      done();
    });
  });

  it('setCurrentCompanyByCompanyId gets null as parameter', done => {
    companyMapperMock.getCompanyById$.mockReturnValue(of(null));
    service.setCurrentCompanyByCompanyId(testCompany.id);
    (service as any).currentCompany$.asObservable().subscribe(value => {
      expect(value).toEqual(null);
      done();
    });
  });

  it('setCurrentCompanyByCompanyId gets undefined as parameter', done => {
    companyMapperMock.getCompanyById$.mockReturnValue(of(undefined));
    service.setCurrentCompanyByCompanyId(testCompany.id);
    (service as any).currentCompany$.asObservable().subscribe(value => {
      expect(value).toEqual(undefined);
      done();
    });
  });

  it('setCurrentCompanyByChildDepartment sets parentCompany as currentCompany', done => {
    service.setCurrentCompanyByChildDepartmentId(0);
    (service as any).currentCompany$.asObservable().subscribe(value => {
      expect(value).toEqual(testCompany);
      done();
    });
  });

  it('setCurrentCompanyByChildDepartmentId gets null as parameter sets company to  undefined', done => {
    service.setCurrentCompanyByChildDepartmentId(null);
    (service as any).currentCompany$.asObservable().subscribe(value => {
      expect(value).toEqual(undefined);
      done();
    });
  });

  it('setCurrentCompanyByChildDepartmentId gets unknown id as parameter sets company to undefined', done => {
    service.setCurrentCompanyByChildDepartmentId(2345);
    (service as any).currentCompany$.asObservable().subscribe(value => {
      expect(value).toEqual(undefined);
      done();
    });
  });

  it('getCurrentCompany returns actual company', done => {
    (service as any).currentCompany$.next(testCompany);
    service.getCurrentCompany$()
      .pipe(take(1))
      .subscribe(value => {
        expect(value)
          .toEqual(testCompany);
        done();
      });
  });

  it('getCurrentCompany returns last set company', done => {
    service.setCurrentCompanyByCompanyId(testCompany.id);
    service.setCurrentCompanyByCompanyId(testCompany2.id);
    service.getCurrentCompany$()
      .pipe(take(1))
      .subscribe(value => {
        expect(value)
          .toEqual(testCompany2);
        done();
      });
  });
});
