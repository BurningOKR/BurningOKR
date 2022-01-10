import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConvertSubmittedTopicDraftToTeamComponent } from './convert-submitted-topic-draft-to-team.component';
import {DialogComponent} from '../../shared/components/dialog-component/dialog.component';
import {MaterialTestingModule} from '../../testing/material-testing.module';
import {FormBuilder, ReactiveFormsModule} from '@angular/forms';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {HttpBackend, HttpClient} from '@angular/common/http';
import {AuthenticationService} from '../../core/auth/services/authentication.service';
import {LoggerConfig} from 'ngx-logger';
import {DatePipe} from '@angular/common';
import {Router} from '@angular/router';
import {StructureMapper} from '../../shared/services/mapper/structure.mapper';
import {Structure} from '../../shared/model/ui/OrganizationalUnit/structure';
import {of} from 'rxjs';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';

describe('SubmittedTopicDraftsConvertToTeamComponent', () => {
  let component: ConvertSubmittedTopicDraftToTeamComponent;
  let fixture: ComponentFixture<ConvertSubmittedTopicDraftToTeamComponent>;

  const formBuilder: FormBuilder = new FormBuilder();
  const structure: Structure[] = [];

  const httpClient: any = {
    get: jest.fn()
  };

  const structureMapper: any = {
    getSchemaOfAllExistingStructures$: jest.fn()
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [
        ConvertSubmittedTopicDraftToTeamComponent,
        DialogComponent
      ],
      imports: [
        MaterialTestingModule,
        ReactiveFormsModule,
        BrowserAnimationsModule
      ],
      providers: [
        {provide: FormBuilder, useValue: formBuilder},
        { provide: MAT_DIALOG_DATA, useValue: {} },
        { provide: MatDialogRef, useValue: {} },
        { provide: HttpClient, useValue: httpClient},
        { provide: AuthenticationService, useValue: {}},
        { provide: HttpBackend, useValue: {}},
        { provide: LoggerConfig, useValue: {}},
        { provide: DatePipe, useValue: {}},
        { provide: Router, useValue: {}},
        { provide: StructureMapper, useValue: structureMapper}

      ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ConvertSubmittedTopicDraftToTeamComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    structureMapper.getSchemaOfAllExistingStructures$.mockReturnValue(of(structure));
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
