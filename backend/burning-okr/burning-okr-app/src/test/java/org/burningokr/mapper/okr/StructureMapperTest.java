package org.burningokr.mapper.okr;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.UUID;
import org.burningokr.dto.okr.StructureDto;
import org.burningokr.mapper.okrUnit.OkrBranchSchemaMapper;
import org.burningokr.model.okrUnits.*;
import org.burningokr.model.users.User;
import org.burningokr.service.userhandling.UserService;
import org.junit.Before;
import org.junit.Test;

public class StructureMapperTest {

  private UserService userService;
  private OkrBranchSchemaMapper branchSchemaMapper;
  private User user;
  private OkrChildUnit childUnit;
  private Collection<OkrChildUnit> childUnits;
  private StructureMapper mapper;
  private OkrCompany company1;

  @Before
  public void setUp() {
    userService = mock(UserService.class);
    user = mock(User.class);
    branchSchemaMapper = new OkrBranchSchemaMapper();
    mapper = new StructureMapper(userService, branchSchemaMapper);
    company1 = new OkrCompany();
    childUnit = mock(OkrChildUnit.class);
    when(userService.getCurrentUser()).thenReturn(user);
    when(user.getId()).thenReturn(UUID.randomUUID());
  }

  @Test
  public void test_mapCompanyToDto_expected_name_Company1() {
    company1.setName("Company1");

    StructureDto actual = mapper.mapCompnayToStructureDto(company1);

    assertEquals("Company1", actual.getUnitName());
  }

  // Can not be tested yet, as JUnit 3.x does not support mocking of static methods (used by
  // Branchhelper)
  /*@Test
  public void test_mapCompanyToDto_inputChildUnit_expectedSubstructureAsStrutureDTos() {
    childUnits = new ArrayList<>();
    childUnits.add(childUnit);
    company1.setOkrChildUnits(childUnits);

    StructureDto dto = mapper.mapCompnayToStructureDto(company1);
    Object actual = dto.getSubstructure().iterator().next();

    assertEquals(StructureDto.class, actual.getClass());
  }*/
}
