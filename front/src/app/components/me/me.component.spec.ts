import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { SessionService } from 'src/app/services/session.service';
import { describe, expect, it } from '@jest/globals';

import { MeComponent } from './me.component';
import { UserService } from 'src/app/services/user.service';
import { of } from 'rxjs';
import { Router } from '@angular/router';

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let userServiceMock: jest.Mocked<UserService>;
  let mockSnackBar: jest.Mocked<MatSnackBar>;
  let routerMock: jest.Mocked<Router>;

  const mockSessionService = {
    sessionInformation: {
      admin: false,
      id: 1,
    },
    logOut: jest.fn(),
  };

  const userMock = {
    id: 1,
    email: 'test@test.fr',
    lastName: 'John',
    firstName: 'Doe',
    admin: false,
    password: 'string123',
    createdAt: new Date(),
    updatedAt: new Date(),
  };

  beforeEach(async () => {
    userServiceMock = {
      getById: jest.fn().mockReturnValue(of(userMock)),
      delete: jest.fn().mockReturnValue(of({})),
    } as any;

    mockSnackBar = {
      open: jest.fn(),
    } as any;

    routerMock = {
      navigate: jest.fn(),
    } as any;

    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: UserService, useValue: userServiceMock },
        { provide: MatSnackBar, useValue: mockSnackBar },
        { provide: Router, useValue: routerMock },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
  it('should load user data on init', () => {
    expect(userServiceMock.getById).toHaveBeenCalledTimes(1);
    expect(userServiceMock.getById).toHaveBeenCalledWith(
      mockSessionService.sessionInformation.id.toString()
    );
    expect(component.user).toEqual(userMock);
  });
  it('should handle back navigation', () => {
    const mockBack = jest.fn();
    window.history.back = mockBack;
    component.back();
    expect(mockBack).toHaveBeenCalled();
  });
  it('should handle user deletion', () => {
    component.delete();
    expect(userServiceMock.delete).toBeCalledWith(
      mockSessionService.sessionInformation.id.toString()
    );
    expect(mockSnackBar.open).toBeCalledWith(
      'Your account has been deleted !',
      'Close',
      { duration: 3000 }
    );
    expect(mockSessionService.logOut).toBeCalled();
    expect(routerMock.navigate).toBeCalledWith(['/']);
  });
});
