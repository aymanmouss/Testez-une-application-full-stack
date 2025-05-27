import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { RegisterComponent } from './register.component';
import { describe, expect, it, beforeEach } from '@jest/globals';
import { AuthService } from '../../services/auth.service';
import { RegisterRequest } from '../../interfaces/registerRequest.interface';

describe('RegisterComponent Integration', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let httpMock: HttpTestingController;
  let router: Router;
  const registerRequestMock = {
    email: 'test@test.com',
    firstName: 'John',
    lastName: 'Doe',
    password: 'password123',
  } as RegisterRequest;

  beforeEach(async () => {
    const routerSpy = { navigate: jest.fn() };

    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [HttpClientTestingModule, ReactiveFormsModule],
      providers: [AuthService, { provide: Router, useValue: routerSpy }],
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
    router = TestBed.inject(Router);
  });

  it('should register successfully via HTTP', () => {
    component.form.patchValue(registerRequestMock);

    component.submit();

    const req = httpMock.expectOne('api/auth/register');
    expect(req.request.method).toEqual('POST');
    expect(req.request.body).toEqual(registerRequestMock);

    req.flush({});

    expect(router.navigate).toHaveBeenCalledWith(['/login']);
  });

  it('should handle registration error via HTTP', () => {
    component.form.patchValue(registerRequestMock);

    component.submit();

    const req = httpMock.expectOne('api/auth/register');
    req.flush('Registration failed', {
      status: 400,
      statusText: 'Bad Request',
    });

    expect(component.onError).toEqual(true);
  });
});
