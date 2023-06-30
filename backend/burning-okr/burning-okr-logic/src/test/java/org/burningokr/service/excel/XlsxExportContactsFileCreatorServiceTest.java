package org.burningokr.service.excel;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.burningokr.model.excel.TeamMemberRow;
import org.burningokr.service.messages.Messages;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class XlsxExportContactsFileCreatorServiceTest {

  @Mock
  private TeamMemberRowBuilderService teamMemberRowBuilderService;

  @Mock
  private GenericXlsxFileCreatorService<TeamMemberRow> genericXlsxFileCreatorService;
  @Mock
  private Messages messages;

  @InjectMocks
  private XlsxExportContactsFileCreatorService xlsxExportContactsFileCreatorService;

  private final long departmentId = 42L;
  private Workbook workbook;

  @BeforeEach
  public void init() throws IllegalAccessException {
    Collection<TeamMemberRow> teamMemberRows = new ArrayList<>();
    this.workbook = new XSSFWorkbook();
    when(teamMemberRowBuilderService.generateForOkrChildUnit(departmentId))
      .thenReturn(teamMemberRows);
    when(genericXlsxFileCreatorService.createWorkbook(
      anyCollection(), anyCollection(), anyString()))
      .thenReturn(workbook);
  }

  @Test
  public void createFileForOkrTeam_shouldReturnWorkbookWhichIsReturnedByGenericFileCreatorService()
    throws IllegalAccessException {
    Workbook workbook = xlsxExportContactsFileCreatorService.createFileForOkrTeam(departmentId);
    assertEquals(this.workbook, workbook);
    verify(teamMemberRowBuilderService, times(1)).generateForOkrChildUnit(departmentId);
  }

// TODO fix test

  @Test
  public void createFileForCompany_shouldReturnExcelWithJustHeaderRowIfTeamMemberRowIsEmpty()
    throws IllegalAccessException {
    long companyId = 44L;
    Workbook workbook = xlsxExportContactsFileCreatorService.createFileForCompany(companyId);
    assertEquals(this.workbook, workbook);
    verify(teamMemberRowBuilderService, times(1)).generateForCompany(companyId);
//    long companyId = 44L;
//    Workbook workbook = xlsxExportContactsFileCreatorService.createFileForCompany(companyId);
//    assertEquals(this.workbook, workbook);
//    verify(teamMemberRowBuilderService, times(1)).generateForCompany(companyId);
  }
}
