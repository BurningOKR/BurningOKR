import { Observable, of } from 'rxjs';
import { PasswordResetMailData } from '../../core/auth/local-auth/password-service/password.service';

export class PasswordServiceMock {
  sendPasswordResetEmail$(data: PasswordResetMailData): Observable<object> {
    return of();
  }
}
