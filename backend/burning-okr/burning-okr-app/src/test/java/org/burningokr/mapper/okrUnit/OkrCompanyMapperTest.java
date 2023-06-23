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

public class OkrCompanyMapperTest {

    private OkrCompany okrCompany;
    private OkrCompanyDto okrCompanyDto;
    private OkrCompanyMapper okrCompanyMapper;

    @BeforeEach
    public void reset() {
        this.okrCompany = new OkrCompany();
        this.okrCompanyDto = new OkrCompanyDto();
        this.okrCompanyMapper = new OkrCompanyMapper();

        Cycle cycle = new Cycle();
        cycle.setId(14L);
        okrCompany.setCycle(cycle);

        OkrCompanyHistory history = new OkrCompanyHistory();
        history.setId(24L);
        okrCompany.setCompanyHistory(history);
    }

    @Test
    public void test_mapEntityToDto_expects_idIsMapped() {
        //Arrange
        Long expected = 5L;
        okrCompany.setId(expected);

        //Act
        okrCompanyDto = okrCompanyMapper.mapEntityToDto(okrCompany);

        //Assert
        assertEquals(expected, okrCompanyDto.getOkrUnitId());
    }

    @Test
    public void test_mapEntityToDto_expects_nameIsMapped() {
        //Arrange
        String expected = "test";
        okrCompany.setName(expected);

        //Act
        okrCompanyDto = okrCompanyMapper.mapEntityToDto(okrCompany);

        //Assert
        assertEquals(expected, okrCompanyDto.getUnitName());
    }

    @Test
    public void test_mapEntityToDto_expects_labelIsMapped() {
        //Arrange
        String expected = "t1e2s3t";
        okrCompany.setLabel(expected);

        //Act
        okrCompanyDto = okrCompanyMapper.mapEntityToDto(okrCompany);

        //Assert
        assertEquals(expected, okrCompanyDto.getLabel());
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
        okrCompany.setOkrChildUnits(departments);

        //Act
        okrCompanyDto = okrCompanyMapper.mapEntityToDto(okrCompany);

        //Assert
        assertEquals(expected, okrCompanyDto.getOkrChildUnitIds().size());
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
        okrCompany.setObjectives(objectives);

        //Act
        okrCompanyDto = okrCompanyMapper.mapEntityToDto(okrCompany);

        //Assert
        assertEquals(expected, okrCompanyDto.getObjectiveIds().size());
    }

    @Test
    public void test_mapEntityToDto_expects_cycleIsMapped() {
        //Arrange
        Long expected = 14L;
        Cycle cycle = new Cycle();
        cycle.setId(expected);
        okrCompany.setCycle(cycle);

        //Act
        okrCompanyDto = okrCompanyMapper.mapEntityToDto(okrCompany);

        //Assert
        assertEquals(expected, okrCompanyDto.getCycleId());
    }

    @Test
    public void test_mapEntityToDto_expects_historyIsMapped() {
        //Arrange
        Long expected = 27L;
        OkrCompanyHistory history = new OkrCompanyHistory();
        history.setId(expected);
        okrCompany.setCompanyHistory(history);

        //Act
        okrCompanyDto = okrCompanyMapper.mapEntityToDto(okrCompany);

        //Assert
        assertEquals(expected, okrCompanyDto.getHistoryId());
    }

    //

    @Test
    public void test_mapDtoToEntity_expects_idIsMapped() {
        //Arrange
        Long expected = 5L;
        okrCompanyDto.setOkrUnitId(expected);

        //Act
        okrCompany = okrCompanyMapper.mapDtoToEntity(okrCompanyDto);

        //Assert
        assertEquals(expected, okrCompany.getId());
    }

    @Test
    public void test_mapDtoToEntity_expects_nameIsMapped() {
        //Arrange
        String expected = "test";
        okrCompanyDto.setUnitName(expected);

        //Act
        okrCompany = okrCompanyMapper.mapDtoToEntity(okrCompanyDto);

        //Assert
        assertEquals(expected, okrCompany.getName());
    }

    @Test
    public void test_mapDtoToEntity_expects_labelIsMapped() {
        //Arrange
        String expected = "test2503";
        okrCompanyDto.setLabel(expected);

        //Act
        okrCompany = okrCompanyMapper.mapDtoToEntity(okrCompanyDto);

        //Assert
        assertEquals(expected, okrCompany.getLabel());
    }

    @Test
    public void test_mapDtoToEntity_expects_departmentsNotNull() {
        //Arrange

        //Act
        okrCompany = okrCompanyMapper.mapDtoToEntity(okrCompanyDto);

        //Assert
        assertNotNull(okrCompany.getOkrChildUnits());
    }

    @Test
    public void test_mapDtoToEntity_expects_objectivesNotNull() {
        //Arrange

        //Act
        okrCompany = okrCompanyMapper.mapDtoToEntity(okrCompanyDto);

        //Assert
        assertNotNull(okrCompany.getObjectives());
    }

    @Test
    public void test_mapDtoToEntity_expects_cycleIdIsMappedNotNull() {
        //Arrange
        Long expected = 14L;
        okrCompanyDto.setCycleId(expected);

        //Act
        okrCompany = okrCompanyMapper.mapDtoToEntity(okrCompanyDto);

        //Assert
        assertEquals(expected, okrCompany.getCycle().getId());
    }

    @Test
    public void test_mapDtoToEntity_expects_cycleIdIsMappedNull() {
        //Arrange
        okrCompanyDto.setCycleId(null);

        //Act
        okrCompany = okrCompanyMapper.mapDtoToEntity(okrCompanyDto);

        //Assert
        assertNull(okrCompany.getCycle());
    }

    @Test
    public void test_mapDtoToEntity_expects_historyIdIsMappedNotNull() {
        //Arrange
        Long expected = 24L;
        okrCompanyDto.setHistoryId(expected);

        //Act
        okrCompany = okrCompanyMapper.mapDtoToEntity(okrCompanyDto);

        //Assert
        assertEquals(expected, okrCompany.getCompanyHistory().getId());
    }

    @Test
    public void test_mapDtoToEntity_expects_historyIdIsMappedNull() {
        //Arrange
        okrCompanyDto.setHistoryId(null);

        //Act
        okrCompany = okrCompanyMapper.mapDtoToEntity(okrCompanyDto);

        //Assert
        assertNull(okrCompany.getCompanyHistory());
    }
}
