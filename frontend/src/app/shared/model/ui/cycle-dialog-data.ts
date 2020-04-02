import { CompanyUnit } from './OrganizationalUnit/company-unit';
import { Observable } from 'rxjs';
import { CycleUnit } from './cycle-unit';

export interface CycleDialogData {
  company: CompanyUnit;
  cycles$: Observable<CycleUnit[]>;
}
