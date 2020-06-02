package org.burningokr.service.structureutil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import org.burningokr.model.cycles.Cycle;
import org.burningokr.model.cycles.CycleState;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.structures.Company;
import org.burningokr.model.structures.Department;
import org.burningokr.model.structures.SubStructure;

public class TestingCycleStructure {

  /*
      Structure of the created Cycle Relationship

                           Cycle
                             |
                             V
                          Company
                         /       \
                        /         \
                       V           V
         exampleObjective1        exampleDepartment
                 V                       V
       exampleChildObjective      exampleObjective2
                                         V
                                exampleChildObjective2


      - 1 Cycle
      - 1 Company
      - 4 Objectives (2 are childobjectives)
      - 1 Department
  */

  public long originalId = 100L;

  @SuppressWarnings("WeakerAccess")
  public Cycle exampleCycle;

  public static final String exampleCycleName = "exampleCycle";

  @SuppressWarnings("WeakerAccess")
  public Company exampleCompany;

  public static final String exampleCompanyName = "exampleCompany";
  public Objective exampleObjective1;
  public static final String exampleObjective1Name = "exampleObjective1";
  public Objective exampleChildObjective1;
  public static final String exampleChildObjective1Name = "exampleChildObjective1";
  public Department exampleDepartment;
  public static final String exampleDepartmentName = "exampleDepartment";
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
    exampleCompany = new Company();
    exampleCompany.setId(originalId);
    exampleCompany.setName(exampleCompanyName);
    exampleObjective1 = new Objective();
    exampleObjective1.setId(originalId);
    exampleObjective1.setName(exampleObjective1Name);
    exampleChildObjective1 = new Objective();
    exampleChildObjective1.setId(originalId);
    exampleChildObjective1.setName(exampleChildObjective1Name);
    exampleDepartment = new Department();
    exampleDepartment.setId(originalId);
    exampleDepartment.setName(exampleDepartmentName);
    exampleObjective2 = new Objective();
    exampleObjective2.setId(originalId);
    exampleObjective2.setName(exampleObjective2Name);
    exampleChildObjective2 = new Objective();
    exampleChildObjective2.setId(originalId);
    exampleChildObjective2.setName(exampleChildObjective2Name);
  }

  @SuppressWarnings("WeakerAccess")
  public void createTestingCompanyParentRelations() {
    exampleObjective1.setParentStructure(exampleCompany);
    exampleChildObjective1.setParentStructure(exampleCompany);
    exampleChildObjective1.setParentObjective(exampleObjective1);
    exampleDepartment.setParentStructure(exampleCompany);
    exampleObjective2.setParentStructure(exampleDepartment);
    exampleChildObjective2.setParentStructure(exampleDepartment);
    exampleChildObjective2.setParentObjective(exampleObjective2);
  }

  public void createTestingCompanyChildListRelations() {
    // Setting up lists
    Collection<Objective> objectiveList1 = new ArrayList<>();
    objectiveList1.add(exampleObjective1);
    objectiveList1.add(exampleChildObjective1);
    Collection<SubStructure> departmentList = new ArrayList<>();
    departmentList.add(exampleDepartment);
    Collection<Objective> objectiveList2 = new ArrayList<>();
    objectiveList2.add(exampleObjective2);
    objectiveList2.add(exampleChildObjective2);

    exampleCompany.setObjectives(objectiveList1);
    exampleCompany.setSubStructures(departmentList);
    exampleDepartment.setObjectives(objectiveList2);
  }

  public Company createTestingCompanyRelationship() {
    createTestingCompanyInstances();
    createTestingCompanyChildListRelations();
    createTestingCompanyParentRelations();

    return exampleCompany;
  }

  public Cycle createTestingCycleRelationship() {
    createTestingCompanyRelationship();

    ArrayList<Company> companies = new ArrayList<>();
    companies.add(exampleCompany);

    exampleCycle.setCompanies(companies);
    exampleCompany.setCycle(exampleCycle);

    return exampleCycle;
  }
}
