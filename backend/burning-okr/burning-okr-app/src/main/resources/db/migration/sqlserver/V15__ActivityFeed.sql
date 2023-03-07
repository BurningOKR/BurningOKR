create table revision_information (
                                      id BIGINT not null,
                                      date datetime,
                                      user_id uniqueidentifier,
                                      primary key (id)
);

create table key_result_aud (
                                id BIGINT not null,
                                rev BIGINT not null,
                                revtype SMALLINT,
                                name varchar(255),
                                primary key (id, rev)
);

create table task_aud (
                          id BIGINT not null,
                          rev BIGINT not null,
                          revtype SMALLINT,
                          description varchar(1023),
                          title varchar(255),
                          assigned_key_result_id BIGINT,
                          parent_task_board_id BIGINT,
                          task_state_id BIGINT,
                          primary key (id, rev)
);

create table task_state_aud (
                                id BIGINT not null,
                                rev BIGINT not null,
                                revtype SMALLINT,
                                title varchar(255),
                                primary key (id, rev)
);

create table task_user_aud (
                               rev BIGINT not null,
                               task_id BIGINT not null,
                               user_id uniqueidentifier not null,
                               revtype SMALLINT,
                               primary key (rev, task_id, user_id)
);


alter table key_result_aud
    add constraint FKiossvobij2qlcj7qxd2gyfsvg
        foreign key (rev)
            references revision_information;

alter table task_aud
    add constraint FKp13dy0lnxkr23drxbq9i5v59m
        foreign key (rev)
            references revision_information;

alter table task_state_aud
    add constraint FK5ogn2055ew0w5gn0qxs00per6
        foreign key (rev)
            references revision_information;

alter table task_user_aud
    add constraint FK59iw54mr7nq4bboyvvyjutokp
        foreign key (rev)
            references revision_information;


BEGIN;

-- New revision for initial auditions
-- INSERT INTO revision_information (id, timestamp) VALUES (nextval('hibernate_sequence'), extract(epoch from now()) * 1000);
INSERT INTO revision_information (id, date) SELECT (NEXT VALUE FOR hibernate_sequence), CAST(GETDATE() AS Date);

-- Initial audited task states
INSERT INTO task_state_aud (id, rev, revtype, title)
SELECT id, (SELECT CAST(current_value AS bigint) FROM sys.sequences WHERE name = 'hibernate_sequence'), 0, title
FROM task_state;

-- Initial audited key results
INSERT INTO key_result_aud (id, rev, revtype, name)
SELECT id, (SELECT CAST(current_value AS bigint) FROM sys.sequences WHERE name = 'hibernate_sequence'), 0, name
FROM key_result;

-- Initial audited tasks
INSERT INTO task_aud
(id, rev,                           revtype, description, title, assigned_key_result_id, parent_task_board_id, task_state_id)
SELECT id, (SELECT CAST(current_value AS bigint) FROM sys.sequences WHERE name = 'hibernate_sequence'), 0,       description, title, assigned_key_result_id, parent_task_board_id, task_state_id
FROM task;

-- Initial audited task users
INSERT INTO task_user_aud (task_id, rev, revtype, user_id)
SELECT task_id, (SELECT CAST(current_value AS bigint) FROM sys.sequences WHERE name = 'hibernate_sequence'), 0, user_id
FROM task_user;

END;
