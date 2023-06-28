package org.burningokr.mapper.okrUnit;

import org.burningokr.dto.okrUnit.OkrCompanyDto;
import org.burningokr.model.cycles.Cycle;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.okrUnits.OkrChildUnit;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.model.okrUnits.okrUnitHistories.OkrCompanyHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

//TODO (F. L. 26.06.2023) Refactor tests (name, etc.)
public class OkrCompanyMapperTest {

    private OkrCompany company;
    private OkrCompanyDto companyDto;
    private OkrCompanyMapper companyMapper;

    @BeforeEach
    public void reset() {
        this.company = new OkrCompany();
        this.companyDto = new OkrCompanyDto();
        this.companyMapper = new OkrCompanyMapper();

        Cycle cycle = new Cycle();
        cycle.setId(14L);
        company.setCycle(cycle);

        OkrCompanyHistory history = new OkrCompanyHistory();
        history.setId(24L);
        company.setCompanyHistory(history);
    }

    @Test
    public void test_mapEntityToDto_expects_idIsMapped() {
        //Arrange
        Long expected = 5L;
        company.setId(expected);

        //Act
        companyDto = companyMapper.mapEntityToDto(company);

        //Assert
        assertEquals(expected, companyDto.getOkrUnitId());
    }

    @Test
    public void test_mapEntityToDto_expects_nameIsMapped() {
        //Arrange
        String expected = "test";
        company.setName(expected);

        //Act
        companyDto = companyMapper.mapEntityToDto(company);

        //Assert
        assertEquals(expected, companyDto.getUnitName());
    }

    @Test
    public void test_mapEntityToDto_expects_labelIsMapped() {
        //Arrange
        String expected = "t1e2s3t";
        company.setLabel(expected);

        //Act
        companyDto = companyMapper.mapEntityToDto(company);

        //Assert
        assertEquals(expected, companyDto.getLabel());
    }

    @Test
    public void test_mapEntityToDto_expects_departmentsSizeIsEqual4() {
        //Arrange
        int expected = 4;
        Collection<OkrChildUnit> departments = new ArrayList<>() {
            {
                add(new OkrDepartment());
                add(new OkrDepartment());
                add(new OkrDepartment());
                add(new OkrDepartment());
            }
        };
        company.setOkrChildUnits(departments);

        //Act
        companyDto = companyMapper.mapEntityToDto(company);

        //Assert
        assertEquals(expected, companyDto.getOkrChildUnitIds().size());
    }

    @Test
    public void test_mapEntityToDto_expects_objectiveSizeIsEqual() {
        //Arrange
        int expected = 4;
        Collection<Objective> objectives = new ArrayList<>() {
            {
                add(new Objective());
                add(new Objective());
                add(new Objective());
                add(new Objective());
            }
        };
        company.setObjectives(objectives);

        //Act
        companyDto = companyMapper.mapEntityToDto(company);

        //Assert
        assertEquals(expected, companyDto.getObjectiveIds().size());
    }

    @Test
    public void test_mapEntityToDto_expects_cycleIsMapped() {
        //Arrange
        Long expected = 14L;
        Cycle cycle = new Cycle();
        cycle.setId(expected);
        company.setCycle(cycle);

        //Act
        companyDto = companyMapper.mapEntityToDto(company);

        //Assert
        assertEquals(expected, companyDto.getCycleId());
    }

    @Test
    public void test_mapEntityToDto_expects_historyIsMapped() {
        //Arrange
        Long expected = 27L;
        OkrCompanyHistory history = new OkrCompanyHistory();
        history.setId(expected);
        company.setCompanyHistory(history);

        //Act
        companyDto = companyMapper.mapEntityToDto(company);

        //Assert
        assertEquals(expected, companyDto.getHistoryId());
    }

    //

    @Test
    public void test_mapDtoToEntity_expects_idIsMapped() {
        //Arrange
        Long expected = 5L;
        companyDto.setOkrUnitId(expected);

        //Act
        company = companyMapper.mapDtoToEntity(companyDto);

        //Assert
        assertEquals(expected, company.getId());
    }

    @Test
    public void test_mapDtoToEntity_expects_nameIsMapped() {
        //Arrange
        String expected = "test";
        companyDto.setUnitName(expected);

        //Act
        company = companyMapper.mapDtoToEntity(companyDto);

        //Assert
        assertEquals(expected, company.getName());
    }

    @Test
    public void test_mapDtoToEntity_expects_labelIsMapped() {
        //Arrange
        String expected = "test2503";
        companyDto.setLabel(expected);

        //Act
        company = companyMapper.mapDtoToEntity(companyDto);

        //Assert
        assertEquals(expected, company.getLabel());
    }

    @Test
    public void test_mapDtoToEntity_expects_departmentsNotNull() {
        //Arrange

        //Act
        company = companyMapper.mapDtoToEntity(companyDto);

        //Assert
        assertNotNull(company.getOkrChildUnits());
    }

    @Test
    public void test_mapDtoToEntity_expects_objectivesNotNull() {
        //Arrange

        //Act
        company = companyMapper.mapDtoToEntity(companyDto);

        //Assert
        assertNotNull(company.getObjectives());
    }

    @Test
    public void test_mapDtoToEntity_expects_cycleIdIsMappedNotNull() {
        //Arrange
        Long expected = 14L;
        companyDto.setCycleId(expected);

        //Act
        company = companyMapper.mapDtoToEntity(companyDto);

        //Assert
        assertEquals(expected, company.getCycle().getId());
    }

    @Test
    public void test_mapDtoToEntity_expects_cycleIdIsMappedNull() {
        //Arrange
        companyDto.setCycleId(null);

        //Act
        company = companyMapper.mapDtoToEntity(companyDto);

        //Assert
        assertNull(company.getCycle());
    }

    @Test
    public void test_mapDtoToEntity_expects_historyIdIsMappedNotNull() {
        //Arrange
        Long expected = 24L;
        companyDto.setHistoryId(expected);

        //Act
        company = companyMapper.mapDtoToEntity(companyDto);

        //Assert
        assertEquals(expected, company.getCompanyHistory().getId());
    }

    @Test
    public void test_mapDtoToEntity_expects_historyIdIsMappedNull() {
        //Arrange
        companyDto.setHistoryId(null);

        //Act
        company = companyMapper.mapDtoToEntity(companyDto);

        //Assert
        assertNull(company.getCompanyHistory());
    }
}
