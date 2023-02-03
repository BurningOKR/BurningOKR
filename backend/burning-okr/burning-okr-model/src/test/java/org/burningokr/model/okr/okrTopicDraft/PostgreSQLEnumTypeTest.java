package org.burningokr.model.okr.okrTopicDraft;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PostgreSQLEnumTypeTest {
  @Mock
  PreparedStatement preparedStatement;

  @Mock
  SharedSessionContractImplementor session;

  private OkrTopicDraftStatusEnum statusEnum;
  private PostgreSQLEnumType postgreSQLEnumType;
  private int index;

  @Before
  public void setUp() {
    index = 0;
    statusEnum = OkrTopicDraftStatusEnum.submitted;
    postgreSQLEnumType = new PostgreSQLEnumType();
  }

  @Test
  public void expects_setObject_to_be_called() {
    try {
      postgreSQLEnumType.nullSafeSet(preparedStatement, statusEnum, index, session);
      verify(preparedStatement).setObject(eq(index), eq(statusEnum.toString()), anyInt());

    } catch (SQLException throwables) {
      throwables.printStackTrace();
      fail();
    }
  }

  @Test
  public void expects_setNull_to_be_called() {
    try {
      postgreSQLEnumType.nullSafeSet(preparedStatement, null, index, session);
      verify(preparedStatement).setNull(eq(index), anyInt());

    } catch (SQLException throwables) {
      throwables.printStackTrace();
      fail();
    }
  }
}
