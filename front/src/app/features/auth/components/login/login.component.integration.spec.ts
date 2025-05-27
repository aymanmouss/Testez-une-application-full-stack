import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { SessionService } from 'src/app/services/session.service';
import { LoginComponent } from './login.component';
import { describe, expect, it, beforeEach } from '@jest/globals';
import { AuthService } from '../../services/auth.service';

describe('LoginComponent Integration', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let httpMock: HttpTestingController;
  let sessionService: SessionService;
  let router: Router;

  beforeEach(async () => {
    const sessionServiceSpy = { logIn: jest.fn() };
    const routerSpy = { navigate: jest.fn() };

    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      imports: [HttpClientTestingModule, ReactiveFormsModule],
      providers: [
        AuthService,
        { provide: SessionService, useValue: sessionServiceSpy },
        { provide: Router, useValue: routerSpy },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
    sessionService = TestBed.inject(SessionService);
    router = TestBed.inject(Router);
  });

  it('should login successfully via HTTP', () => {
    component.form.patchValue({
      email: 'test@test.com',
      password: 'password123',
    });

    component.submit();

    const req = httpMock.expectOne('api/auth/login');
    expect(req.request.method).toEqual('POST');
    expect(req.request.body).toEqual({
      email: 'test@test.com',
      password: 'password123',
    });

    const mockResponse = {
      token: 'fake-jwt-token',
      type: 'Bearer',
      id: 1,
      username: 'testUser',
      firstName: 'Test',
      lastName: 'User',
      admin: false,
    };
    req.flush(mockResponse);

    expect(sessionService.logIn).toHaveBeenCalledWith(mockResponse);
    expect(router.navigate).toHaveBeenCalledWith(['/sessions']);
  });
});
