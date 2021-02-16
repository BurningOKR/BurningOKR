package org.burningokr.dialects;

import java.util.UUID;
import org.hibernate.dialect.Dialect;
import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.type.LiteralType;
import org.hibernate.type.StringType;
import org.hibernate.type.descriptor.java.UUIDTypeDescriptor;
import org.hibernate.type.descriptor.sql.VarcharTypeDescriptor;

public class SqlServerUUIDFixType extends AbstractSingleColumnStandardBasicType<UUID>
    implements LiteralType<UUID> {

  public static final SqlServerUUIDFixType INSTANCE = new SqlServerUUIDFixType();

  public SqlServerUUIDFixType() {
    super(VarcharTypeDescriptor.INSTANCE, UUIDTypeDescriptor.INSTANCE);
  }

  @Override
  protected boolean registerUnderJavaType() {
    return true;
  }

  @Override
  public String getName() {
    return "fixed-uuid";
  }

  @Override
  public String objectToSQLString(UUID uuid, Dialect dialect) throws Exception {
    return StringType.INSTANCE.objectToSQLString(uuid.toString(), dialect);
  }
}
