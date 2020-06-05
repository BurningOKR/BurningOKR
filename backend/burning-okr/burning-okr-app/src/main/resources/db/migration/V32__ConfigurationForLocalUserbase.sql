DELETE FROM public.configuration
	WHERE true;

INSERT INTO public.configuration (id, name, value)
VALUES (nextval('public.hibernate_sequence'), 'max-key-results', '7');

INSERT INTO public.configuration (id, name, value)
VALUES (nextval('public.hibernate_sequence'), 'objective-progress-green-yellow-threshold', '-0.2');

INSERT INTO public.configuration (id, name, value)
VALUES (nextval('public.hibernate_sequence'), 'objective-progress-yellow-red-threshold', '-0.4');

INSERT INTO public.configuration (id, name, value)
VALUES (nextval('public.hibernate_sequence'), 'general_frontend-base-url', 'http://localhost:4200');

INSERT INTO public.configuration (id, name, value)
VALUES (nextval('public.hibernate_sequence'), 'initFlag', '');

INSERT INTO public.configuration (id, name, value)
VALUES (nextval('public.hibernate_sequence'), 'passwords_new-user_email_from', 'no-reply@burning-okr.com');

INSERT INTO public.configuration (id, name, value)
VALUES (nextval('public.hibernate_sequence'), 'passwords_new-user_email_template-name', 'set-password.html');

INSERT INTO public.configuration (id, name, value)
VALUES (nextval('public.hibernate_sequence'), 'passwords_new-user_email_subject', 'Please set your password for BurningOKR');

INSERT INTO public.configuration (id, name, value)
VALUES (nextval('public.hibernate_sequence'), 'passwords_new-user_email_url-suffix', '/auth/setpassword');
