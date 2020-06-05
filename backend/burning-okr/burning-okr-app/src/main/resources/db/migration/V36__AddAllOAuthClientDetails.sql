ALTER TABLE public.oauth_client_details
   ADD COLUMN silent_refresh_redirect_uri character varying(255);

ALTER TABLE public.oauth_client_details
   ADD COLUMN auth_type character varying(255);

ALTER TABLE public.oauth_client_details
   ADD COLUMN issuer character varying(255);

ALTER TABLE public.oauth_client_details
   ADD COLUMN response_type character varying(255);

ALTER TABLE public.oauth_client_details
   ADD COLUMN token_endpoint character varying(255);

ALTER TABLE public.oauth_client_details
   ADD COLUMN oidc boolean
   DEFAULT 'TRUE';

ALTER TABLE public.oauth_client_details
   ADD COLUMN show_debug_information boolean
   DEFAULT 'TRUE';

ALTER TABLE public.oauth_client_details
   ADD COLUMN require_https boolean
   DEFAULT 'TRUE';

ALTER TABLE public.oauth_client_details
   ADD COLUMN strict_discovery_document_validation boolean
   DEFAULT 'TRUE';

ALTER TABLE public.oauth_client_details
   ADD COLUMN use_http_basic_auth boolean
   DEFAULT 'TRUE';
