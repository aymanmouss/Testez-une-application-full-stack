import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from '../../../../services/session.service';
import { DetailComponent } from './detail.component';
import { SessionApiService } from '../../services/session-api.service';
import { TeacherService } from 'src/app/services/teacher.service';
import { ActivatedRoute, Router } from '@angular/router';
import { of } from 'rxjs';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { Session } from '../../interfaces/session.interface';
import { Teacher } from 'src/app/interfaces/teacher.interface';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';

describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;
  let service: SessionService;
  let mockSessionApiService: jest.Mocked<SessionApiService>;
  let mockTeacherService: jest.Mocked<TeacherService>;
  let mockMatSnackBar: jest.Mocked<MatSnackBar>;
  let mockRouter: jest.Mocked<Router>;
  let activatedRouteMock: jest.Mocked<ActivatedRoute>;
  const mockSessionService = {
    sessionInformation: {
      token: 'fake-jwt-token',
      type: 'Bearer',
      id: 1,
      username: 'testUser',
      firstName: 'Test',
      lastName: 'User',
      admin: true,
    } as SessionInformation,
  };
  const mockSession = {
    id: 1,
    name: 'Test Session',
    description: 'Test Session',
    date: new Date(),
    teacher_id: 1,
    users: [1, 2, 4],
    createdAt: new Date(),
    updatedAt: new Date(),
  } as Session;

  const mockTeacher = {
    id: 1,
    lastName: 'Teacher',
    firstName: 'Test',
    createdAt: new Date(),
    updatedAt: new Date(),
  } as Teacher;

  beforeEach(async () => {
    mockSessionApiService = {
      detail: jest.fn().mockReturnValue(of(mockSession)),
      delete: jest.fn().mockReturnValue(of({})),
      participate: jest.fn().mockReturnValue(of({})),
      unParticipate: jest.fn().mockReturnValue(of({})),
    } as any;

    mockTeacherService = {
      detail: jest.fn().mockReturnValue(of(mockTeacher)),
    } as any;

    mockMatSnackBar = {
      open: jest.fn(),
    } as any;

    mockRouter = {
      navigate: jest.fn(),
    } as any;
    activatedRouteMock = {
      snapshot: { paramMap: { get: jest.fn().mockReturnValue('123') } },
    } as any;

    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule,
        MatCardModule,
        MatIconModule,
      ],
      declarations: [DetailComponent],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: ActivatedRoute, useValue: activatedRouteMock },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: TeacherService, useValue: mockTeacherService },
        { provide: MatSnackBar, useValue: mockMatSnackBar },
        { provide: Router, useValue: mockRouter },
      ],
    }).compileComponents();
    service = TestBed.inject(SessionService);
    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch session on init', () => {
    expect(mockSessionApiService.detail).toBeCalledWith('123');
    expect(mockTeacherService.detail).toBeCalledWith('1');
  });

  it('should call delete and show snackbar message', () => {
    component.delete();
    expect(mockSessionApiService.delete).toBeCalledWith('123');
    expect(mockMatSnackBar.open).toBeCalledWith('Session deleted !', 'Close', {
      duration: 3000,
    });
    expect(mockRouter.navigate).toBeCalledWith(['sessions']);
  });

  it('should call participate and fetch session', () => {
    component.participate();
    expect(mockSessionApiService.participate).toBeCalledWith('123', '1');
  });

  it('should call unParticipate and fetch session', () => {
    component.unParticipate();
    expect(mockSessionApiService.unParticipate).toBeCalledWith('123', '1');
  });

  it('should handle back navigation', () => {
    const mockBack = jest.fn();
    window.history.back = mockBack;
    component.back();
    expect(mockBack).toHaveBeenCalled();
  });
});
