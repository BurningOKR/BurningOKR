INSERT INTO local_user( id, given_name, surname, mail, job_title, department, photo, active, password)
VALUES ('9e4fe13e-a59a-408a-a7c8-f44af757d9e5', 'Max', 'Admin', 'admin@example.com', null, null, null, true, '$2a$10$f7XAen8fU8DYusdV8B3gy.6lVH/huy0aIl41Vd3YJAHynBlXBkY4O') ON CONFLICT(id) DO NOTHING;

INSERT INTO local_user( id, given_name, surname, mail, job_title, department, photo, active, password)
VALUES ('19bb8bfe-83a6-4e8d-894a-678ad9b2a49b', 'Hans', 'Master', 'master@example.com', null, null, null, true, '$2a$10$CVftDNB2wg034RwcI7L33eLlh8oIOb45tTQgyuBU01VUJHuZr998y') ON CONFLICT(id) DO NOTHING;

INSERT INTO local_user( id, given_name, surname, mail, job_title, department, photo, active, password)
VALUES ('6675cbca-a863-4075-9de2-335e4bdec9a3', 'Gabi', 'Pate', 'pate@example.com', null, null, null, true, '$2a$10$HfnqIlmirhCOvfzv.euSsu0VfqL7bKhxQodxyS4f3t8Hyf9n3n0pu') ON CONFLICT(id) DO NOTHING;

INSERT INTO local_user( id, given_name, surname, mail, job_title, department, photo, active, password)
VALUES ('f5ce0818-d446-4eea-b3ee-a35ffcc62ed4', 'Ute', 'User', 'user@example.com', null, null, null, true, '$2a$10$OnTVz7yPoAw0DsGPEmTMfeL5CEHjZAZ5ca4rzPA3xD2laIsHMoZru') ON CONFLICT(id) DO NOTHING;

INSERT INTO admin_user( id)
VALUES ('9e4fe13e-a59a-408a-a7c8-f44af757d9e5') ON CONFLICT (id) DO NOTHING;

INSERT INTO public.oauth_client_details (client_id, resource_ids, client_secret, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove)
VALUES ('clientapp', 'oauth2-resource', '$2a$10$2/HRssJeVt4gKpFdmHAHKety08710k1PKd4sy2sEEaWwsPru9mJla', 'USER', 'password,refresh_token', 'http://localhost:4200/companies', '', 43200, 43200, '{}', '')
ON CONFLICT (client_id) DO NOTHING;

INSERT INTO public.oauth_frontend_details (client_id, dummy_client_secret, scope, redirect_uri, silent_refresh_redirect_uri, auth_type, issuer, response_type, token_endpoint, oidc, show_debug_information, require_https, strict_discovery_document_validation, use_http_basic_auth)
VALUES ('clientapp', '123abc', 'USER', null, null, 'local', '/oauth/token', 'password,refresh_token', '/oauth/token', false, true, false, null, true)
ON CONFLICT (client_id) DO NOTHING;
