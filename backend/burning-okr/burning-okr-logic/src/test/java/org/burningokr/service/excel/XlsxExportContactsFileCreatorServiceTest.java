package org.burningokr.service.excel;

import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.burningokr.model.excel.TeamMemberRow;
import org.burningokr.service.messages.Messages;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class XlsxExportContactsFileCreatorServiceTest {

  @Mock private TeamMemberRowBuilderService teamMemberRowBuilderService;

  @Mock private GenericXlsxFileCreatorService<TeamMemberRow> genericXlsxFileCreatorService;

  @Mock private Messages messages;

  @InjectMocks private XlsxExportContactsFileCreatorService xlsxExportContactsFileCreatorService;

  private Collection<TeamMemberRow> teamMemberRows;
  private long departmentId = 42L;
  private long companyId = 44L;
  private Workbook workbook;

  @Before
  public void init() throws IllegalAccessException {
    teamMemberRows = new ArrayList<>();
    this.workbook = new XSSFWorkbook();
    when(teamMemberRowBuilderService.generateForOkrChildUnit(departmentId))
        .thenReturn(teamMemberRows);
    when(teamMemberRowBuilderService.generateForCompany(companyId)).thenReturn(teamMemberRows);
    when(genericXlsxFileCreatorService.createWorkbook(
            anyCollection(), anyCollection(), anyString()))
        .thenReturn(workbook);
  }

  @Test
  public void createFileForOkrTeam_shouldReturnWorkbookWhichIsReturnedByGenericFileCreatorService()
      throws IllegalAccessException {
    Workbook workbook = xlsxExportContactsFileCreatorService.createFileForOkrTeam(departmentId);
    Assert.assertEquals(this.workbook, workbook);
    verify(teamMemberRowBuilderService, times(1)).generateForOkrChildUnit(departmentId);
  }

  @Test
  public void createFileForOkrTeam_shouldReturnExcelWithJustHeaderRowIfTeamMemberRowIsEmpty()
      throws IllegalAccessException {
    Workbook workbook = xlsxExportContactsFileCreatorService.createFileForCompany(companyId);
    Assert.assertEquals(this.workbook, workbook);
    verify(teamMemberRowBuilderService, times(1)).generateForCompany(companyId);
  }
}
