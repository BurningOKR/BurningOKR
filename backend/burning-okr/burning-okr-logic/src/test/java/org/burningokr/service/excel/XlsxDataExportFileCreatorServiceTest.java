package org.burningokr.service.excel;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.burningokr.model.excel.ObjectiveRow;
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
public class XlsxDataExportFileCreatorServiceTest {

  @Mock
  private ObjectiveRowBuilderService objectiveRowBuilderService;

  @Mock
  private GenericXlsxFileCreatorService<ObjectiveRow> genericXlsxFileCreatorService;

  @Mock
  private Messages messages;

  @InjectMocks
  private XlsxDataExportFileCreatorService xlsxDataExportFileCreatorService;

  private long departmentId = 42L;
  private long companyId = 44L;
  private Collection<ObjectiveRow> objectiveRowCollection;
  private Workbook workbook;

  @BeforeEach
  public void before() throws IllegalAccessException {
    objectiveRowCollection = new ArrayList<>();
    this.workbook = new XSSFWorkbook();
    when(genericXlsxFileCreatorService.createWorkbook(
      anyCollection(), anyCollection(), anyString()))
      .thenReturn(workbook);
  }

  @Test
  public void createFileForOkrTeam_ShouldReturnWorkWhichIsReturnedByGenericFileCreatorService()
    throws IllegalAccessException {
    Workbook workbook = xlsxDataExportFileCreatorService.createFileForOkrTeam(departmentId);
    assertEquals(this.workbook, workbook);
    verify(objectiveRowBuilderService, times(1)).generateForOkrChildUnit(departmentId);
  }

  @Test
  public void createFileForCompany_ShouldReturnWorkWhichIsReturnedByGenericFileCreatorService()
    throws IllegalAccessException {
    Workbook workbook = xlsxDataExportFileCreatorService.createFileForCompany(companyId);
    assertEquals(this.workbook, workbook);
    verify(objectiveRowBuilderService, times(1)).generateForCompany(companyId);
  }
}
