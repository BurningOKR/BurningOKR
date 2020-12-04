-- Deletes the first init state, because it will be added by code

DELETE FROM public.init_state WHERE NOT init_state = 0;
