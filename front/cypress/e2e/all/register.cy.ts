/// <reference types="cypress" />

describe('Register Component', () => {
  beforeEach(() => {
    cy.visit('/register');
  });

  it('should display register form', () => {
    cy.get('button[type="submit"]').should('be.disabled');
  });

  it('should show validation errors for invalid inputs', () => {
    cy.get('input[formControlName="email"]').type('invalid-email');
    cy.get('input[formControlName="email"]').blur();
    cy.get('mat-form-field').should('have.class', 'mat-form-field-invalid');

    cy.get('input[formControlName="firstName"]').type('Jo');
    cy.get('input[formControlName="firstName"]').blur();
    cy.get('mat-form-field').should('have.class', 'mat-form-field-invalid');
  });

  it('should handle successful registration', () => {
    cy.intercept('POST', '**/register', {
      statusCode: 200,
      body: {},
    }).as('registerRequest');

    cy.get('input[formControlName="firstName"]').type('John');
    cy.get('input[formControlName="lastName"]').type('Doe');
    cy.get('input[formControlName="email"]').type('john.doe@example.com');
    cy.get('input[formControlName="password"]').type('password123');

    cy.get('button[type="submit"]').click();

    cy.wait('@registerRequest');
    cy.url().should('include', '/login');
  });

  it('should handle registration error', () => {
    cy.intercept('POST', '**/register', {
      statusCode: 400,
      body: { error: 'Registration failed' },
    }).as('registerRequest');

    cy.get('input[formControlName="firstName"]').type('John');
    cy.get('input[formControlName="lastName"]').type('Doe');
    cy.get('input[formControlName="email"]').type('john.doe@example.com');
    cy.get('input[formControlName="password"]').type('password123');

    cy.get('button[type="submit"]').click();

    cy.wait('@registerRequest');
    cy.get('.error').should('be.visible');
    cy.get('.error').should('contain', 'An error occurred');
  });
});
