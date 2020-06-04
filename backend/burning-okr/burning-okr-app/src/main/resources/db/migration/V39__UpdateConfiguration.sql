UPDATE configuration
    SET name='email_from'
    WHERE name='passwords_new-user_email_from';

UPDATE configuration
    SET name='email_subject_new-user'
    WHERE name='passwords_new-user_email_subject';

DELETE FROM configuration
    WHERE name='passwords_new-user_email_template-name';

DELETE FROM configuration
    WHERE name='passwords_new-user_email_url-suffix';

INSERT INTO configuration(id, name, value)
    VALUES (nextval('hibernate_sequence'), 'email_subject_forgot-password', 'Reset your Password for BurningOKR');