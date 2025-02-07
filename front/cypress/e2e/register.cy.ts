/// <reference types="cypress" />

describe('Register Component', () => {
  beforeEach(() => {
    // Visit the register page before each test
    cy.visit('/register');
  });

  it('should display register form', () => {
    cy.get('button[type="submit"]').should('be.disabled');
  });

  it('should show validation errors for invalid inputs', () => {
    // Test invalid email
    cy.get('input[formControlName="email"]').type('invalid-email');
    cy.get('input[formControlName="email"]').blur();
    cy.get('mat-form-field').should('have.class', 'mat-form-field-invalid');

    // Test short first name (less than 3 characters)
    cy.get('input[formControlName="firstName"]').type('Jo');
    cy.get('input[formControlName="firstName"]').blur();
    cy.get('mat-form-field').should('have.class', 'mat-form-field-invalid');
  });

  it('should handle successful registration', () => {
    // Intercept the registration API call
    cy.intercept('POST', '**/register', {
      statusCode: 200,
      body: {},
    }).as('registerRequest');

    // Fill in valid form data
    cy.get('input[formControlName="firstName"]').type('John');
    cy.get('input[formControlName="lastName"]').type('Doe');
    cy.get('input[formControlName="email"]').type('john.doe@example.com');
    cy.get('input[formControlName="password"]').type('password123');

    // Submit the form
    cy.get('button[type="submit"]').click();

    // Wait for the API call and verify redirect
    cy.wait('@registerRequest');
    cy.url().should('include', '/login');
  });

  it('should handle registration error', () => {
    // Intercept the registration API call with error
    cy.intercept('POST', '**/register', {
      statusCode: 400,
      body: { error: 'Registration failed' },
    }).as('registerRequest');

    // Fill in form data
    cy.get('input[formControlName="firstName"]').type('John');
    cy.get('input[formControlName="lastName"]').type('Doe');
    cy.get('input[formControlName="email"]').type('john.doe@example.com');
    cy.get('input[formControlName="password"]').type('password123');

    // Submit the form
    cy.get('button[type="submit"]').click();

    // Wait for the API call and verify error message
    cy.wait('@registerRequest');
    cy.get('.error').should('be.visible');
    cy.get('.error').should('contain', 'An error occurred');
  });
});
