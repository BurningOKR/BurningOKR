import { CompanyUnit } from '../../../shared/model/ui/OrganizationalUnit/company-unit';
import { DepartmentUnit } from '../../../shared/model/ui/OrganizationalUnit/department-unit';
import { CompanyStructure } from '../../../shared/model/ui/OrganizationalUnit/company-structure';

// TODO: please delete me. i am ugly... -> for real: type check is code smell
export class ItemHelperService {
  static getType(item: CompanyStructure): string {
    if (item instanceof CompanyUnit) {
      return 'company';
    } else if (item instanceof DepartmentUnit) {
      return 'department';
    } else {
      return '';
    }
  }
}
