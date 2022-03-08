import { TestBed } from '@angular/core/testing';

import { CookieHelperService } from './cookie-helper.service';
import {MaterialTestingModule} from '../../../testing/material-testing.module';

describe('CookieHelperService', () => {
  let service: CookieHelperService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ MaterialTestingModule ],
    });
    service = TestBed.inject(CookieHelperService);
    service.clearAllCookies();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should add cookie', () => {
    service.setCookieValue('test', 'test1234');
    expect(document.cookie).toBe('test=test1234');
    expect(service.isCookieSet('test')).toBeTruthy();
    expect(service.getCookieValue('test')).toBe('test1234');

    service.setCookieValue('test2', 'test5678');
    service.setCookieValue('test3', 'test3333', 2);
    expect(document.cookie).toBe('test=test1234; test2=test5678; test3=test3333');
    expect(service.isCookieSet('test2')).toBeTruthy();
    expect(service.getCookieValue('test2')).toBe('test5678');
    expect(service.isCookieSet('test3')).toBeTruthy();
    expect(service.getCookieValue('test3')).toBe('test3333');
    expect(service.getAllCookieNames().toString()).toBe('test,test2,test3');

  });

  it('should delete cookie', () => {

    service.setCookieValue('test', 'test1234');
    service.setCookieValue('test2', 'test5678');
    service.setCookieValue('test3', 'test3333', 2);
    expect(service.getAllCookieNames().toString()).toBe('test,test2,test3');

    service.deleteCookie('test');
    expect(service.getAllCookieNames().toString()).toBe('test2,test3');
    expect(service.isCookieSet('test')).toBeFalsy();
    expect(service.getCookieValue('test')).toBe('');
    expect(document.cookie).toBe('test2=test5678; test3=test3333');
  });

  it('should change cookie', () => {

    service.setCookieValue('test', 'test1234');
    service.setCookieValue('test2', 'test5678');
    service.setCookieValue('test3', 'test3333', 2);
    expect(service.getAllCookieNames().toString()).toBe('test,test2,test3');

    service.setCookieValue('test', 'new test value');
    expect(service.getAllCookieNames().toString()).toBe('test,test2,test3');
    expect(service.isCookieSet('test')).toBeTruthy();
    expect(service.getCookieValue('test')).toBe('new test value');
    expect(document.cookie).toBe('test2=test5678; test3=test3333; test=new test value');
  });

  it('should delete all cookies', () => {

    service.setCookieValue('test', 'test1234');
    service.setCookieValue('test2', 'test5678');
    service.setCookieValue('test3', 'test3333', 2);
    expect(service.getAllCookieNames().toString()).toBe('test,test2,test3');
    expect(document.cookie).toBe('test2=test5678; test3=test3333; test=test1234');

    service.clearAllCookies();
    expect(service.getAllCookieNames().toString()).toBe('');
    expect(service.isCookieSet('test')).toBeFalsy();
    expect(service.isCookieSet('test2')).toBeFalsy();
    expect(service.isCookieSet('test3')).toBeFalsy();

    expect(document.cookie).toBe('');
  });
});
