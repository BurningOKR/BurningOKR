package org.burningokr.service.excel;

import jakarta.validation.constraints.NotNull;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.burningokr.model.excel.ColumnIndex;
import org.burningokr.model.excel.PercentageCellValue;
import org.burningokr.service.exceptions.MissingAnnotationException;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;

@Service
public class GenericXlsxFileCreatorService<T> {

  /**
   * Creates a Workbook.
   *
   * @param contentRows a {@link Collection} of {@link T} objects
   * @param headlines   a {@link Collection} of String values
   * @param sheetTitle  a String value
   * @return a {@link Workbook} object
   * @throws IllegalAccessException if the name is null or invalid or workbook already contains a
   *                                sheet with this name
   */
  public Workbook createWorkbook(
    @NotNull Collection<T> contentRows,
    @NotNull Collection<String> headlines, String sheetTitle
  )
    throws IllegalAccessException {
    Workbook workbook = new XSSFWorkbook();
    Sheet sheet = workbook.createSheet(sheetTitle);

    createHeadlines(sheet, headlines);
    fillOutSheet(workbook, sheet, contentRows);
    autosizeColumns(sheet, headlines.size());

    return workbook;
  }

  private void createHeadlines(Sheet sheet, Collection<String> headlines) {
    int rowIndex = 0;

    Row row = sheet.createRow(rowIndex);

    for (int cellIndex = 0; cellIndex < headlines.size(); cellIndex++) {
      row.createCell(cellIndex).setCellValue((String) headlines.toArray()[cellIndex]);
    }
  }

  private void fillOutSheet(Workbook workbook, Sheet sheet, Collection<T> contentRows)
    throws IllegalAccessException {
    int rowIndex = 1;

    for (T content : contentRows) {
      Row row = sheet.createRow(rowIndex);
      int cellIndex = 0;
      Field[] classFields = content.getClass().getDeclaredFields();
      sortFieldDependingOnColumnIndex(classFields);
      for (Field field : classFields) {
        field.setAccessible(true); // to get access for private fields
        Class type = field.getType();
        Object value = field.get(content);
        if (type == String.class) {
          fillOutCell(row, cellIndex, String.valueOf(value));
        } else if (type == PercentageCellValue.class) {
          fillOutCell(workbook, row, cellIndex, (PercentageCellValue) value);
        } else if (value instanceof Long || value instanceof Integer) {
          fillOutCell(row, cellIndex, (long) value);
        } else if (value instanceof Double || value instanceof Float) {
          fillOutCell(row, cellIndex, (float) value);
        }
        cellIndex++;
      }
      rowIndex++;
    }
  }

  /*
   * Reflection is not order safe - on different platforms can the order be different
   */
  private void sortFieldDependingOnColumnIndex(Field[] fields) throws MissingAnnotationException {
    Arrays.sort(
      fields,
      (o1, o2) -> {
        ColumnIndex columnIndex1 = o1.getAnnotation(ColumnIndex.class);
        ColumnIndex columnIndex2 = o2.getAnnotation(ColumnIndex.class);
        if (columnIndex1 != null && columnIndex2 != null) {
          return columnIndex1.value() - columnIndex2.value();
        }
        throw new MissingAnnotationException(
          "One or both fields are not annotated with the @ColumnIndex annotation");
      }
    );
  }

  private void fillOutCell(Row row, int cellIndex, String value) {
    row.createCell(cellIndex).setCellValue(value);
  }

  private void fillOutCell(Row row, int cellIndex, long value) {
    row.createCell(cellIndex).setCellValue(value);
  }

  private void fillOutCell(Row row, int cellIndex, float value) {
    row.createCell(cellIndex).setCellValue(value);
  }

  private void fillOutCell(Workbook workbook, Row row, int cellIndex, PercentageCellValue value) {
    row.createCell(cellIndex).setCellValue(value.getValue());
    CellStyle stylePercentage = workbook.createCellStyle();
    stylePercentage.setDataFormat(
      workbook.createDataFormat().getFormat(BuiltinFormats.getBuiltinFormat(10)));
    row.getCell(cellIndex).setCellStyle(stylePercentage);
  }

  private void autosizeColumns(Sheet sheet, int columnLength) {
    for (int columnIndex = 0; columnIndex < columnLength; columnIndex++) {
      sheet.autoSizeColumn(columnIndex);
    }
  }
}
