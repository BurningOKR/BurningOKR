import { Injectable } from '@angular/core';
import { ApiHttpService } from '../../../services/api-http.service';
import { Observable, of } from 'rxjs';

export class PasswordResetData {
  emailIdentifier: string;
  password: string;
}

export class PasswordChangeData {
  oldPassword: string;
  newPassword: string;
  email: string;
}

export class PasswordResetMailData {
  email: string;
}

@Injectable({
  providedIn: 'root'
})
export class PasswordService {
  constructor(private apiService: ApiHttpService) { }

  setPasswordWithEmailIdentifier$(data: PasswordResetData): Observable<object> {
    return this.apiService.postData$('local-users/password', data, (error => {
      return of(error);
    }));
  }

  setPasswordWhileUserIsLoggedin$(data: PasswordChangeData): Observable<PasswordChangeData> {
    return this.apiService.postData$('local-users/change-password', data);
  }

  sendPasswordResetEmail$(data: PasswordResetMailData): Observable<PasswordResetMailData> {
    return this.apiService.postData$('local-users/forgot-password', data);
  }
}
