package org.burningokr.model.okr.okrTopicDraft;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.PreparedStatement;

@ExtendWith(MockitoExtension.class)
public class PostgreSQLEnumTypeTest {
  @Mock
  PreparedStatement preparedStatement;

  @Mock
  SharedSessionContractImplementor session;

  private OkrTopicDraftStatusEnum statusEnum;
  private PostgreSQLEnumType postgreSQLEnumType;
  private int index;

  @BeforeEach
  public void setUp() {
    index = 0;
    statusEnum = OkrTopicDraftStatusEnum.submitted;
    postgreSQLEnumType = new PostgreSQLEnumType();
  }
// TODO fix test

//  @Test
//  public void expects_setObject_to_be_called() {
//    try {
//      postgreSQLEnumType.nullSafeSet(preparedStatement, statusEnum, index, session);
//      verify(preparedStatement).setObject(eq(index), eq(statusEnum.toString()), anyInt());
//
//    } catch (SQLException throwables) {
//      throwables.printStackTrace();
//      fail();
//    }
//  }
//
//  @Test
//  public void expects_setNull_to_be_called() {
//    try {
//      postgreSQLEnumType.nullSafeSet(preparedStatement, null, index, session);
//      verify(preparedStatement).setNull(eq(index), anyInt());
//
//    } catch (SQLException throwables) {
//      throwables.printStackTrace();
//      fail();
//    }
//  }
}
