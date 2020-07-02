-- Truncate all tables that should be empty

TRUNCATE
    okr_company,
    okr_member,
    okr_department,
    okr_branch,
    okr_child_unit,
    okr_unit,
    okr_company_history,
    objective,
    note,
    key_result,
    cycle CASCADE;

-- Fill Demo Data

DO $$
DECLARE
    firstCompanyId INTEGER = nextval('hibernate_sequence');
    secondCompanyId INTEGER = nextval('hibernate_sequence');
    cycleId INTEGER = nextval('hibernate_sequence');
    historyId INTEGER = nextval('hibernate_sequence');

    itBranchObjective INTEGER = nextval('hibernate_sequence');

    marketingDepartmentId INTEGER = nextval('hibernate_sequence');
    firstMarketingObjectiveId INTEGER = nextval('hibernate_sequence');
    firstKeyResultId INTEGER = nextval('hibernate_sequence');
    secondKeyResultId INTEGER = nextval('hibernate_sequence');
    thirdKeyResultId INTEGER = nextval('hibernate_sequence');

    itBranchId INTEGER = nextval('hibernate_sequence');

    developmentDepartmentId INTEGER = nextval('hibernate_sequence');
    firstDevelopmentDepartmentObjectiveId INTEGER = nextval('hibernate_sequence');
    secondDevelopmentDepartmentObjectiveId INTEGER = nextval('hibernate_sequence');
    codeGuideLinesKeyResultId INTEGER = nextval('hibernate_sequence');
    gettingStartedKeyResultId INTEGER = nextval('hibernate_sequence');
    forkKeyResultId INTEGER = nextval('hibernate_sequence');
    developKeyResultId INTEGER = nextval('hibernate_sequence');

    integrationDepartmentId INTEGER = nextval('hibernate_sequence');
    integrationDepartmentObjectiveId INTEGER = nextval('hibernate_sequence');
    installGuideKeyResultId INTEGER = nextval('hibernate_sequence');
BEGIN

    -- Insert Cycle
    INSERT INTO cycle (id, cycle_state, factual_end_date, factual_start_date, name, planned_end_date, planned_start_date)
    VALUES (cycleId, --id
            1, -- CycleState.ACTIVE
            current_date + INTEGER '7', -- endDate -> today plus seven days
            current_date - INTEGER '7', -- startDate -> today minus seven days
            'My First Cycle', --name
            current_date + INTEGER '7', -- plannedEndDate -> today plus seven days
            current_date - INTEGER '7' -- plannedStartDate -> today minus seven days
           );

    -- Insert Company History
    INSERT INTO okr_company_history (id)
    VALUES (historyId);

    -- Insert all Base Classes for OKRUnits
    INSERT INTO okr_unit (id, label, name)
    VALUES (firstCompanyId, 'Company', 'My First Company'),
           (secondCompanyId, 'Company', 'My Second Company'),
           (marketingDepartmentId, 'Department', 'Marketing Department'),
           (itBranchId, 'OKR Branch', 'IT Branch'),
           (developmentDepartmentId, 'Department', 'Department for Application Development'),
           (integrationDepartmentId, 'Department', 'Department for Application Integration');

    -- Insert all Base Classes for OKRChildUnits
    INSERT INTO okr_child_unit (id, parent_okr_unit_id, is_active)
    VALUES (marketingDepartmentId, firstCompanyId, true),
           (itBranchId, firstCompanyId, true),
           (developmentDepartmentId, itBranchId, true),
           (integrationDepartmentId, itBranchId, true);

    -- Insert all Companies
    INSERT INTO okr_company (id, cycle_id, history_id)
    VALUES (firstCompanyId, cycleId, historyId),
           (secondCompanyId, cycleId, historyId);

    -- Insert all Departments
    INSERT INTO okr_department (id, okr_master_id, okr_topic_sponsor_id)
    VALUES (marketingDepartmentId, NULL, NULL),
           (developmentDepartmentId, NULL, NULL),
           (integrationDepartmentId, NULL, NULL);

    -- Insert all OKRBranches
    INSERT INTO okr_branch (id)
    VALUES (itBranchId);

    -- Insert all Objectives
    INSERT INTO objective (id, contact_person_id, description, name, remark, review, parent_objective_id, parent_okr_unit_id)
    VALUES (itBranchObjective, NULL, 'Our OKR Process would be easier with an OKR Tool.', 'Switch to Burning OKR.', '', '', NULL, itBranchId),
           (firstMarketingObjectiveId, NULL, 'We need to tell everyone about Burning OKR!', 'Spread the word about Burning OKR!', '', '', itBranchObjective, marketingDepartmentId),
           (firstDevelopmentDepartmentObjectiveId, NULL, '', 'Get Started with the development of Burning OKR.', '', '', NULL, developmentDepartmentId),
           (secondDevelopmentDepartmentObjectiveId, NULL, 'It is open source! Everybody can contribute to the development of Burning OKR.', 'Develop features for Burning OKR!', '', '', NULL, developmentDepartmentId),
           (integrationDepartmentObjectiveId, NULL, '', 'Integrate Burning OKR in your company.', '', '', itBranchObjective, integrationDepartmentId);

    -- Insert all KeyResults
    INSERT INTO key_result (id, current_value, description, name, start_value, target_value, unit, parent_objective_id, sequence)
    VALUES (firstKeyResultId, 0, '', 'Tell three friends about Burning OKR!', 0, 3, 0, firstMarketingObjectiveId, 0),
           (secondKeyResultId, 0, '', 'Follow the Burning OKR Twitter Account.', 0, 1, 0, firstMarketingObjectiveId, 1),
           (thirdKeyResultId, 0, '', 'Learn more about us on www.brockhaus-ag.de', 0, 100, 1, firstMarketingObjectiveId, 2),
           (gettingStartedKeyResultId, 0, 'It is very helpful!', 'Read our Getting Started Guide', 0, 100, 1, firstDevelopmentDepartmentObjectiveId, 0),
           (forkKeyResultId, 0, '', 'Fork our Github Project', 0, 1, 0, firstDevelopmentDepartmentObjectiveId, 1),
           (codeGuideLinesKeyResultId, 0, 'See https://github.com/BurningOKR/BurningOKR/blob/master/CODE_GUIDELINES.md', 'Read our Code Guidelines', 0, 100, 1, firstDevelopmentDepartmentObjectiveId, 2),
           (developKeyResultId, 0, '', 'Develop five features for Burning OKR.', 0, 5, 0, secondDevelopmentDepartmentObjectiveId, 0),
           (installGuideKeyResultId, 0, '', 'Take a look at our Install Guide', 0, 100, 1, NULL, integrationDepartmentObjectiveId);
END $$;
