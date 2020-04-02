import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CorporateObjectiveStructureComponent } from './corporate-objective-structure.component';

describe('CorporateObjectiveStructureComponent', () => {
  let component: CorporateObjectiveStructureComponent;
  let fixture: ComponentFixture<CorporateObjectiveStructureComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CorporateObjectiveStructureComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CorporateObjectiveStructureComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
