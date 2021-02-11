INSERT INTO public.configuration (id, name, value)
VALUES (nextval('public.hibernate_sequence'), 'email_subject_feedback', 'OKR Tool - Feedback');

INSERT INTO public.configuration (id, name, value)
VALUES (nextval('public.hibernate_sequence'), 'feedback_receivers', 'feedback@burning-okr.com');
