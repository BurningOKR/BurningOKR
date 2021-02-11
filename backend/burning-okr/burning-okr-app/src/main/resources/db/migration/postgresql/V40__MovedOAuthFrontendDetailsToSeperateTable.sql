alter table oauth_client_details drop column silent_refresh_redirect_uri;

alter table oauth_client_details drop column auth_type;

alter table oauth_client_details drop column issuer;

alter table oauth_client_details drop column response_type;

alter table oauth_client_details drop column token_endpoint;

alter table oauth_client_details drop column oidc;

alter table oauth_client_details drop column show_debug_information;

alter table oauth_client_details drop column require_https;

alter table oauth_client_details drop column strict_discovery_document_validation;

alter table oauth_client_details drop column use_http_basic_auth;

alter table oauth_client_details drop column dummy_client_secret;

create table oauth_frontend_details
(
	client_id character varying(255)
		constraint oauth_frontend_details_pk
			primary key,
	dummy_client_secret character varying(255),
	scope character varying(255),
	redirect_uri character varying(255),
	silent_refresh_redirect_uri character varying(255),
	auth_type character varying(255),
	issuer character varying(255),
	response_type character varying(255),
	token_endpoint character varying(255),
	oidc boolean,
	show_debug_information boolean,
	require_https boolean,
	strict_discovery_document_validation boolean,
	use_http_basic_auth boolean
);
