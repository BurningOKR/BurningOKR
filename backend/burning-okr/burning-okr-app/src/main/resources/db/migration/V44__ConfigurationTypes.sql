ALTER TABLE public.configuration
  ADD COLUMN type character varying(255);

UPDATE public.configuration SET type = 'text' WHERE TRUE;
UPDATE public.configuration SET type = 'number' WHERE name = 'max-key-results';
UPDATE public.configuration SET type = 'number' WHERE name = 'objective-progress-green-yellow-threshold';
UPDATE public.configuration SET type = 'number' WHERE name = 'objective-progress-yellow-red-threshold';
