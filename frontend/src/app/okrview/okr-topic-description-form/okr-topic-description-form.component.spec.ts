import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { OkrTopicDescriptionFormComponent } from './okr-topic-description-form.component';
import { MaterialTestingModule } from '../../testing/material-testing.module';
import { UserService } from '../../shared/services/helper/user.service';
import { of } from 'rxjs';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { SharedModule } from '../../shared/shared.module';
import { NgxMatSelectSearchModule } from 'ngx-mat-select-search';

const userServiceMock: any = {
  getAllUsers$: jest.fn(),
};

describe('OkrTopicDescriptionFormComponent', () => {
  let component: OkrTopicDescriptionFormComponent;
  let fixture: ComponentFixture<OkrTopicDescriptionFormComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
      declarations: [OkrTopicDescriptionFormComponent],
      imports: [MaterialTestingModule, FormsModule, ReactiveFormsModule, NoopAnimationsModule, SharedModule, NgxMatSelectSearchModule],
      providers: [
        { provide: UserService, useValue: userServiceMock },
      ],
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OkrTopicDescriptionFormComponent);
    component = fixture.componentInstance;

    component.descriptionForm = new FormGroup({
      name: new FormControl('', Validators.maxLength(255)),
      description: new FormControl('', Validators.maxLength(1024)),
      contributesTo: new FormControl('', Validators.maxLength(1024)),
      delimitation: new FormControl('', Validators.maxLength(1024)),
      beginning: new FormControl(),
      dependencies: new FormControl('', Validators.maxLength(1024)),
      resources: new FormControl('', Validators.maxLength(1024)),
      handoverPlan: new FormControl('', Validators.maxLength(1024)),
      initiatorId: new FormControl(),
      startTeam: new FormControl([]),
      stakeholders: new FormControl([]),
    });

    fixture.detectChanges();

    userServiceMock.getAllUsers$.mockReset();
    userServiceMock.getAllUsers$.mockReturnValue(of([]));
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });

  it('should get all users on init', () => {
    component.ngOnInit();

    expect(userServiceMock.getAllUsers$)
      .toHaveBeenCalled();
  });

  it('should patch minBeginning', () => {
    const date: Date = new Date('2000-01-01');
    component.descriptionForm.get('beginning').patchValue(date);
    component.ngOnInit();
    expect(component.minBeginn.getTime).toEqual(date.getTime);
  });

  it('should not patch minBeginning', () => {
    const date: Date = new Date('2000-02-01');
    component.descriptionForm.get('beginning').patchValue(date);
    component.minBeginn = new Date('2000-01-01');
    component.ngOnInit();
    expect(component.minBeginn.getTime()).toBeLessThan(date.getTime());
    expect(component.minBeginn.getTime()).toEqual(new Date('2000-01-01').getTime());
  });
});
