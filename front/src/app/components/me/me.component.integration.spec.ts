import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { SessionService } from 'src/app/services/session.service';
import { UserService } from 'src/app/services/user.service';
import { MeComponent } from './me.component';
import { describe, expect, it, beforeEach } from '@jest/globals';

describe('MeComponent Integration', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let httpMock: HttpTestingController;
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
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [HttpClientTestingModule, MatSnackBarModule],
      providers: [
        UserService,
        {
          provide: SessionService,
          useValue: { sessionInformation: { id: 1 }, logOut: jest.fn() },
        },
        { provide: Router, useValue: { navigate: jest.fn() } },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
  });

  it('should load user via HTTP', () => {
    fixture.detectChanges();

    const req = httpMock.expectOne('api/user/1');
    req.flush(userMock);

    expect(component.user).toEqual(userMock);
  });

  it('should delete user via HTTP', () => {
    fixture.detectChanges();
    component.delete();
    const req = httpMock.expectOne('api/user/1');
    req.flush({});
    expect(component.user).toEqual({});
  });
});
