import { TestBed } from '@angular/core/testing';

import { SubStructureContextRoleService } from './sub-structure-context-role.service';
import { OAuthService } from 'angular-oauth2-oidc';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { CurrentUserService } from '../../../core/services/current-user.service';

const currentUserService: any = {};

describe('SubStructureContextRoleService', () => {
  beforeEach(() => TestBed.configureTestingModule({
    imports: [],
    providers: [
      { provide: CurrentUserService, useValue: currentUserService }
    ]
  }));

  it('should be created', () => {
    const service: SubStructureContextRoleService = TestBed.get(SubStructureContextRoleService);
    expect(service)
      .toBeTruthy();
  });
});
