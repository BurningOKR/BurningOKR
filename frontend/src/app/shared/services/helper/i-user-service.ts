import { Observable } from 'rxjs/internal/Observable';
import { User } from '../../model/api/user';

// eslint-disable-next-line @typescript-eslint/naming-convention
export interface IUserService {
  getUserById$(objectId: string): Observable<User>;

  getUsers$(): Observable<User[]>;

  addAdmin$(adminToAdd: User): Observable<User>;

  deleteAdmin$(adminToDeleteId: string): Observable<boolean>;

  getAdminIds$(): Observable<string[]>;
}
