package org.burningokr.dialects;

import org.hibernate.boot.model.TypeContributions;
import org.hibernate.dialect.SQLServer2012Dialect;
import org.hibernate.service.ServiceRegistry;

/**
 * This class fixes the UUID Generator of the SQL Server dialect.
 * The Microsoft SQL Server uses GUIDs instead of UUIDs. Because of that, the order of the bytes
 * is changed, when the UUID is saved to the SQL Server.
 */
public class SQLServer2012UUIDFixDialect extends SQLServer2012Dialect {
  @Override
  public void contributeTypes(TypeContributions typeContributions, ServiceRegistry serviceRegistry) {
    super.contributeTypes(typeContributions, serviceRegistry);

    typeContributions.contributeType(SqlServerUUIDFixType.INSTANCE);
  }
}
