import { TestBed } from '@angular/core/testing';

import { CsvUserParseService } from './csv-user-parse.service';
import { User } from '../../../../../shared/model/api/user';

describe('CsvUserParseService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: CsvUserParseService = TestBed.get(CsvUserParseService);
    expect(service)
      .toBeTruthy();
  });

  it('should parse csv with multiple users to user[]', () => {
    const service: CsvUserParseService = TestBed.get(CsvUserParseService);
    const input: string =
      `Nico;Jeske;email@email.email;job bezeichung;department
Tim;Fischer ;tim.fischer@fisch.de;Fischer;FischDepartment`;

    const actual: { warnings: { tooManyFields: boolean }; users: User[] } = service.parseCsvStringToUserArray(input);

    expect(actual.users.length)
      .toEqual(2);
    expect(actual.users[0].givenName)
      .toEqual('Nico');
    expect(actual.users[1].givenName)
      .toEqual('Tim');
    expectNoWarnings(actual.warnings);
  });

  it('should parse csv with multiple users with not all properties set to user[]', () => {
    const service: CsvUserParseService = TestBed.get(CsvUserParseService);
    const input: string =
      `;Jeske;email@email.email;job bezeichung;department
Tim;;tim.fischer@fisch.de;Fischer;FischDepartment`;

    const actual: { warnings: { tooManyFields: boolean }; users: User[] } = service.parseCsvStringToUserArray(input);

    expect(actual.users.length)
      .toEqual(2);
    expect(actual.users[0].givenName)
      .toEqual('');
    expect(actual.users[0].surname)
      .toEqual('Jeske');
    expect(actual.users[1].givenName)
      .toEqual('Tim');
    expect(actual.users[1].surname)
      .toEqual('');
    expectNoWarnings(actual.warnings);
  });

  it('should return empty user[] if csv is empty', () => {
    const service: CsvUserParseService = TestBed.get(CsvUserParseService);
    const input: string = '';

    const actual: { warnings: { tooManyFields: boolean }; users: User[] } = service.parseCsvStringToUserArray(input);

    expect(actual.users.length)
      .toEqual(0);
    expectNoWarnings(actual.warnings);
  });

  it('should have warning if csv is invalid', () => {
    const service: CsvUserParseService = TestBed.get(CsvUserParseService);
    const input: string = 'dasdas,eqwe,das.d,.d.sd,asd.d,qw,dasd,,wd,s,asd,qwd,sdqw,,qwdw';

    const actual: { warnings: { tooManyFields: boolean }; users: User[] } = service.parseCsvStringToUserArray(input);

    expect(actual.warnings.tooManyFields)
      .toEqual(true);
  });

  const expectNoWarnings: (warnings: { tooManyFields: boolean }) => void = (warnings: { tooManyFields: boolean }) => {
    expect(warnings)
      .toEqual(undefined);
  };
});
