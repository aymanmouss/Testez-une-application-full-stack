import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule, Validators } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { describe, expect } from '@jest/globals';
import { RegisterComponent } from './register.component';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { RegisterRequest } from '../../interfaces/registerRequest.interface';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authServiceMock: jest.Mocked<AuthService>;
  let routerMock: jest.Mocked<Router>;

  authServiceMock = {
    register: jest.fn(),
  } as any;

  routerMock = {
    navigate: jest.fn(),
  } as any;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      providers: [
        { provide: AuthService, useValue: authServiceMock },
        { provide: Router, useValue: routerMock },
      ],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('Form Initialization', () => {
    it('should initialize with empty form fields', () => {
      expect(component.form.get('email')?.value).toBe('');
      expect(component.form.get('firstName')?.value).toBe('');
      expect(component.form.get('lastName')?.value).toBe('');
      expect(component.form.get('password')?.value).toBe('');
    });

    it('should have required validators', () => {
      const emailControl = component.form.get('email');
      const firstNameControl = component.form.get('firstName');
      const lastNameControl = component.form.get('lastName');
      const passwordControl = component.form.get('password');
      expect(emailControl?.hasValidator(Validators.required)).toBeTruthy();
      expect(emailControl?.hasValidator(Validators.email)).toBeTruthy();
      expect(firstNameControl?.hasValidator(Validators.required)).toBeTruthy();
      expect(lastNameControl?.hasValidator(Validators.required)).toBeTruthy();
      expect(passwordControl?.hasValidator(Validators.required)).toBeTruthy();
    });

    describe('Form Validation', () => {
      it('should be invalid when empty', () => {
        expect(component.form.valid).toBeFalsy();
      });

      it('should be invalid with invalid email', () => {
        const emailControl = component.form.get('email');
        emailControl?.setValue('invalidemail');
        expect(emailControl?.valid).toBeFalsy();
      });

      it('should be valid with valid email', () => {
        const emailControl = component.form.get('email');
        emailControl?.setValue('test@email.fr');
        expect(emailControl?.valid).toBeTruthy();
      });
    });

    describe('Submit', () => {
      const registerRequest: RegisterRequest = {
        email: 'test@test.fr',
        firstName: 'John',
        lastName: 'Doe',
        password: 'Doe1234',
      };
      beforeEach(() => {
        component.form.patchValue(registerRequest);
      });

      it('should set onError to true on login failure', () => {
        authServiceMock.register.mockReturnValue(
          throwError(() => new Error('Login failed'))
        );
        component.submit();
        expect(component.onError).toBeTruthy();
        expect(routerMock.navigate).not.toHaveBeenCalled();
      });

      it('should navigate to Login page on successful registration', () => {
        authServiceMock.register.mockReturnValue(of(undefined));
        component.submit();
        expect(routerMock.navigate).toBeCalledWith(['/login']);
      });
    });
  });
});
