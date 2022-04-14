import { Injectable } from '@angular/core';
import { LocalAuthTypeHandlerService } from './local-auth-type-handler.service';
import { OAuthService } from 'angular-oauth2-oidc';
import { FetchingService } from '../../../services/fetching.service';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class DemoAuthTypeHandlerService extends LocalAuthTypeHandlerService {

}
