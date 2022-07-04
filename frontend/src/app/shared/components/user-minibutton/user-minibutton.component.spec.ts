import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { AvatarComponent } from 'ngx-avatars';
import { UserMinibuttonComponent } from './user-minibutton.component';
import { MaterialTestingModule } from '../../../testing/material-testing.module';
import { UserAvatarComponent } from '../user-avatar/user-avatar.component';
import { UserService } from '../../services/helper/user.service';
import { of } from 'rxjs';

const userService: any = {
  getUserById$: jest.fn()
};

describe('UserMinibuttonComponent', () => {
  let component: UserMinibuttonComponent;
  let fixture: ComponentFixture<UserMinibuttonComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [
        UserMinibuttonComponent,
        UserAvatarComponent,
        AvatarComponent
      ],
      imports: [MaterialTestingModule],
      providers: [
        { provide: UserService, useValue: userService }
      ]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    userService.getUserById$.mockReset();
    userService.getUserById$.mockReturnValue(of(null));
  });

  it('should create', () => {
    fixture = TestBed.createComponent(UserMinibuttonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    expect(component)
      .toBeTruthy();
  });

  it('should get user on init', () => {
    fixture = TestBed.createComponent(UserMinibuttonComponent);
    component = fixture.componentInstance;

    component.userId = 'some user';

    fixture.detectChanges();

    expect(userService.getUserById$)
      .toHaveBeenCalled();
  });

  it('should get user by id on init', () => {
    fixture = TestBed.createComponent(UserMinibuttonComponent);
    component = fixture.componentInstance;

    component.userId = 'some user';

    fixture.detectChanges();

    expect(userService.getUserById$)
      .toHaveBeenCalledWith('some user');
  });

  it('should get user by id (null) on init', () => {
    fixture = TestBed.createComponent(UserMinibuttonComponent);
    component = fixture.componentInstance;

    component.userId = null;

    fixture.detectChanges();

    expect(userService.getUserById$)
      .toHaveBeenCalledWith(null);
  });

  it('should get user by id (undefined) on init', () => {
    fixture = TestBed.createComponent(UserMinibuttonComponent);
    component = fixture.componentInstance;

    component.userId = undefined;

    fixture.detectChanges();

    expect(userService.getUserById$)
      .toHaveBeenCalledWith(undefined);
  });

  it('should not get user onChanges, changes empty', () => {
    fixture = TestBed.createComponent(UserMinibuttonComponent);
    component = fixture.componentInstance;

    component.userId = 'some user';

    fixture.detectChanges();

    userService.getUserById$.mockReset(); // get a clean mock, after ngOnInit was called

    component.ngOnChanges({});

    expect(userService.getUserById$)
      .toHaveBeenCalledTimes(0);
  });

  it('should not get user onChanges, changes do not contain userId', () => {
    fixture = TestBed.createComponent(UserMinibuttonComponent);
    component = fixture.componentInstance;

    component.userId = 'some user';

    fixture.detectChanges();

    userService.getUserById$.mockReset(); // get a clean mock, after ngOnInit was called

    component.ngOnChanges({
      test: { currentValue: 'test', previousValue: 'also test', firstChange: false, isFirstChange: jest.fn() }
    });

    expect(userService.getUserById$)
      .toHaveBeenCalledTimes(0);
  });

  it('should not get user onChanges, changes do contain userId but firstChange is true', () => {
    fixture = TestBed.createComponent(UserMinibuttonComponent);
    component = fixture.componentInstance;

    component.userId = 'some user';

    fixture.detectChanges();

    userService.getUserById$.mockReset(); // get a clean mock, after ngOnInit was called

    component.ngOnChanges({
      userId: { currentValue: 'test', previousValue: 'some user', firstChange: true, isFirstChange: jest.fn() }
    });

    expect(userService.getUserById$)
      .toHaveBeenCalledTimes(0);
  });

  it('should get user onChanges, changes do contain userId, firstChange is false', () => {
    fixture = TestBed.createComponent(UserMinibuttonComponent);
    component = fixture.componentInstance;

    component.userId = 'some user';

    fixture.detectChanges();

    userService.getUserById$.mockReset(); // get a clean mock, after ngOnInit was called

    component.ngOnChanges({
      userId: { currentValue: 'test', previousValue: 'some user', firstChange: false, isFirstChange: jest.fn() }
    });

    expect(userService.getUserById$)
      .toHaveBeenCalled();
  });

  it('should get user by id onChanges, changes do contain userId, firstChange is false', () => {
    fixture = TestBed.createComponent(UserMinibuttonComponent);
    component = fixture.componentInstance;

    component.userId = 'some user';

    fixture.detectChanges();

    userService.getUserById$.mockReset(); // get a clean mock, after ngOnInit was called

    component.userId = 'test';
    component.ngOnChanges({
      userId: { currentValue: 'test', previousValue: 'some user', firstChange: false, isFirstChange: jest.fn() }
    });

    expect(userService.getUserById$)
      .toHaveBeenCalledWith('test');
  });
});
