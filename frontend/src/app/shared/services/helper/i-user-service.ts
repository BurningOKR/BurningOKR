import { Observable } from 'rxjs/internal/Observable';
import { User } from '../../model/api/user';

// tslint:disable-next-line:interface-name
export interface IUserService {
  getUserById$(objectId: string): Observable<User>;
  getUsers$(): Observable<User[]>;
  addAdmin$(adminToAdd: User): Observable<User>;
  deleteAdmin$(adminToDeleteId: string): Observable<boolean>;
  getAdminIds$(): Observable<string[]>;
}
