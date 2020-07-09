import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DemoHomeComponent } from './demo-home.component';
import { MaterialTestingModule } from '../../testing/material-testing.module';
import { MatDialog } from '@angular/material';

const matDialog: any = {
  open: jest.fn()
};

describe('DemoHomeComponent', () => {
  let component: DemoHomeComponent;
  let fixture: ComponentFixture<DemoHomeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ MaterialTestingModule ],
      providers: [ { provide: MatDialog, useValue: matDialog } ],
      declarations: [ DemoHomeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DemoHomeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
