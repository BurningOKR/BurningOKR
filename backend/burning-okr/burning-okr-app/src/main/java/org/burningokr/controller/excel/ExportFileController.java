package org.burningokr.controller.excel;

import org.apache.poi.ss.usermodel.Workbook;
import org.burningokr.annotation.RestApiController;
import org.burningokr.service.excel.XlsxDataExportFileCreatorService;
import org.burningokr.service.excel.XlsxExportContactsFileCreatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestApiController
public class ExportFileController {

  private final XlsxDataExportFileCreatorService xlsxDataExportFileCreatorService;
  private final XlsxExportContactsFileCreatorService xlsxExportContactsFileCreatorService;

  @Autowired
  public ExportFileController(
    XlsxDataExportFileCreatorService xlsxDataExportFileCreatorService,
    XlsxExportContactsFileCreatorService xlsxExportContactsFileCreatorService
  ) {
    this.xlsxDataExportFileCreatorService = xlsxDataExportFileCreatorService;
    this.xlsxExportContactsFileCreatorService = xlsxExportContactsFileCreatorService;
  }

  @GetMapping("/export/unit/{unitId}")
  public HttpEntity<byte[]> generateExcelFileForOkrTeam(
    @PathVariable long unitId
  )
    throws IOException, IllegalAccessException {
    Workbook workbook = xlsxDataExportFileCreatorService.createFileForOkrTeam(unitId);
    return getInputStreamResourceResponseEntity(workbook);
  }

  @GetMapping("/export/company/{companyId}")
  public HttpEntity<byte[]> generateExcelFileForCompany(
    @PathVariable long companyId
  )
    throws IOException, IllegalAccessException {
    Workbook workbook = xlsxDataExportFileCreatorService.createFileForCompany(companyId);
    return getInputStreamResourceResponseEntity(workbook);
  }

  @GetMapping("export/email/department/{departmentId}")
  public HttpEntity<byte[]> generateExcelEmailFileForOkrTeam(
    @PathVariable long departmentId
  )
    throws IOException, IllegalAccessException {
    Workbook workbook = xlsxExportContactsFileCreatorService.createFileForOkrTeam(departmentId);
    return getInputStreamResourceResponseEntity(workbook);
  }

  @GetMapping("export/email/company/{companyId}")
  public HttpEntity<byte[]> generateExcelEmailFileForCompany(
    @PathVariable long companyId
  )
    throws IOException, IllegalAccessException {
    Workbook workbook = xlsxExportContactsFileCreatorService.createFileForCompany(companyId);
    return getInputStreamResourceResponseEntity(workbook);
  }

  private HttpEntity<byte[]> getInputStreamResourceResponseEntity(Workbook workbook)
    throws IOException {
    byte[] excelContent = exportWorkbookToByteArrayResource(workbook);
    return new HttpEntity<>(excelContent, getHeaders());
  }

  private byte[] exportWorkbookToByteArrayResource(Workbook workbook) throws IOException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    try {
      workbook.write(bos);
    } finally {
      bos.close();
    }
    return bos.toByteArray();
  }

  private HttpHeaders getHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(
      new MediaType("application", "vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
    headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=okr.xls");
    return headers;
  }
}
