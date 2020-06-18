package org.burningokr.service.excel;

import java.util.Arrays;
import java.util.Collection;
import org.apache.poi.ss.usermodel.Workbook;
import org.burningokr.model.excel.ObjectiveRow;
import org.burningokr.service.messages.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class XlsxDataExportFileCreatorService {

  private final Collection<String> headlines;
  private final ObjectiveRowBuilderService objectiveRowBuilderService;
  private final GenericXlsxFileCreatorService<ObjectiveRow> genericXlsxFileCreatorService;
  private final Messages messages;
  private final Logger logger = LoggerFactory.getLogger(XlsxDataExportFileCreatorService.class);

  /**
   * Initializes XlsxExportEmailFileCreatorService.
   *
   * @param objectiveRowBuilderService a {@link ObjectiveRowBuilderService} object
   * @param genericXlsxFileCreatorService a {@link GenericXlsxFileCreatorService} object
   * @param messages a {@link Messages} object
   */
  @Autowired
  public XlsxDataExportFileCreatorService(
      ObjectiveRowBuilderService objectiveRowBuilderService,
      GenericXlsxFileCreatorService<ObjectiveRow> genericXlsxFileCreatorService,
      Messages messages) {
    this.objectiveRowBuilderService = objectiveRowBuilderService;
    this.genericXlsxFileCreatorService = genericXlsxFileCreatorService;
    this.messages = messages;
    this.headlines =
        Arrays.asList(
            messages.get("team"),
            messages.get("objective"),
            messages.get("progress"),
            messages.get("branch"),
            messages.get("keyResult"),
            messages.get("description"),
            messages.get("start"),
            messages.get("end"),
            messages.get("actual"),
            messages.get("unit"));
  }

  /**
   * Creates a File for OKR-Teams.
   *
   * @param unitId a long value
   * @return a {@link Workbook} object
   * @throws IllegalAccessException if an error occurs while creating the Workbook
   */
  public Workbook createFileForOkrTeam(long unitId) throws IllegalAccessException {
    Collection<ObjectiveRow> objectiveRows =
        objectiveRowBuilderService.generateForOkrChildUnit(unitId);

    Workbook workbook =
        genericXlsxFileCreatorService.createWorkbook(objectiveRows, headlines, "okr");

    logger.info("Created excel file for OKR team with ID: " + unitId);

    return workbook;
  }

  /**
   * Creates a File for a OkrCompany.
   *
   * @param companyId a long value
   * @return a {@link Workbook} object
   * @throws IllegalAccessException if an error occurs while creating the Workbook
   */
  public Workbook createFileForCompany(long companyId) throws IllegalAccessException {
    Collection<ObjectiveRow> objectiveRows =
        objectiveRowBuilderService.generateForCompany(companyId);

    Workbook workbook =
        genericXlsxFileCreatorService.createWorkbook(objectiveRows, headlines, "okr");

    logger.info("Created excel file for company with ID: " + companyId);

    return workbook;
  }
}
