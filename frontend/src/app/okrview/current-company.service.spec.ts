import { TestBed } from '@angular/core/testing';

import { CurrentCompanyService } from './current-company.service';
import { CompanyMapper } from '../shared/services/mapper/company.mapper';
import { CompanyMapperMock } from '../shared/mocks/company-mapper-mock';

describe('CurrentCompanyService', () => {
  beforeEach(() => TestBed.configureTestingModule({
    providers: [
      {provide: CompanyMapper, useValue: CompanyMapperMock}
    ]
  }));

  it('should be created', () => {
    const service: CurrentCompanyService = TestBed.get(CurrentCompanyService);
    expect(service)
      .toBeTruthy();
  });
});
