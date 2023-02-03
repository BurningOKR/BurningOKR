package org.burningokr.service.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.burningokr.model.excel.ObjectiveRow;
import org.burningokr.model.excel.PercentageCellValue;
import org.burningokr.model.excel.TeamMemberRow;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class GenericXlsxFileCreatorServiceTest {

  GenericXlsxFileCreatorService<TeamMemberRow> teamMemberXlsxFileCreatorService;

  GenericXlsxFileCreatorService<ObjectiveRow> objectiveRowXlsxFileCreatorService;

  @Before
  public void init() {
    teamMemberXlsxFileCreatorService = new GenericXlsxFileCreatorService<>();
    objectiveRowXlsxFileCreatorService = new GenericXlsxFileCreatorService<>();
  }

  @Test
  public void createWorkbook_shouldCreateTeamMemberWorkbookWithHeadlineRowAndFillThemOutCorrectly()
      throws IllegalAccessException {
    Collection<String> headlines = Arrays.asList("h1", "h2", "h3", "h4");
    String sheetTitle = "okr-members";

    Workbook workbook =
        teamMemberXlsxFileCreatorService.createWorkbook(new ArrayList<>(), headlines, sheetTitle);

    Sheet sheet = workbook.getSheet(sheetTitle);

    Assert.assertEquals(1, sheet.getPhysicalNumberOfRows());
    Assert.assertEquals("h1", getStringContentOf(sheet, 0, 0));
    Assert.assertEquals("h2", getStringContentOf(sheet, 0, 1));
    Assert.assertEquals("h3", getStringContentOf(sheet, 0, 2));
    Assert.assertEquals("h4", getStringContentOf(sheet, 0, 3));
  }

  @Test
  public void createWorkbook_shouldCreateObjectiveRowWorkbookWithHeadlines()
      throws IllegalAccessException {
    Collection<String> headlines =
        Arrays.asList(
            "Team",
            "Objective",
            "Fortschritt",
            "Uebergeordnetes Unternehemensziel",
            "Key Result",
            "Beschreibung",
            "Start",
            "Ende",
            "Aktuell",
            "Einheit");
    String sheetTitle = "okr-members";

    Workbook workbook =
        objectiveRowXlsxFileCreatorService.createWorkbook(new ArrayList<>(), headlines, sheetTitle);

    Sheet sheet = workbook.getSheet(sheetTitle);

    Assert.assertEquals(1, sheet.getPhysicalNumberOfRows());
    Assert.assertEquals("Team", getStringContentOf(sheet, 0, 0));
    Assert.assertEquals("Objective", getStringContentOf(sheet, 0, 1));
    Assert.assertEquals("Fortschritt", getStringContentOf(sheet, 0, 2));
    Assert.assertEquals("Uebergeordnetes Unternehemensziel", getStringContentOf(sheet, 0, 3));
    Assert.assertEquals("Key Result", getStringContentOf(sheet, 0, 4));
    Assert.assertEquals("Beschreibung", getStringContentOf(sheet, 0, 5));
    Assert.assertEquals("Start", getStringContentOf(sheet, 0, 6));
    Assert.assertEquals("Ende", getStringContentOf(sheet, 0, 7));
    Assert.assertEquals("Aktuell", getStringContentOf(sheet, 0, 8));
    Assert.assertEquals("Einheit", getStringContentOf(sheet, 0, 9));
  }

  @Test
  public void createWorkbook_shouldTeamMemberCreateWorkbookShouldFillOutWorkBookCorrectly()
      throws IllegalAccessException {
    Collection<String> headlines = Arrays.asList("h1", "h2", "h3", "h4");
    String sheetTitle = "okr-members";
    TeamMemberRow row1 = new TeamMemberRow("team1", "role1", "name1", "email1");
    TeamMemberRow row2 = new TeamMemberRow("team2", "role2", "name2", "email2");
    TeamMemberRow row3 = new TeamMemberRow("team3", "role3", "name3", "email3");

    Collection<TeamMemberRow> teamMemberRows = Arrays.asList(row1, row2, row3);

    Workbook workbook =
        teamMemberXlsxFileCreatorService.createWorkbook(teamMemberRows, headlines, sheetTitle);

    Sheet sheet = workbook.getSheet(sheetTitle);

    Assert.assertEquals(4, sheet.getPhysicalNumberOfRows());
    Assert.assertEquals("h1", getStringContentOf(sheet, 0, 0));
    Assert.assertEquals("h2", getStringContentOf(sheet, 0, 1));
    Assert.assertEquals("h3", getStringContentOf(sheet, 0, 2));
    Assert.assertEquals("h4", getStringContentOf(sheet, 0, 3));
    Assert.assertEquals(row1.getTeamName(), getStringContentOf(sheet, 1, 0));
    Assert.assertEquals(row1.getRole(), getStringContentOf(sheet, 1, 1));
    Assert.assertEquals(row1.getFullName(), getStringContentOf(sheet, 1, 2));
    Assert.assertEquals(row1.getEmailAddress(), getStringContentOf(sheet, 1, 3));
    Assert.assertEquals(row2.getTeamName(), getStringContentOf(sheet, 2, 0));
    Assert.assertEquals(row2.getRole(), getStringContentOf(sheet, 2, 1));
    Assert.assertEquals(row2.getFullName(), getStringContentOf(sheet, 2, 2));
    Assert.assertEquals(row2.getEmailAddress(), getStringContentOf(sheet, 2, 3));
    Assert.assertEquals(row3.getTeamName(), getStringContentOf(sheet, 3, 0));
    Assert.assertEquals(row3.getRole(), getStringContentOf(sheet, 3, 1));
    Assert.assertEquals(row3.getFullName(), getStringContentOf(sheet, 3, 2));
    Assert.assertEquals(row3.getEmailAddress(), getStringContentOf(sheet, 3, 3));
  }

  @Test
  public void
      createWorkbook_shouldCreateObjectiveRowWorkbookWithHeadlineRowAndFillThemOutCorrectly()
          throws IllegalAccessException {
    Collection<String> headlines =
        Arrays.asList(
            "Team",
            "Objective",
            "Fortschritt",
            "Uebergeordnetes Unternehemensziel",
            "Key Result",
            "Beschreibung",
            "Start",
            "Ende",
            "Aktuell",
            "Einheit");
    String sheetTitle = "okr-members";

    ObjectiveRow row1 =
        new ObjectiveRow(
            "Workflow (Camunda)",
            "Wir begeistern unsere Kollegen für Camunda",
            new PercentageCellValue(15f),
            "Wir verstehen die Bedüfnisse...",
            "Wir haben 10 interessante...",
            "Workshopideen...",
            0,
            10,
            1,
            "Anzahl");
    ObjectiveRow row2 =
        new ObjectiveRow(
            "Workflow (Camunda)",
            "Wie begeistern unsre Kollegen für...",
            new PercentageCellValue(15f),
            "Wir verstehen...",
            "Wir kommunizieren...",
            "z.b. Projektbericht...",
            0,
            5,
            1,
            "Anzahl");

    Collection<ObjectiveRow> objectiveRows = Arrays.asList(row1, row2);

    Workbook workbook =
        objectiveRowXlsxFileCreatorService.createWorkbook(objectiveRows, headlines, sheetTitle);

    Sheet sheet = workbook.getSheet(sheetTitle);

    Assert.assertEquals(3, sheet.getPhysicalNumberOfRows());
    Assert.assertEquals("Team", getStringContentOf(sheet, 0, 0));
    Assert.assertEquals("Objective", getStringContentOf(sheet, 0, 1));
    Assert.assertEquals("Fortschritt", getStringContentOf(sheet, 0, 2));
    Assert.assertEquals("Uebergeordnetes Unternehemensziel", getStringContentOf(sheet, 0, 3));
    Assert.assertEquals("Key Result", getStringContentOf(sheet, 0, 4));
    Assert.assertEquals("Beschreibung", getStringContentOf(sheet, 0, 5));
    Assert.assertEquals("Start", getStringContentOf(sheet, 0, 6));
    Assert.assertEquals("Ende", getStringContentOf(sheet, 0, 7));
    Assert.assertEquals("Aktuell", getStringContentOf(sheet, 0, 8));
    Assert.assertEquals("Einheit", getStringContentOf(sheet, 0, 9));
    Assert.assertEquals(row1.getTeam(), getStringContentOf(sheet, 1, 0));
    Assert.assertEquals(row1.getObjective(), getStringContentOf(sheet, 1, 1));
    Assert.assertEquals(
        String.valueOf(row1.getProgress().getValue()), getStringContentOf(sheet, 1, 2));
    Assert.assertEquals(row1.getParentUnitGoal(), getStringContentOf(sheet, 1, 3));
    Assert.assertEquals(row1.getKeyResult(), getStringContentOf(sheet, 1, 4));
    Assert.assertEquals(row1.getDescription(), getStringContentOf(sheet, 1, 5));
    Assert.assertEquals((float) row1.getStart(), getFloatValueOf(sheet, 1, 6), 0.0f);
    Assert.assertEquals((float) row1.getEnd(), getFloatValueOf(sheet, 1, 7), 0.0f);
    Assert.assertEquals((float) row1.getActual(), getFloatValueOf(sheet, 1, 8), 0.0f);
    Assert.assertEquals(row1.getUnit(), getStringContentOf(sheet, 1, 9));
    Assert.assertEquals(row2.getTeam(), getStringContentOf(sheet, 2, 0));
    Assert.assertEquals(row2.getObjective(), getStringContentOf(sheet, 2, 1));
    Assert.assertEquals(
        String.valueOf(row2.getProgress().getValue()), getStringContentOf(sheet, 2, 2));
    Assert.assertEquals(row2.getParentUnitGoal(), getStringContentOf(sheet, 2, 3));
    Assert.assertEquals(String.valueOf(row2.getKeyResult()), getStringContentOf(sheet, 2, 4));
    Assert.assertEquals(row2.getDescription(), getStringContentOf(sheet, 2, 5));
    Assert.assertEquals((float) row2.getStart(), getFloatValueOf(sheet, 2, 6), 0.0f);
    Assert.assertEquals((float) row2.getEnd(), getFloatValueOf(sheet, 2, 7), 0.0f);
    Assert.assertEquals((float) row2.getActual(), getFloatValueOf(sheet, 2, 8), 0.0f);
    Assert.assertEquals(row2.getUnit(), getStringContentOf(sheet, 2, 9));
  }

  @Test
  public void createWorkbook_shouldCreateObjectiveRowWorkbookWithCorrectCellTypes()
      throws IllegalAccessException {
    Collection<String> headlines =
        Arrays.asList(
            "Team",
            "Objective",
            "Fortschritt",
            "Uebergeordnetes Unternehemensziel",
            "Key Result",
            "Beschreibung",
            "Start",
            "Ende",
            "Aktuell",
            "Einheit");
    String sheetTitle = "okr-members";

    ObjectiveRow row1 =
        new ObjectiveRow(
            "Workflow (Camunda)",
            "Wir begeistern unsere Kollegen für Camunda",
            new PercentageCellValue(15f),
            "Wir verstehen die Bedüfnisse...",
            "Wir haben 10 interessante...",
            "Workshopideen...",
            0,
            10,
            1,
            "Anzahl");
    ObjectiveRow row2 =
        new ObjectiveRow(
            "Workflow (Camunda)",
            "Wie begeistern unsre Kollegen für...",
            new PercentageCellValue(15f),
            "Wir verstehen...",
            "Wir kommunizieren...",
            "z.b. Projektbericht...",
            0,
            5,
            1,
            "Anzahl");

    Collection<ObjectiveRow> objectiveRows = Arrays.asList(row1, row2);

    Workbook workbook =
        objectiveRowXlsxFileCreatorService.createWorkbook(objectiveRows, headlines, sheetTitle);

    Sheet sheet = workbook.getSheet(sheetTitle);

    Assert.assertEquals(3, sheet.getPhysicalNumberOfRows());
    Assert.assertEquals(Cell.CELL_TYPE_STRING, getTypeOf(sheet, 0, 0));
    Assert.assertEquals(Cell.CELL_TYPE_STRING, getTypeOf(sheet, 0, 1));
    Assert.assertEquals(Cell.CELL_TYPE_STRING, getTypeOf(sheet, 0, 2));
    Assert.assertEquals(Cell.CELL_TYPE_STRING, getTypeOf(sheet, 0, 3));
    Assert.assertEquals(Cell.CELL_TYPE_STRING, getTypeOf(sheet, 0, 4));
    Assert.assertEquals(Cell.CELL_TYPE_STRING, getTypeOf(sheet, 0, 5));
    Assert.assertEquals(Cell.CELL_TYPE_STRING, getTypeOf(sheet, 0, 6));
    Assert.assertEquals(Cell.CELL_TYPE_STRING, getTypeOf(sheet, 0, 7));
    Assert.assertEquals(Cell.CELL_TYPE_STRING, getTypeOf(sheet, 0, 8));
    Assert.assertEquals(Cell.CELL_TYPE_STRING, getTypeOf(sheet, 0, 9));
    Assert.assertEquals(Cell.CELL_TYPE_STRING, getTypeOf(sheet, 1, 0));
    Assert.assertEquals(Cell.CELL_TYPE_STRING, getTypeOf(sheet, 1, 1));
    Assert.assertEquals(Cell.CELL_TYPE_NUMERIC, getTypeOf(sheet, 1, 2));
    Assert.assertEquals(Cell.CELL_TYPE_STRING, getTypeOf(sheet, 1, 3));
    Assert.assertEquals(Cell.CELL_TYPE_STRING, getTypeOf(sheet, 1, 4));
    Assert.assertEquals(Cell.CELL_TYPE_STRING, getTypeOf(sheet, 1, 5));
    Assert.assertEquals(Cell.CELL_TYPE_NUMERIC, getTypeOf(sheet, 1, 6), 0.0f);
    Assert.assertEquals(Cell.CELL_TYPE_NUMERIC, getTypeOf(sheet, 1, 7), 0.0f);
    Assert.assertEquals(Cell.CELL_TYPE_NUMERIC, getTypeOf(sheet, 1, 8), 0.0f);
    Assert.assertEquals(Cell.CELL_TYPE_STRING, getTypeOf(sheet, 1, 9));
    Assert.assertEquals(Cell.CELL_TYPE_STRING, getTypeOf(sheet, 2, 0));
    Assert.assertEquals(Cell.CELL_TYPE_STRING, getTypeOf(sheet, 2, 1));
    Assert.assertEquals(Cell.CELL_TYPE_NUMERIC, getTypeOf(sheet, 2, 2));
    Assert.assertEquals(Cell.CELL_TYPE_STRING, getTypeOf(sheet, 2, 3));
    Assert.assertEquals(Cell.CELL_TYPE_STRING, getTypeOf(sheet, 2, 4));
    Assert.assertEquals(Cell.CELL_TYPE_STRING, getTypeOf(sheet, 2, 5));
    Assert.assertEquals(Cell.CELL_TYPE_NUMERIC, getTypeOf(sheet, 2, 6), 0.0f);
    Assert.assertEquals(Cell.CELL_TYPE_NUMERIC, getTypeOf(sheet, 2, 7), 0.0f);
    Assert.assertEquals(Cell.CELL_TYPE_NUMERIC, getTypeOf(sheet, 2, 8), 0.0f);
    Assert.assertEquals(Cell.CELL_TYPE_STRING, getTypeOf(sheet, 2, 9));
  }

  private String getStringContentOf(Sheet sheet, int rowIndex, int columnIndex) {
    return String.valueOf(sheet.getRow(rowIndex).getCell(columnIndex));
  }

  private int getTypeOf(Sheet sheet, int rowIndex, int columnIndex) {
    return sheet.getRow(rowIndex).getCell(columnIndex).getCellType();
  }

  private float getFloatValueOf(Sheet sheet, int rowIndex, int columnIndex) {
    return Float.parseFloat(getStringContentOf(sheet, rowIndex, columnIndex));
  }
}
