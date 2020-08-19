import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DemoMainViewComponent } from './demo-main-view.component';
import { MaterialTestingModule } from '../../testing/material-testing.module';
import { OkrToolbarBareComponent } from '../../shared/components/okr-toolbar-bare/okr-toolbar-bare.component';
import { RouterTestingModule } from '@angular/router/testing';
import { MatDialog } from '@angular/material';
import { DemoFooterComponent } from '../demo-footer/demo-footer.component';
import { ScrollTopComponent } from '../scroll-top/scroll-top.component';

const matDialog: any = {
  open: jest.fn()
};

describe('DemoMainViewComponent', () => {
  let component: DemoMainViewComponent;
  let fixture: ComponentFixture<DemoMainViewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ MaterialTestingModule, RouterTestingModule ],
      providers: [ { provide: MatDialog, useValue: matDialog } ],
      declarations: [ DemoMainViewComponent, OkrToolbarBareComponent, DemoFooterComponent, ScrollTopComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DemoMainViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
