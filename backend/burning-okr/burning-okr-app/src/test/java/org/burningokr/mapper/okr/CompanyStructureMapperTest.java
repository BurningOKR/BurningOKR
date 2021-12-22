package org.burningokr.mapper.okr;

import org.burningokr.dto.okr.CompanyStructureDto;
import org.burningokr.mapper.okrUnit.OkrBranchSchemaMapper;
import org.burningokr.model.okrUnits.*;
import org.burningokr.model.users.User;
import org.burningokr.service.userhandling.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CompanyStructureMapperTest{

    private UserService userService;
    private OkrBranchSchemaMapper branchSchemaMapper;
    private User user;

    private CompanyStructureMapper mapper;

    private OkrCompany company1;


    @Before
    public void setUp() {
        userService = mock(UserService.class);
        user = mock(User.class);
        branchSchemaMapper = new OkrBranchSchemaMapper();
        mapper = new CompanyStructureMapper(userService, branchSchemaMapper);
        company1 = new OkrCompany();
        when(userService.getCurrentUser()).thenReturn(user);
        when(user.getId()).thenReturn(UUID.randomUUID());
    }

    @Test
    public void test_mapEntityToDto_expected_name_Company1() {
        company1.setName("Company1");
        CompanyStructureDto actual = mapper.mapEntityToDto(company1);

        assertEquals("Company1", actual.getUnitName());
    }

    public void testMapEntitiesToDtos() {
    }
}