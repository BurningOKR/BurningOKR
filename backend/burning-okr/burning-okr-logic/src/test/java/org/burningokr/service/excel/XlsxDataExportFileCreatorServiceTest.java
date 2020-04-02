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
import org.burningokr.model.excel.ObjectiveRow;
import org.burningokr.service.messages.Messages;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class XlsxDataExportFileCreatorServiceTest {

  @Mock private ObjectiveRowBuilderService objectiveRowBuilderService;

  @Mock private GenericXlsxFileCreatorService<ObjectiveRow> genericXlsxFileCreatorService;

  @Mock private Messages messages;

  @InjectMocks private XlsxDataExportFileCreatorService xlsxDataExportFileCreatorService;

  private long departmentId = 42L;
  private long companyId = 44L;
  private Collection<ObjectiveRow> objectiveRowCollection;
  private Workbook workbook;

  @Before
  public void before() throws IllegalAccessException {
    objectiveRowCollection = new ArrayList<>();
    this.workbook = new XSSFWorkbook();
    when(objectiveRowBuilderService.generateForDepartment(departmentId))
        .thenReturn(objectiveRowCollection);
    when(objectiveRowBuilderService.generateForCompany(companyId))
        .thenReturn(objectiveRowCollection);
    when(genericXlsxFileCreatorService.createWorkbook(
            anyCollection(), anyCollection(), anyString()))
        .thenReturn(workbook);
  }

  @Test
  public void createFileForOkrTeam_ShouldReturnWorkWhichIsReturnedByGenericFileCreatorService()
      throws IllegalAccessException {
    Workbook workbook = xlsxDataExportFileCreatorService.createFileForOkrTeam(departmentId);
    Assert.assertEquals(this.workbook, workbook);
    verify(objectiveRowBuilderService, times(1)).generateForDepartment(departmentId);
  }

  @Test
  public void createFileForCompany_ShouldReturnWorkWhichIsReturnedByGenericFileCreatorService()
      throws IllegalAccessException {
    Workbook workbook = xlsxDataExportFileCreatorService.createFileForCompany(companyId);
    Assert.assertEquals(this.workbook, workbook);
    verify(objectiveRowBuilderService, times(1)).generateForCompany(companyId);
  }
}
