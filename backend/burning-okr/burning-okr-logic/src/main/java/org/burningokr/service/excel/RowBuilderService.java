package org.burningokr.service.excel;

import java.util.Collection;

public interface RowBuilderService<T> {
  Collection<T> generateForDepartment(long departmentId);

  Collection<T> generateForCompany(long companyId);
}
