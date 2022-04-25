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
    deleteAdmin$: jest.fn(),
    getActiveUsers$: jest.fn()
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
    active: false
  };

  beforeEach(() => {
    userApiServiceMock.getUserById$.mockReset();
    userApiServiceMock.getUsers$.mockReset();
    userApiServiceMock.addAdmin$.mockReset();
    userApiServiceMock.deleteAdmin$.mockReset();
    userApiServiceMock.getActiveUsers$.mockReset();
    userService = new UserService(userApiServiceMock);

    userApiServiceMock.getUsers$.mockReturnValue(of([copyUser(mockUser1), copyUser(mockUser2), copyUser(mockUser3)]));
    userApiServiceMock.getActiveUsers$.mockReturnValue(of([copyUser(mockUser1), copyUser(mockUser2)]))
  });

  it('getAllUsers$ should return User from ApiService', done => {
    const expected: User[] = [mockUser1, mockUser2, mockUser3];

    const actual$: Observable<User[]> = userService.getAllUsers$();

    actual$.subscribe((actual: User[]) => {
      expect(actual)
        .toEqual(expected);
      done();
    });
  });
  it('getAllUsers$ should return User from cache', done => {
    const expected: User[] = [mockUser1, mockUser2, mockUser3];

    const fromApi$: Observable<User[]> = userService.getAllUsers$();
    const fromCache$: Observable<User[]> = userService.getAllUsers$();

    fromApi$.subscribe((actual: User[]) => {
      expect(actual)
          .toEqual(expected);
    });
    fromCache$.subscribe((actual: User[]) => {
      expect(actual)
          .toEqual(expected);
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

  it('getAllActiveUsers$ should only return active users', done => {
    const expected: User[] = [mockUser1, mockUser2];
    const actual$: Observable<User[]> = userService.getAllActiveUsers$();

    actual$.subscribe((actual: User[]) => {
      expect(actual)
        .toEqual(expected);
      done();
    });
  })

  function copyUser(user: User): User {
    return new User(user.id, user.givenName, user.surname, user.email, user.jobTitle, user.department, user.photo, user.active);
  }

});
