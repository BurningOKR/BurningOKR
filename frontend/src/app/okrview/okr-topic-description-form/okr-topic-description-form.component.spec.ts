import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OkrTopicDescriptionFormComponent } from './okr-topic-description-form.component';
import { MaterialTestingModule } from '../../testing/material-testing.module';
import { UserService } from '../../shared/services/helper/user.service';
import { of } from 'rxjs';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

const userServiceMock: any = {
  getAllUsers$: jest.fn()
};

describe('OkrTopicDescriptionFormComponent', () => {
  let component: OkrTopicDescriptionFormComponent;
  let fixture: ComponentFixture<OkrTopicDescriptionFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      schemas: [ CUSTOM_ELEMENTS_SCHEMA ],
      declarations: [ OkrTopicDescriptionFormComponent ],
      imports: [ MaterialTestingModule, FormsModule, ReactiveFormsModule, NoopAnimationsModule ],
      providers: [
        { provide: UserService, useValue: userServiceMock }
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OkrTopicDescriptionFormComponent);
    component = fixture.componentInstance;

    component.descriptionForm = new FormGroup({
      name: new FormControl('', Validators.maxLength(255)),
      acceptanceCriteria: new FormControl('', Validators.maxLength(1024)),
      contributesTo: new FormControl('', Validators.maxLength(1024)),
      delimitation: new FormControl('', Validators.maxLength(1024)),
      beginning: new FormControl(),
      dependencies: new FormControl('', Validators.maxLength(1024)),
      resources: new FormControl('', Validators.maxLength(1024)),
      handoverPlan: new FormControl('', Validators.maxLength(1024)),
      initiatorId: new FormControl(),
      startTeam: new FormControl([]),
      stakeholders: new FormControl([])
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
});
