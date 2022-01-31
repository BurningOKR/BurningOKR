import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class CookieHelperService {

  private cookieMap: Map<string, string>;

  constructor() {
    this.cookieMap = this.getCookieMap();
  }

  isCookieSet (cookieName: string): boolean {

    return this.cookieMap.has(cookieName);
  }

  getCookieNames (): string[] {

    const cookieNames: string[] = [];

    this.cookieMap.forEach( (value, key) => cookieNames.push(key))

    return cookieNames;
  }

  getCookieValue (cookieName: string): string {

    if (this.cookieMap.has(cookieName)) {

      return this.cookieMap.get(cookieName);
    } else {

      return '';
    }
  }

  setCookieValue (cookieName: string, cookieValue: string, expiresInDays?: number, path?: string): void {

    const expiresOn: Date = new Date();
    expiresOn.setTime(expiresOn.getTime() + expiresInDays * 24 * 60 * 60 * 1000);
    const expireDate = expiresInDays ? `expires=${expiresOn.toUTCString()}` : '';
    const cookiePath = path ? `; path=${path}` : '';
    document.cookie=`${cookieName}=${cookieValue}; ${expireDate}${cookiePath}; SameSite=Strict`;
  }

  private getCookieMap (): Map<string, string> {

    const cookieArray = document.cookie.split('; ');
    const cookieMap = new Map<string, string> ();

    cookieArray.forEach( cookie => {
      const valueDelimiterPosition = cookie.indexOf('=');
      const cookieName = cookie.substr(0, valueDelimiterPosition)
      const cookieValue = cookie.substr(valueDelimiterPosition + 1)

      cookieMap.set(cookieName, cookieValue);
    })

    return cookieMap;
  }
}
