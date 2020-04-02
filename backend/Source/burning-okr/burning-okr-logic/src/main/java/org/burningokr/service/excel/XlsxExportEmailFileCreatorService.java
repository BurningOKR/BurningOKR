package org.burningokr.service.excel;

import java.util.Arrays;
import java.util.Collection;
import org.apache.poi.ss.usermodel.Workbook;
import org.burningokr.model.excel.TeamMemberRow;
import org.burningokr.service.messages.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class XlsxExportEmailFileCreatorService {

  private final Collection<String> headlines;
  private final TeamMemberRowBuilderService teamMemberRowBuilderService;
  private final GenericXlsxFileCreatorService<TeamMemberRow> genericXlsxFileCreatorService;
  private final Messages messages;
  private final Logger logger = LoggerFactory.getLogger(XlsxExportEmailFileCreatorService.class);

  /**
   * Initializes XlsxExportEmailFileCreatorService.
   *
   * @param teamMemberRowBuilderService a {@link TeamMemberRowBuilderService} object
   * @param genericXlsxFileCreatorService a {@link GenericXlsxFileCreatorService} object
   * @param messages a {@link Messages} object
   */
  public XlsxExportEmailFileCreatorService(
      TeamMemberRowBuilderService teamMemberRowBuilderService,
      GenericXlsxFileCreatorService<TeamMemberRow> genericXlsxFileCreatorService,
      Messages messages) {
    this.teamMemberRowBuilderService = teamMemberRowBuilderService;
    this.genericXlsxFileCreatorService = genericXlsxFileCreatorService;
    this.messages = messages;
    this.headlines =
        Arrays.asList(
            messages.get("team"),
            messages.get("role"),
            messages.get("name"),
            messages.get("emailAddress"));
  }

  /**
   * Creates a File for OKR-Teams.
   *
   * @param departmentId a long value
   * @return a {@link Workbook} object
   * @throws IllegalAccessException if an error occurs while creating the Workbook
   */
  public Workbook createFileForOkrTeam(long departmentId) throws IllegalAccessException {
    Collection<TeamMemberRow> teamMemberRows =
        teamMemberRowBuilderService.generateForDepartment(departmentId);

    Workbook workbook =
        genericXlsxFileCreatorService.createWorkbook(teamMemberRows, headlines, "okr-members");

    logger.info(
        "Created excel file with member information about okr team with ID: " + departmentId);

    return workbook;
  }

  /**
   * Creates a File for a Company.
   *
   * @param companyId a long value
   * @return a {@link Workbook} object
   * @throws IllegalAccessException if an error occurs while creating the Workbook
   */
  public Workbook createFileForCompany(long companyId) throws IllegalAccessException {
    Collection<TeamMemberRow> teamMemberRows =
        teamMemberRowBuilderService.generateForCompany(companyId);

    Workbook workbook =
        genericXlsxFileCreatorService.createWorkbook(teamMemberRows, headlines, "okr-members");

    logger.info("Created excel file with member information about company with ID: " + companyId);

    return workbook;
  }
}
