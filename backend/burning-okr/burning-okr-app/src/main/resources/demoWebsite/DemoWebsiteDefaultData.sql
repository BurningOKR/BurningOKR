-- Truncate all tables that should be empty

TRUNCATE
    cycle,
    default_task_board_state,
    okr_unit,
    objective,
    key_result,
    okr_topic_description,
    okr_unit_history,
    okr_branch_history,
    okr_child_unit,
    okr_branch,
    okr_company_history,
    okr_company,
    okr_department_history,
    okr_department,
    okr_description_member,
    okr_description_stakeholder,
    okr_member,
    task_board,
    task_state,
    task,
    task_user,
    user_settings CASCADE;

-- Fill Demo Data
DO
$$
    DECLARE
        cycleid INTEGER = NEXTVAL('hibernate_sequence');
        firstcompanyid INTEGER = NEXTVAL('hibernate_sequence');
        secondcompanyid INTEGER = NEXTVAL('hibernate_sequence');
        firstdepartmentid INTEGER = NEXTVAL('hibernate_sequence');
        seconddepartmentid INTEGER = NEXTVAL('hibernate_sequence');
        thirddepartmentid INTEGER = NEXTVAL('hibernate_sequence');
        branchid INTEGER = NEXTVAL('hibernate_sequence');
        firstobjectiveid INTEGER = NEXTVAL('hibernate_sequence');
        secondobjectiveid INTEGER = NEXTVAL('hibernate_sequence');
        thirdobjectiveid INTEGER = NEXTVAL('hibernate_sequence');
        fourthobjectiveid INTEGER = NEXTVAL('hibernate_sequence');
        fifthobjectiveid INTEGER = NEXTVAL('hibernate_sequence');
        firstkeyresultid INTEGER = NEXTVAL('hibernate_sequence');
        secondkeyresultid INTEGER = NEXTVAL('hibernate_sequence');
        thirdkeyresultid INTEGER = NEXTVAL('hibernate_sequence');
        fourthkeyresultid INTEGER = NEXTVAL('hibernate_sequence');
        fifthkeyresultid INTEGER = NEXTVAL('hibernate_sequence');
        sixthkeyresultid INTEGER = NEXTVAL('hibernate_sequence');
        seventhkeyresultid INTEGER = NEXTVAL('hibernate_sequence');
        firsttopicdescriptionid INTEGER = NEXTVAL('hibernate_sequence');
        secondtopicdescriptionid INTEGER = NEXTVAL('hibernate_sequence');
        thirdtopicdescriptionid INTEGER = NEXTVAL('hibernate_sequence');
        firstunithistoryid INTEGER = NEXTVAL('hibernate_sequence');
        secondunithistoryid INTEGER = NEXTVAL('hibernate_sequence');
        thirdunithistoryid INTEGER = NEXTVAL('hibernate_sequence');
        fourthunithistoryid INTEGER = NEXTVAL('hibernate_sequence');
        firsttaskboardid INTEGER = NEXTVAL('hibernate_sequence');
        secondtaskboardid INTEGER = NEXTVAL('hibernate_sequence');
        thirdtaskboardid INTEGER = NEXTVAL('hibernate_sequence');
        firsttaskid INTEGER = NEXTVAL('hibernate_sequence');
        firsttaskstateid INTEGER = NEXTVAL('hibernate_sequence');
        secondtaskstateid INTEGER = NEXTVAL('hibernate_sequence');
        thirdtaskstateid INTEGER = NEXTVAL('hibernate_sequence');
        fourthtaskstateid INTEGER = NEXTVAL('hibernate_sequence');
        fifthtaskstateid INTEGER = NEXTVAL('hibernate_sequence');
        sixthtaskstateid INTEGER = NEXTVAL('hibernate_sequence');
        seventhtaskstateid INTEGER = NEXTVAL('hibernate_sequence');
        eighthtaskstateid INTEGER = NEXTVAL('hibernate_sequence');
        ninthtaskstateid INTEGER = NEXTVAL('hibernate_sequence');
        tenthtaskstateid INTEGER = NEXTVAL('hibernate_sequence');
        eleventhtaskstateid INTEGER = NEXTVAL('hibernate_sequence');
        twelfthtaskstateid INTEGER = NEXTVAL('hibernate_sequence');
    BEGIN

        -- Insert Data for Table: cycle
        INSERT INTO cycle (id, cycle_state, factual_end_date, factual_start_date, name, planned_end_date,
                           planned_start_date)
        VALUES (cycleid, --id
                1, -- CycleState.ACTIVE
                CURRENT_DATE + INTEGER '7', -- endDate -> today plus seven days
                CURRENT_DATE - INTEGER '7', -- startDate -> today minus seven days
                'Mein erster OKR Zyklus', --name
                CURRENT_DATE + INTEGER '7', -- plannedEndDate -> today plus seven days
                CURRENT_DATE - INTEGER '7' -- plannedStartDate -> today minus seven days
               );

        -- Insert Data for Table: default_task_board_state
        INSERT INTO public.default_task_board_state (id, title) VALUES (1, 'ToDo');
        INSERT INTO public.default_task_board_state (id, title) VALUES (2, 'In Progress');
        INSERT INTO public.default_task_board_state (id, title) VALUES (3, 'Blocked');
        INSERT INTO public.default_task_board_state (id, title) VALUES (4, 'Finished');

        -- Insert Data for Table: okr_unit
        INSERT INTO public.okr_unit (id, label, name) VALUES (firstcompanyid, 'Firma', 'Meine erste Firma');
        INSERT INTO public.okr_unit (id, label, name) VALUES (secondcompanyid, 'Company', 'Meine zweite Firma');
        INSERT INTO public.okr_unit (id, label, name) VALUES (firstdepartmentid, 'Abteilung', 'Marketing Abteilung');
        INSERT INTO public.okr_unit (id, label, name) VALUES (branchid, 'OKR Zweig', 'IT Zweig');
        INSERT INTO public.okr_unit (id, label, name) VALUES (seconddepartmentid, 'Abteilung', 'Entwicklungsabteilung');
        INSERT INTO public.okr_unit (id, label, name)
        VALUES (thirddepartmentid, 'Abteilung', 'Abteilung für Anwendungsintegration');

        -- Insert Data for Table: objective
        INSERT INTO public.objective (id, contact_person_id, description, name, remark, review, parent_objective_id,
                                      parent_okr_unit_id, is_active, sequence)
        VALUES (firstobjectiveid, NULL, 'Wir müssen allen Leuten von Burning OKR erzählen!',
                'Burning OKR bekannt machen', '', NULL, NULL, firstdepartmentid, TRUE, 0);
        INSERT INTO public.objective (id, contact_person_id, description, name, remark, review, parent_objective_id,
                                      parent_okr_unit_id, is_active, sequence)
        VALUES (secondobjectiveid, NULL, 'Unser OKR Prozess wäre einfacher mit einem OKR Tool.',
                'Zu Burning OKR wechseln.', '', NULL, NULL, branchid, TRUE, 0);
        INSERT INTO public.objective (id, contact_person_id, description, name, remark, review, parent_objective_id,
                                      parent_okr_unit_id, is_active, sequence)
        VALUES (thirdobjectiveid, NULL, 'Burning OKR ist OpenSource! Jeder kann daran mitentwickeln.',
                'Weitere Features für Burning OKR entwickeln.', '', NULL, NULL, seconddepartmentid, TRUE, 1);
        INSERT INTO public.objective (id, contact_person_id, description, name, remark, review, parent_objective_id,
                                      parent_okr_unit_id, is_active, sequence)
        VALUES (fourthobjectiveid, NULL, '', 'Mit der Entwicklung von Burning OKR beginnen.', '', NULL, NULL,
                seconddepartmentid, TRUE, 0);
        INSERT INTO public.objective (id, contact_person_id, description, name, remark, review, parent_objective_id,
                                      parent_okr_unit_id, is_active, sequence)
        VALUES (fifthobjectiveid, NULL, '', 'Burning OKR in den OKR Prozess unserer Firma einbinden.', '', NULL,
                secondobjectiveid, thirddepartmentid, TRUE, 0);

        --Insert Data for Table: key_result
        INSERT INTO public.key_result (id, current_value, description, name, start_value, target_value, unit,
                                       parent_objective_id, sequence)
        VALUES (firstkeyresultid, 0, 'https://github.com/BurningOKR/BurningOKR/blob/master/CODE_GUIDELINES.md',
                'Lies unsere Code Guidelines.', 0, 100, 1, fourthobjectiveid, 2);
        INSERT INTO public.key_result (id, current_value, description, name, start_value, target_value, unit,
                                       parent_objective_id, sequence)
        VALUES (secondkeyresultid, 0, '', 'Erstelle einen Fork von unserem Github Project.', 0, 1, 0, fourthobjectiveid,
                1);
        INSERT INTO public.key_result (id, current_value, description, name, start_value, target_value, unit,
                                       parent_objective_id, sequence)
        VALUES (thirdkeyresultid, 0, 'Er ist sehr Hilfreich!', 'Lies unseren Getting Started Guide.', 0, 100, 1,
                fourthobjectiveid, 0);
        INSERT INTO public.key_result (id, current_value, description, name, start_value, target_value, unit,
                                       parent_objective_id, sequence)
        VALUES (fourthkeyresultid, 0, '', 'Erzähle drei Freunden von Burning OKR!', 0, 3, 0, firstobjectiveid, 0);
        INSERT INTO public.key_result (id, current_value, description, name, start_value, target_value, unit,
                                       parent_objective_id, sequence)
        VALUES (fifthkeyresultid, 0, '', 'Lerne mehr über uns auf www.brockhaus-ag.de', 0, 100, 1, firstobjectiveid, 2);
        INSERT INTO public.key_result (id, current_value, description, name, start_value, target_value, unit,
                                       parent_objective_id, sequence)
        VALUES (sixthkeyresultid, 0, '', 'Folge dem Burning OKR Twitter Account.', 0, 1, 0, firstobjectiveid, 1);
        INSERT INTO public.key_result (id, current_value, description, name, start_value, target_value, unit,
                                       parent_objective_id, sequence)
        VALUES (seventhkeyresultid, 0, '', 'Entwickle fünf neue Features für Burning OKR.', 0, 5, 0, thirdobjectiveid,
                0);

        -- Insert Data for Table: okr_topic_description
        INSERT INTO public.okr_topic_description (id, name, initiator_id, description, contributes_to, delimitation,
                                                  dependencies, resources, handover_plan, beginning)
        VALUES (firsttopicdescriptionid, 'Entwicklungsabteilung', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
        INSERT INTO public.okr_topic_description (id, name, initiator_id, description, contributes_to, delimitation,
                                                  dependencies, resources, handover_plan, beginning)
        VALUES (secondtopicdescriptionid, 'Abteilung für Anwendungsintegration', NULL, NULL, NULL, NULL, NULL, NULL,
                NULL, NULL);
        INSERT INTO public.okr_topic_description (id, name, initiator_id, description, contributes_to, delimitation,
                                                  dependencies, resources, handover_plan, beginning)
        VALUES (thirdtopicdescriptionid, 'Reichweite', '2b055eeb-8fa6-4043-b80f-e2aec3f54165',
                'Ziel von dem ganzen Aufwand ist eine hohe Reichweite', NULL,
                'Keine Abgrenzung nötig, da keine weiteren Teams ähnliche Themen bearbeiten', NULL, NULL, NULL, NULL);

        -- Insert Data for Table: okr_unit_history
        INSERT INTO public.okr_unit_history (id) VALUES (firstunithistoryid);
        INSERT INTO public.okr_unit_history (id) VALUES (secondunithistoryid);
        INSERT INTO public.okr_unit_history (id) VALUES (thirdunithistoryid);
        INSERT INTO public.okr_unit_history (id) VALUES (fourthunithistoryid);

        -- Insert Data for Table: okr_branch_history; Type: TABLE DATA; Schema: public; Owner: okr-user
        INSERT INTO public.okr_branch_history (id) VALUES (fourthunithistoryid);

        -- Insert Data for Table: okr_child_unit; Type: TABLE DATA; Schema: public; Owner: okr-user
        INSERT INTO public.okr_child_unit (id, parent_okr_unit_id, is_active)
        VALUES (firstdepartmentid, firstcompanyid, TRUE);
        INSERT INTO public.okr_child_unit (id, parent_okr_unit_id, is_active) VALUES (branchid, firstcompanyid, TRUE);
        INSERT INTO public.okr_child_unit (id, parent_okr_unit_id, is_active)
        VALUES (seconddepartmentid, branchid, TRUE);
        INSERT INTO public.okr_child_unit (id, parent_okr_unit_id, is_active)
        VALUES (thirddepartmentid, branchid, TRUE);

        -- Insert Data for Table: okr_branch
        INSERT INTO public.okr_branch (id, history_id) VALUES (branchid, fourthunithistoryid);

        -- Insert Data for Table: okr_company_history
        INSERT INTO public.okr_company_history (id) VALUES (firstunithistoryid);
        INSERT INTO public.okr_company_history (id) VALUES (secondunithistoryid);

        -- Insert Data for Table: okr_company
        INSERT INTO public.okr_company (id, cycle_id, history_id) VALUES (firstcompanyid, cycleid, firstunithistoryid);
        INSERT INTO public.okr_company (id, cycle_id, history_id)
        VALUES (secondcompanyid, cycleid, secondunithistoryid);

        -- Insert Data for Table: okr_department_history
        INSERT INTO public.okr_department_history (id) VALUES (thirdunithistoryid);

        -- Insert Data for Table: okr_department
        INSERT INTO public.okr_department (id, okr_master_id, okr_topic_sponsor_id, okr_topic_description_id,
                                           history_id)
        VALUES (firstdepartmentid, '2b055eeb-8fa6-4043-b80f-e2aec3f54165', 'ec63a79c-7a0f-42f4-90f7-a65b5bfe00f7',
                thirdtopicdescriptionid, thirdunithistoryid);
        INSERT INTO public.okr_department (id, okr_master_id, okr_topic_sponsor_id, okr_topic_description_id,
                                           history_id)
        VALUES (seconddepartmentid, '3736177c-e8bf-49f0-838f-55a96ddcfa48', '587b3863-1ebd-4436-b812-a6671a065e6d',
                firsttopicdescriptionid, NULL);
        INSERT INTO public.okr_department (id, okr_master_id, okr_topic_sponsor_id, okr_topic_description_id,
                                           history_id)
        VALUES (thirddepartmentid, '25669c9b-b55d-44e5-987e-9278e72f924c', '8d01ffe5-a291-4a00-a8cd-d557cff10b77',
                secondtopicdescriptionid, NULL);

        -- Insert Data for Table: okr_description_member
        INSERT INTO public.okr_description_member (okr_topic_description_id, okr_member_id)
        VALUES (thirdtopicdescriptionid, '45b6e775-02d1-402b-a4d9-d52be510680e');
        INSERT INTO public.okr_description_member (okr_topic_description_id, okr_member_id)
        VALUES (thirdtopicdescriptionid, 'bfab3ef9-e1f4-44d0-8c33-dd7dbae7db1c');
        INSERT INTO public.okr_description_member (okr_topic_description_id, okr_member_id)
        VALUES (thirdtopicdescriptionid, '9628bd31-1438-4e56-93c4-4862252ebac1');

        -- Insert Data for Table: okr_description_stakeholder
        INSERT INTO public.okr_description_stakeholder (okr_topic_description_id, okr_stakeholder_id)
        VALUES (thirdtopicdescriptionid, 'ec63a79c-7a0f-42f4-90f7-a65b5bfe00f7');

        -- Insert Data for Table: okr_member
        INSERT INTO public.okr_member (okr_department_id, okr_member_id)
        VALUES (firstdepartmentid, '45b6e775-02d1-402b-a4d9-d52be510680e');
        INSERT INTO public.okr_member (okr_department_id, okr_member_id)
        VALUES (firstdepartmentid, 'bfab3ef9-e1f4-44d0-8c33-dd7dbae7db1c');
        INSERT INTO public.okr_member (okr_department_id, okr_member_id)
        VALUES (firstdepartmentid, '9628bd31-1438-4e56-93c4-4862252ebac1');
        INSERT INTO public.okr_member (okr_department_id, okr_member_id)
        VALUES (seconddepartmentid, '18f951c5-c170-4f77-a0c9-0779682e0b68');
        INSERT INTO public.okr_member (okr_department_id, okr_member_id)
        VALUES (seconddepartmentid, 'd70dfd70-6e7c-4559-a241-d106adb5dcbc');
        INSERT INTO public.okr_member (okr_department_id, okr_member_id)
        VALUES (seconddepartmentid, 'd6a32065-a3c0-4821-b06d-a369bd389f6c');
        INSERT INTO public.okr_member (okr_department_id, okr_member_id)
        VALUES (seconddepartmentid, '2e50acc6-d9eb-4613-bc62-1beae2a3899c');
        INSERT INTO public.okr_member (okr_department_id, okr_member_id)
        VALUES (seconddepartmentid, '09dfeff0-b3ad-488c-a52a-7d30825e0f47');
        INSERT INTO public.okr_member (okr_department_id, okr_member_id)
        VALUES (thirddepartmentid, '7aa52ae5-8dfb-466b-a208-3bcad8ab91d2');
        INSERT INTO public.okr_member (okr_department_id, okr_member_id)
        VALUES (thirddepartmentid, '0a3281e7-d300-4354-98ef-43f02a028706');

        -- Insert Data for Table: task_board
        INSERT INTO public.task_board (id, parent_unit_id) VALUES (firsttaskboardid, firstdepartmentid);
        INSERT INTO public.task_board (id, parent_unit_id) VALUES (secondtaskboardid, seconddepartmentid);
        INSERT INTO public.task_board (id, parent_unit_id) VALUES (thirdtaskboardid, thirddepartmentid);

        -- Insert Data for Table: task_state
        INSERT INTO public.task_state (id, title, parent_task_board_id)
        VALUES (firsttaskstateid, 'ToDo', firsttaskboardid);
        INSERT INTO public.task_state (id, title, parent_task_board_id)
        VALUES (secondtaskstateid, 'In Progress', firsttaskboardid);
        INSERT INTO public.task_state (id, title, parent_task_board_id)
        VALUES (thirdtaskstateid, 'Blocked', firsttaskboardid);
        INSERT INTO public.task_state (id, title, parent_task_board_id)
        VALUES (fourthtaskstateid, 'Finished', firsttaskboardid);
        INSERT INTO public.task_state (id, title, parent_task_board_id)
        VALUES (fifthtaskstateid, 'ToDo', secondtaskboardid);
        INSERT INTO public.task_state (id, title, parent_task_board_id)
        VALUES (sixthtaskstateid, 'In Progress', secondtaskboardid);
        INSERT INTO public.task_state (id, title, parent_task_board_id)
        VALUES (seventhtaskstateid, 'Blocked', secondtaskboardid);
        INSERT INTO public.task_state (id, title, parent_task_board_id)
        VALUES (eighthtaskstateid, 'Finished', secondtaskboardid);
        INSERT INTO public.task_state (id, title, parent_task_board_id)
        VALUES (ninthtaskstateid, 'ToDo', thirdtaskboardid);
        INSERT INTO public.task_state (id, title, parent_task_board_id)
        VALUES (tenthtaskstateid, 'In Progress', thirdtaskboardid);
        INSERT INTO public.task_state (id, title, parent_task_board_id)
        VALUES (eleventhtaskstateid, 'Blocked', thirdtaskboardid);
        INSERT INTO public.task_state (id, title, parent_task_board_id)
        VALUES (twelfthtaskstateid, 'Finished', thirdtaskboardid);

        -- Insert Data for Table: task; Type: TABLE DATA
        INSERT INTO public.task (id, title, description, version, task_state_id, assigned_key_result_id,
                                 parent_task_board_id, previous_task_id)
        VALUES (firsttaskid, 'Twitter-Account verlinken', '', 0, secondtaskstateid, sixthkeyresultid, firsttaskboardid,
                NULL);

        -- Insert Data for Table: task_user
        INSERT INTO public.task_user (task_id, user_id) VALUES (firsttaskid, '45b6e775-02d1-402b-a4d9-d52be510680e');

    END
$$
