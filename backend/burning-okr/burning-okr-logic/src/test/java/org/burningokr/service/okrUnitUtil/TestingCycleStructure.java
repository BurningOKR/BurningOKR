package org.burningokr.service.okrUnitUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import org.burningokr.model.cycles.Cycle;
import org.burningokr.model.cycles.CycleState;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.okrUnits.OkrChildUnit;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.model.okrUnits.OkrDepartment;

public class TestingCycleStructure {

  /*
      okrUnit of the created Cycle Relationship

                           Cycle
                             |
                             V
                          OkrCompany
                         /       \
                        /         \
                       V           V
         exampleObjective1        exampleOkrDepartment
                 V                       V
       exampleChildObjective      exampleObjective2
                                         V
                                exampleChildObjective2


      - 1 Cycle
      - 1 OkrCompany
      - 4 Objectives (2 are childobjectives)
      - 1 OkrDepartment
  */

  public long originalId = 100L;

  @SuppressWarnings("WeakerAccess")
  public Cycle exampleCycle;

  public static final String exampleCycleName = "exampleCycle";

  @SuppressWarnings("WeakerAccess")
  public OkrCompany exampleOkrCompany;

  public static final String exampleCompanyName = "exampleOkrCompany";
  public Objective exampleObjective1;
  public static final String exampleObjective1Name = "exampleObjective1";
  public Objective exampleChildObjective1;
  public static final String exampleChildObjective1Name = "exampleChildObjective1";
  public OkrDepartment exampleOkrDepartment;
  public static final String exampleDepartmentName = "exampleOkrDepartment";
  public Objective exampleObjective2;
  public static final String exampleObjective2Name = "exampleObjective2";
  public Objective exampleChildObjective2;
  public static final String exampleChildObjective2Name = "exampleChildObjective2";

  public void createTestingCompanyInstances() {
    // Creating fresh instances of all objects
    exampleCycle = new Cycle();
    exampleCycle.setId(originalId);
    exampleCycle.setName(exampleCycleName);
    exampleCycle.setCycleState(CycleState.ACTIVE);
    exampleCycle.setPlannedStartDate(LocalDate.now().plusMonths(1));
    exampleCycle.setPlannedEndDate(LocalDate.now().plusMonths(2));
    exampleCycle.setFactualStartDate(LocalDate.now().plusMonths(3));
    exampleCycle.setFactualEndDate(LocalDate.now().plusMonths(4));
    exampleOkrCompany = new OkrCompany();
    exampleOkrCompany.setId(originalId);
    exampleOkrCompany.setName(exampleCompanyName);
    exampleObjective1 = new Objective();
    exampleObjective1.setId(originalId);
    exampleObjective1.setName(exampleObjective1Name);
    exampleChildObjective1 = new Objective();
    exampleChildObjective1.setId(originalId);
    exampleChildObjective1.setName(exampleChildObjective1Name);
    exampleOkrDepartment = new OkrDepartment();
    exampleOkrDepartment.setId(originalId);
    exampleOkrDepartment.setName(exampleDepartmentName);
    exampleObjective2 = new Objective();
    exampleObjective2.setId(originalId);
    exampleObjective2.setName(exampleObjective2Name);
    exampleChildObjective2 = new Objective();
    exampleChildObjective2.setId(originalId);
    exampleChildObjective2.setName(exampleChildObjective2Name);
  }

  @SuppressWarnings("WeakerAccess")
  public void createTestingCompanyParentRelations() {
    exampleObjective1.setParentOkrUnit(exampleOkrCompany);
    exampleChildObjective1.setParentOkrUnit(exampleOkrCompany);
    exampleChildObjective1.setParentObjective(exampleObjective1);
    exampleOkrDepartment.setParentOkrUnit(exampleOkrCompany);
    exampleObjective2.setParentOkrUnit(exampleOkrDepartment);
    exampleChildObjective2.setParentOkrUnit(exampleOkrDepartment);
    exampleChildObjective2.setParentObjective(exampleObjective2);
  }

  public void createTestingCompanyChildListRelations() {
    // Setting up lists
    Collection<Objective> objectiveList1 = new ArrayList<>();
    objectiveList1.add(exampleObjective1);
    objectiveList1.add(exampleChildObjective1);
    Collection<OkrChildUnit> departmentList = new ArrayList<>();
    departmentList.add(exampleOkrDepartment);
    Collection<Objective> objectiveList2 = new ArrayList<>();
    objectiveList2.add(exampleObjective2);
    objectiveList2.add(exampleChildObjective2);

    exampleOkrCompany.setObjectives(objectiveList1);
    exampleOkrCompany.setOkrChildUnits(departmentList);
    exampleOkrDepartment.setObjectives(objectiveList2);
  }

  public OkrCompany createTestingCompanyRelationship() {
    createTestingCompanyInstances();
    createTestingCompanyChildListRelations();
    createTestingCompanyParentRelations();

    return exampleOkrCompany;
  }

  public Cycle createTestingCycleRelationship() {
    createTestingCompanyRelationship();

    ArrayList<OkrCompany> companies = new ArrayList<>();
    companies.add(exampleOkrCompany);

    exampleCycle.setCompanies(companies);
    exampleOkrCompany.setCycle(exampleCycle);

    return exampleCycle;
  }
}
