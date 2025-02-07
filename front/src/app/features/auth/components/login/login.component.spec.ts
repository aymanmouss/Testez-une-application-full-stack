import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule, Validators } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { describe, expect, it } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { LoginComponent } from './login.component';
import { of, throwError } from 'rxjs';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { LoginRequest } from '../../interfaces/loginRequest.interface';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authServiceMock: jest.Mocked<AuthService>;
  let routerMock: jest.Mocked<Router>;
  let sessionServiceMock: jest.Mocked<SessionService>;

  beforeEach(async () => {
    authServiceMock = {
      login: jest.fn(),
    } as any;

    sessionServiceMock = {
      logIn: jest.fn(),
    } as any;

    routerMock = {
      navigate: jest.fn(),
    } as any;

    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [
        { provide: AuthService, useValue: authServiceMock },
        { provide: SessionService, useValue: sessionServiceMock },
        { provide: Router, useValue: routerMock },
      ],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
      ],
    }).compileComponents();
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
  describe('Form Initialization', () => {
    it('should initialize with empty form fields', () => {
      expect(component.form.get('email')?.value).toBe('');
      expect(component.form.get('password')?.value).toBe('');
    });

    it('should have required validators', () => {
      const emailControl = component.form.get('email');
      const passwordControl = component.form.get('password');
      expect(emailControl?.hasValidator(Validators.required)).toBeTruthy();
      expect(emailControl?.hasValidator(Validators.email)).toBeTruthy();
      expect(passwordControl?.hasValidator(Validators.required)).toBeTruthy();
    });
  });
  describe('Form Validation', () => {
    it('should be invalid when empty', () => {
      expect(component.form.valid).toBeFalsy();
    });
    it('should be invalid with invalid email', () => {
      const emailControl = component.form.get('email');
      emailControl?.setValue('invalidEmail');
      expect(emailControl?.valid).toBeFalsy;
    });
  });
  it('should be valid with valid email', () => {
    const emailControl = component.form.get('email');
    emailControl?.setValue('test@test.fr');
    expect(emailControl?.valid).toBeTruthy();
  });
  describe('Submit', () => {
    const mockLoginRequest: LoginRequest = {
      email: 'test@example.com',
      password: 'password123',
    };
    const mockSessionInfo: SessionInformation = {
      token: 'fake-jwt-token',
      type: 'Bearer',
      id: 1,
      username: 'testUser',
      firstName: 'Test',
      lastName: 'User',
      admin: false,
    };

    beforeEach(() => {
      component.form.patchValue(mockLoginRequest);
    });
    it('should call auth service with form values on submit', () => {
      authServiceMock.login.mockReturnValue(of(mockSessionInfo));
      component.submit();
      expect(authServiceMock.login).toBeCalledWith(mockLoginRequest);
    });
    it('should navigate to sessions page on successful login', () => {
      authServiceMock.login.mockReturnValue(of(mockSessionInfo));
      component.submit();
      expect(sessionServiceMock.logIn).toHaveBeenCalledWith(mockSessionInfo);
      expect(routerMock.navigate).toBeCalledWith(['/sessions']);
    });
    it('should set onError to true on login failure', () => {
      authServiceMock.login.mockReturnValue(
        throwError(() => new Error('Login failed'))
      );
      component.submit();
      expect(component.onError).toBeTruthy();
      expect(routerMock.navigate).not.toHaveBeenCalled();
    });
  });
});
