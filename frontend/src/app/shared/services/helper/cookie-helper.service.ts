import { Injectable } from '@angular/core';
import moment from 'moment/moment';

@Injectable({
  providedIn: 'root',
})
export class CookieHelperService {

  private cookieMap: Map<string, string>;

  constructor() {
    this.cookieMap = this.getCookieMap();
  }

  isCookieSet(cookieName: string): boolean {
    return this.cookieMap.has(cookieName);
  }

  getAllCookieNames(): string[] {
    const cookieNames: string[] = [];

    this.cookieMap.forEach((value, key) => cookieNames.push(key));

    return cookieNames;
  }

  getCookieValue(cookieName: string): string {
    if (this.cookieMap.has(cookieName)) {
      return this.cookieMap.get(cookieName);
    } else {
      return '';
    }
  }

  setCookieValue(cookieName: string, cookieValue: string, expiresInDays?: number, path?: string): void {
    const expiresOn: moment.Moment = moment().add(expiresInDays, 'days');
    const expireDate: string = expiresInDays ? `expires=${expiresOn.toDate().toUTCString()}` : '';
    const cookiePath: string = path ? `; path=${path}` : '';

    document.cookie = `${cookieName}=${cookieValue}; ${expireDate}${cookiePath}; SameSite=Strict`;
    this.cookieMap.set(cookieName, cookieValue);
  }

  clearAllCookies(): void {
    this.cookieMap.forEach((value, key) => {
      this.deleteCookie(key);
    });
  }

  deleteCookie(cookieName: string): void {
    document.cookie = `${cookieName}="";max-age=0; SameSite=Strict`;
    this.cookieMap.delete(cookieName);
  }

  private getCookieMap(): Map<string, string> {
    const cookieArray: string[] = document.cookie.split('; ');
    const cookieMap: Map<string, string> = new Map<string, string>();

    cookieArray.forEach(cookie => {
      const valueDelimiterPosition: number = cookie.indexOf('=');
      const cookieName: string = cookie.substr(0, valueDelimiterPosition);
      const cookieValue: string = cookie.substr(valueDelimiterPosition + 1);

      cookieMap.set(cookieName, cookieValue);
    });

    return cookieMap;
  }
}
