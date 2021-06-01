// tslint:disable:rxjs-finnish

import { DepartmentTabDescriptionComponent } from './department-tab-description.component';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { SharedModule } from '../../../../shared/shared.module';
import { MaterialTestingModule } from '../../../../testing/material-testing.module';
import { TopicDescriptionMapper } from '../../../../shared/services/mapper/topic-description-mapper.service';
import { MatDialog } from '@angular/material';
import { OkrDepartment } from '../../../../shared/model/ui/OrganizationalUnit/okr-department';
import { OkrTopicDescription } from '../../../../shared/model/ui/OrganizationalUnit/okr-topic-description';
import { of } from 'rxjs';
import { ContextRole } from '../../../../shared/model/ui/context-role';
import { DepartmentDescriptionEditFormComponent } from './department-description-edit-form/department-description-edit-form.component';

describe('DepartmentTabDescription', () => {
  let component: DepartmentTabDescriptionComponent;
  let fixture: ComponentFixture<DepartmentTabDescriptionComponent>;

  const topicDescriptionMapperService: any = {
    getTopicDescriptionById$: jest.fn()
  };

  const matDialog: any = {
    open: jest.fn()
  };

  const dialogReference: any = {
    afterClosed: jest.fn()
  };

  let department: OkrDepartment;
  let topicDescription: OkrTopicDescription;
  let userRole: ContextRole;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DepartmentTabDescriptionComponent],
      imports: [SharedModule, MaterialTestingModule],
      providers: [
        { provide: TopicDescriptionMapper, useValue: topicDescriptionMapperService },
        { provide: MatDialog, useValue: matDialog }
      ]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    department = new OkrDepartment(1, 'test', [], 2, 'label', null, null, [], true, false);
    topicDescription = new OkrTopicDescription(4, 'test', null, [], [], '', '', '', new Date(), '', '', '');
    userRole = new ContextRole();

    topicDescriptionMapperService.getTopicDescriptionById$.mockReset();
    topicDescriptionMapperService.getTopicDescriptionById$.mockReturnValue(of(topicDescription));

    matDialog.open.mockReset();
    matDialog.open.mockReturnValue(dialogReference);

    dialogReference.afterClosed.mockReset();
    dialogReference.afterClosed.mockReturnValue(of(of(topicDescription)));
  });

  it('should create', () => {
    fixture = TestBed.createComponent(DepartmentTabDescriptionComponent);
    component = fixture.componentInstance;

    component.department = department;
    component.currentUserRole = userRole;

    fixture.detectChanges();

    expect(component)
      .toBeTruthy();
  });

  it('ngOnInit sets canEdit to false, when the user is not admin or manager', () => {
    fixture = TestBed.createComponent(DepartmentTabDescriptionComponent);
    component = fixture.componentInstance;

    component.department = department;
    component.currentUserRole = userRole;

    fixture.detectChanges();

    component.ngOnInit();

    expect(component.canEdit)
      .toBeFalsy();
  });

  it('ngOnInit sets canEdit to true, when the user is admin', () => {
    userRole.isAdmin = true;

    fixture = TestBed.createComponent(DepartmentTabDescriptionComponent);
    component = fixture.componentInstance;

    component.department = department;
    component.currentUserRole = userRole;

    fixture.detectChanges();

    component.ngOnInit();

    expect(component.canEdit)
      .toBeTruthy();
  });

  it('ngOnInit sets canEdit to true, when the user is manager', () => {
    userRole.isOKRManager = true;

    fixture = TestBed.createComponent(DepartmentTabDescriptionComponent);
    component = fixture.componentInstance;

    component.department = department;
    component.currentUserRole = userRole;

    fixture.detectChanges();

    component.ngOnInit();

    expect(component.canEdit)
      .toBeTruthy();
  });

  it('ngOnInit sets canEdit to true, when the user is manager and admin', () => {
    userRole.isOKRManager = true;
    userRole.isAdmin = true;

    fixture = TestBed.createComponent(DepartmentTabDescriptionComponent);
    component = fixture.componentInstance;

    component.department = department;
    component.currentUserRole = userRole;

    fixture.detectChanges();

    component.ngOnInit();

    expect(component.canEdit)
      .toBeTruthy();
  });

  it('ngOnInit loads the OkrTopicDescription', () => {
    fixture = TestBed.createComponent(DepartmentTabDescriptionComponent);
    component = fixture.componentInstance;

    component.department = department;
    component.currentUserRole = userRole;

    fixture.detectChanges();

    component.ngOnInit();

    expect(topicDescriptionMapperService.getTopicDescriptionById$)
      .toHaveBeenCalled();
  });

  it('ngOnChanges loads the OkrTopicDescription', () => {
    fixture = TestBed.createComponent(DepartmentTabDescriptionComponent);
    component = fixture.componentInstance;

    component.department = department;
    component.currentUserRole = userRole;

    fixture.detectChanges();

    component.ngOnChanges();

    expect(topicDescriptionMapperService.getTopicDescriptionById$)
      .toHaveBeenCalled();
  });

  it('openDialog, opens dialog with data', () => {
    fixture = TestBed.createComponent(DepartmentTabDescriptionComponent);
    component = fixture.componentInstance;

    component.department = department;
    component.currentUserRole = userRole;

    fixture.detectChanges();

    component.openDialog(topicDescription);

    expect(matDialog.open)
      .toHaveBeenCalledWith(DepartmentDescriptionEditFormComponent, {
        width: '600px', data: {
          departmentId: department.id,
          okrTopicDescription: topicDescription
        }
      });
  });

  it('openDialog, updates topicDescription', done => {
    fixture = TestBed.createComponent(DepartmentTabDescriptionComponent);
    component = fixture.componentInstance;

    component.department = department;
    component.currentUserRole = userRole;

    fixture.detectChanges();

    // (R.J. 22.01.2020): mockReset is needed here, because this method is also called by ngOnInit, which is called by detectChanges.
    topicDescriptionMapperService.getTopicDescriptionById$.mockReset();

    component.openDialog(topicDescription);

    setTimeout(() => {
      expect(topicDescriptionMapperService.getTopicDescriptionById$)
        .toHaveBeenCalled();
      done();
    }, 300);

  });
});
