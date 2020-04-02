import { CycleUnit } from './cycle-unit';
import { CompanyUnit } from './OrganizationalUnit/company-unit';

export class CycleWithHistoryCompany {
  cycle: CycleUnit;
  company: CompanyUnit;

  constructor(cycle: CycleUnit, company: CompanyUnit) {
    this.cycle = cycle;
    this.company = company;
  }
}
