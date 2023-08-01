import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChildUnitAvatarComponent } from './child-unit-avatar.component';

describe('ChildUnitAvatarComponent', () => {
  let component: ChildUnitAvatarComponent;
  let fixture: ComponentFixture<ChildUnitAvatarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ChildUnitAvatarComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ChildUnitAvatarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
