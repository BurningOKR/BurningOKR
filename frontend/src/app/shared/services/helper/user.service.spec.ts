import { UserService } from './user.service';
import { User } from '../../model/api/user';
import { Observable, of } from 'rxjs';
import { AdminUser } from '../../model/api/admin-user';

describe('UserService', () => {

  let userService: UserService;
  const userApiServiceMock: any = {
    getUserById$: jest.fn(),
    getUsers$: jest.fn(),
    addAdmin$: jest.fn(),
    deleteAdmin$: jest.fn()
  };

  const mockUser1: User = {
    id: '123456',
    givenName: 'testFirstName',
    surname: 'testLastName',
    department: 'testDepartment',
    email: 'test@tester.test',
    jobTitle: 'testTitle',
    photo: 'photoString',
    active: true
  };
  const mockUser2: User = {
    id: '654321',
    givenName: 'testFirstName2',
    surname: 'testLastName2',
    department: 'testDepartment2',
    email: 'test@tester.test2',
    jobTitle: 'testTitle2',
    photo: 'photoString2',
    active: true
  };

  const mockUser3: User = {
    id: 'TEST',
    givenName: 'testFirstName3',
    surname: 'testLastName3',
    department: 'testDepartment3',
    email: 'test@tester.test3',
    jobTitle: 'testTitle3',
    photo: 'photoString3',
    active: true
  };

  beforeEach(() => {
    userApiServiceMock.getUserById$.mockReset();
    userApiServiceMock.getUsers$.mockReset();
    userApiServiceMock.addAdmin$.mockReset();
    userApiServiceMock.deleteAdmin$.mockReset();
    userService = new UserService(userApiServiceMock);
  });

  it('getAllUsers$ should return User from ApiService', done => {
    userApiServiceMock.getUsers$.mockReturnValueOnce(of([mockUser1, mockUser2, mockUser3]));

    const actual$: Observable<User[]> = userService.getAllUsers$();

    actual$.subscribe((users: User[]) => {
      expect(users)
        .toContain(mockUser1);
      expect(users)
        .toContain(mockUser2);
      expect(users)
        .toContain(mockUser3);
      done();
    });
  });
  it('getAllUsers$ should return User from cache', done => {
    userApiServiceMock.getUsers$.mockReturnValueOnce(of([mockUser1, mockUser2, mockUser3]));
    userApiServiceMock.getUsers$.mockReturnValueOnce(of([mockUser1, mockUser2]));

    const fromApi$: Observable<User[]> = userService.getAllUsers$();
    const fromCache$: Observable<User[]> = userService.getAllUsers$();
// Todo 26.05.2020 dturnschek; This is not nice. Change TSLint to allow userService['users$'] = something; ?
    fromApi$.subscribe((users: User[]) => {
      expect(users)
        .toContain(mockUser1);
      expect(users)
        .toContain(mockUser2);
      expect(users)
        .toContain(mockUser3);
    });
    fromCache$.subscribe((users: User[]) => {
      expect(users)
        .toContain(mockUser1);
      expect(users)
        .toContain(mockUser2);
      expect(users)
        .toContain(mockUser3);
      done();
    });
  });

  it('getUserById$ should return a user from ApiService', done => {
    userApiServiceMock.getUserById$.mockReturnValueOnce(of(mockUser1));

    const actual$: Observable<User> = userService.getUserById$(mockUser1.id);

    actual$.subscribe((user: User) => {
      expect(user)
        .toEqual(mockUser1);
      done();
    });
  });

  it('getUserById$ should return a user from cache', done => {
    userApiServiceMock.getUsers$.mockReturnValueOnce(of([mockUser1, mockUser2, mockUser3]));

    userService.getAllUsers$();

    const  actual$: Observable<User> = userService.getUserById$(mockUser1.id);

    actual$.subscribe((user: User) => {
      expect(user)
        .toEqual(mockUser1);
      expect(userApiServiceMock.getUserById$)
        .toHaveBeenCalledTimes(0);
      done();
    });
  });

  it('getUserById$ should return undefined', done => {
    userApiServiceMock.getUserById$.mockReturnValueOnce(of(undefined));

    const actual$: Observable<User> = userService.getUserById$('notAnID');

    actual$.subscribe((user: User) => {
      expect(user)
        .toBeUndefined();
      done();
    });
  });

  it('addAdmin$ should call userApiService.addAdmin$', done => {
    userApiServiceMock.addAdmin$.mockReturnValueOnce(of(mockUser1));

    const actual$: Observable<AdminUser> = userService.addAdmin$(mockUser1);

    actual$.subscribe((mockAdmin: AdminUser) => {
      expect(userApiServiceMock.addAdmin$)
        .toHaveBeenCalledWith(new AdminUser(mockUser1.id));
      expect(mockAdmin)
        .toEqual(mockUser1);
      done();
    });
  });

  it('deleteAdmin$ should return true if backend returns true', done => {
    userApiServiceMock.deleteAdmin$.mockReturnValueOnce(of(true));

    const actual$: Observable<boolean> = userService.deleteAdmin$(mockUser1.id);

    actual$.subscribe((returnValue: boolean) => {
      expect(returnValue)
        .toBeTruthy();
      done();
    });
  });

  it('deleteAdmin$ should return false if backend returns false', done => {
    userApiServiceMock.deleteAdmin$.mockReturnValueOnce(of(false));

    const actual$: Observable<boolean> = userService.deleteAdmin$(mockUser1.id);

    actual$.subscribe((returnValue: boolean) => {
      expect(returnValue)
        .toBeFalsy();
      done();
    });
  });
});
