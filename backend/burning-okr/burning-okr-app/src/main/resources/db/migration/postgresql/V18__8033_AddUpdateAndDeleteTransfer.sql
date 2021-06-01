alter table user_settings drop constraint default_company_fkey;
alter table department drop constraint fkbqdg50hket4gx9j9xixvew31o;
alter table user_settings drop constraint default_team_fkey;
alter table department drop constraint fktqv08tc4aufiv4bu0kwcj185q;


alter table user_settings
	add constraint default_company_fkey
		foreign key (default_company_id) references company
			on update cascade on delete cascade;

alter table user_settings
	add constraint default_team_fkey
		foreign key (default_team_id) references department
			on update cascade on delete cascade;


alter table department
	add constraint fkbqdg50hket4gx9j9xixvew31o
		foreign key (parent_structure_id) references company_structure
			on update cascade on delete cascade;

alter table department
	add constraint fktqv08tc4aufiv4bu0kwcj185q
		foreign key (id) references company_structure
			on update cascade on delete cascade;
