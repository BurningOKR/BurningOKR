import { UserAvatarComponent } from './user-avatar.component';
import { User } from '../../model/api/user';

describe('UserAvatarComponent', () => {
  let userAvatarComponent: UserAvatarComponent;

  beforeAll(() => {
    userAvatarComponent = new UserAvatarComponent();
  });

  it('should create an instance', () => {
    expect(userAvatarComponent).toBeTruthy();
  });

  it('should return initials separated by space for single firstname and single lastname', () => {
    const userWithSingleFirstnameAndLastname: User = {
      id: '',
      givenName: 'Bob',
      surname: 'Ross',
      email: '',
      jobTitle: '',
      department: '',
      photo: '',
      active: true,
    };

    expect(userAvatarComponent.buildInitialsForUserAvatar(userWithSingleFirstnameAndLastname)).toBe('B R');
  });

  it('should return initials separated by space for multiple firstnames and/or multiple lastnames', () => {
    const userWithMultiFirstnameAndSingleLastname: User = {
      id: '',
      givenName: 'Dwayne The Rock',
      surname: 'Johnson',
      email: '',
      jobTitle: '',
      department: '',
      photo: '',
      active: true,
    };

    const userWithSingleFirstnameAndMultiLastname: User = {
      id: '',
      givenName: 'Lil',
      surname: 'Nas X',
      email: '',
      jobTitle: '',
      department: '',
      photo: '',
      active: true,
    };

    const userWithMultiFirstnameAndLastname: User = {
      id: '',
      givenName: 'Cristiano Ronaldo',
      surname: 'dos Santos Aveiro',
      email: '',
      jobTitle: '',
      department: '',
      photo: '',
      active: true,
    };

    expect(userAvatarComponent.buildInitialsForUserAvatar(userWithMultiFirstnameAndSingleLastname)).toBe('D J');
    expect(userAvatarComponent.buildInitialsForUserAvatar(userWithSingleFirstnameAndMultiLastname)).toBe('L N');
    expect(userAvatarComponent.buildInitialsForUserAvatar(userWithMultiFirstnameAndLastname)).toBe('C d');
  });

  it('should return null for undefined/empty firstname', () => {
    const userWithUndefinedFirstname: User = {
      id: '',
      givenName: undefined,
      surname: 'Cena',
      email: '',
      jobTitle: '',
      department: '',
      photo: '',
      active: true,
    };

    const userWithEmptyFirstname: User = {
      id: '',
      givenName: '',
      surname: 'Cena',
      email: '',
      jobTitle: '',
      department: '',
      photo: '',
      active: true,
    };

    expect(userAvatarComponent.buildInitialsForUserAvatar(userWithEmptyFirstname)).toBeNull();
    expect(userAvatarComponent.buildInitialsForUserAvatar(userWithUndefinedFirstname)).toBeNull();
  });

  it('should return null for undefined/empty lastname', () => {
    const userWithUndefinedLastname: User = {
      id: '',
      givenName: 'John',
      surname: undefined,
      email: '',
      jobTitle: '',
      department: '',
      photo: '',
      active: true,
    };

    const userWithEmptyLastname: User = {
      id: '',
      givenName: 'John',
      surname: '',
      email: '',
      jobTitle: '',
      department: '',
      photo: '',
      active: true,
    };

    expect(userAvatarComponent.buildInitialsForUserAvatar(userWithEmptyLastname)).toBeNull();
    expect(userAvatarComponent.buildInitialsForUserAvatar(userWithUndefinedLastname)).toBeNull();
  });

  it('should return initials for firstname or lastname with leading space', () => {
    const userWithLeadingSpaceFirstname: User = {
      id: '',
      givenName: ' John',
      surname: 'Cena',
      email: '',
      jobTitle: '',
      department: '',
      photo: '',
      active: true,
    };

    const userWithLeadingSpaceLastname: User = {
      id: '',
      givenName: 'John',
      surname: ' Cena',
      email: '',
      jobTitle: '',
      department: '',
      photo: '',
      active: true,
    };

    expect(userAvatarComponent.buildInitialsForUserAvatar(userWithLeadingSpaceFirstname)).toBe('J C');
    expect(userAvatarComponent.buildInitialsForUserAvatar(userWithLeadingSpaceLastname)).toBe('J C');
  });
});
