package org.burningokr.unit.mapper.okrUnit;

import org.burningokr.dto.okrUnit.OkrCompanyDto;
import org.burningokr.mapper.okrUnit.OkrCompanyMapper;
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
    public void mapEntityToDto_shouldMapId() {
        //Arrange
        Long expected = 5L;
        company.setId(expected);

        //Act
        companyDto = companyMapper.mapEntityToDto(company);

        //Assert
        assertEquals(expected, companyDto.getOkrUnitId());
    }

    @Test
    public void mapEntityToDto_shouldMapName() {
        //Arrange
        String expected = "test";
        company.setName(expected);

        //Act
        companyDto = companyMapper.mapEntityToDto(company);

        //Assert
        assertEquals(expected, companyDto.getUnitName());
    }

    @Test
    public void mapEntityToDto_shouldMapLabel() {
        //Arrange
        String expected = "t1e2s3t";
        company.setLabel(expected);

        //Act
        companyDto = companyMapper.mapEntityToDto(company);

        //Assert
        assertEquals(expected, companyDto.getLabel());
    }

    @Test
    public void mapEntityToDto_shouldMapDepartmentsSize() {
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
    public void mapEntityToDto_shouldMapObjectiveSize() {
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
    public void mapEntityToDto_shouldMapCycle() {
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
    public void mapEntityToDto_shouldMapHistory() {
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

    @Test
    public void mapEntitiesToDtos_shouldMapCompanyEntitiesToDtos() {
        company.setId(12L);
        Collection<OkrCompany> expected = new ArrayList<>() {
            {
                add(company);
                add(company);
            }
        };
        Collection<OkrCompanyDto> actual = companyMapper.mapEntitiesToDtos(expected);
        assertEquals(expected.size(), actual.size());
        assertEquals(expected.stream().findFirst().orElseThrow().getCycle().getId(), actual.stream().findFirst().orElseThrow().getCycleId());
    }

    //

    @Test
    public void mapDtoToEntity_shouldMapId() {
        //Arrange
        Long expected = 5L;
        companyDto.setOkrUnitId(expected);

        //Act
        company = companyMapper.mapDtoToEntity(companyDto);

        //Assert
        assertEquals(expected, company.getId());
    }

    @Test
    public void mapDtoToEntity_shouldMapName() {
        //Arrange
        String expected = "test";
        companyDto.setUnitName(expected);

        //Act
        company = companyMapper.mapDtoToEntity(companyDto);

        //Assert
        assertEquals(expected, company.getName());
    }

    @Test
    public void mapDtoToEntity_shouldMapLabel() {
        //Arrange
        String expected = "test2503";
        companyDto.setLabel(expected);

        //Act
        company = companyMapper.mapDtoToEntity(companyDto);

        //Assert
        assertEquals(expected, company.getLabel());
    }

    @Test
    public void mapDtoToEntity_shouldMapDepartments() {
        //Arrange

        //Act
        company = companyMapper.mapDtoToEntity(companyDto);

        //Assert
        assertNotNull(company.getOkrChildUnits());
    }

    @Test
    public void mapDtoToEntity_shouldMapObjectives() {
        //Arrange

        //Act
        company = companyMapper.mapDtoToEntity(companyDto);

        //Assert
        assertNotNull(company.getObjectives());
    }

    @Test
    public void mapDtoToEntity_shouldMapCycleId() {
        //Arrange
        Long expected = 14L;
        companyDto.setCycleId(expected);

        //Act
        company = companyMapper.mapDtoToEntity(companyDto);

        //Assert
        assertEquals(expected, company.getCycle().getId());
    }

    @Test
    public void mapDtoToEntity_shouldMapCycleIdNull() {
        //Arrange
        companyDto.setCycleId(null);

        //Act
        company = companyMapper.mapDtoToEntity(companyDto);

        //Assert
        assertNull(company.getCycle());
    }

    @Test
    public void mapDtoToEntity_shouldMapHistoryId() {
        //Arrange
        Long expected = 24L;
        companyDto.setHistoryId(expected);

        //Act
        company = companyMapper.mapDtoToEntity(companyDto);

        //Assert
        assertEquals(expected, company.getCompanyHistory().getId());
    }

    @Test
    public void mapDtoToEntity_shouldMapHistoryIdNull() {
        //Arrange
        companyDto.setHistoryId(null);

        //Act
        company = companyMapper.mapDtoToEntity(companyDto);

        //Assert
        assertNull(company.getCompanyHistory());
    }

    @Test
    public void mapDtosToEntities_shouldMapCompanyDtosToEntities() {
        companyDto.setCycleId(12L);
        Collection<OkrCompanyDto> expected = new ArrayList<>() {
            {
                add(companyDto);
                add(companyDto);
            }
        };
        Collection<OkrCompany> actual = companyMapper.mapDtosToEntities(expected);
        assertEquals(expected.size(), actual.size());
        assertEquals(expected.stream().findFirst().orElseThrow().getCycleId(), actual.stream().findFirst().orElseThrow().getCycle().getId());
    }
}
