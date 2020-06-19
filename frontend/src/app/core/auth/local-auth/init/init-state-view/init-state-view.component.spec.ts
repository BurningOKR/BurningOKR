import { TestBed } from '@angular/core/testing';

import { InitStateViewComponent } from './init-state-view.component';
import {
  ComponentFactoryResolver,
  CUSTOM_ELEMENTS_SCHEMA,
  NO_ERRORS_SCHEMA,
} from '@angular/core';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { InitService } from '../../../../services/init.service';
import { InitServiceMock } from '../../../../../shared/mocks/init-service-mock';
import { ComponentFactoryResolverMock } from '../../../../../shared/mocks/component-factory-resolver-mock';

describe('InitStateViewComponent', () => {
  const initServiceMock: InitServiceMock = new InitServiceMock();
  const componentFactoryResolverMock: ComponentFactoryResolverMock = new ComponentFactoryResolverMock();

  let component: any;
  let fixture: any;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
      ],
      declarations: [ InitStateViewComponent ],
      schemas: [ CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA ],
      providers: [
        {provide: InitService, useValue: initServiceMock},
        {provide: ComponentFactoryResolver, useValue: componentFactoryResolverMock},
      ]
    })
      .overrideComponent(InitStateViewComponent, {})
      .compileComponents();
    fixture = TestBed.createComponent(InitStateViewComponent);
    component = fixture.debugElement.componentInstance;
    fixture.detectChanges();
  });

  afterEach(() => {
    fixture.destroy();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
