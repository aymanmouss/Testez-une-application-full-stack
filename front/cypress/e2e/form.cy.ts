describe('Foem session updater', () => {
  it('Login successfull ', () => {
    cy.login('yoga@studio.com', 'test!1234', true);
  });
  it('Create session form', () => {
    cy.intercept('GET', '/api/teacher', {
      body: [
        {
          id: 1,
          lastName: 'Hélène',
          firstName: 'THIERCELIN',
          createdAt: new Date(),
          updatedAt: new Date(),
        },
      ],
    });
    cy.contains('button', 'Create').click();
    cy.url().should('include', 'sessions/create');

    cy.get('input[formControlName=name]').type('New Session');
    cy.get('input[formControlName=date]').type('2025-02-20');
    cy.get('mat-select[formControlName=teacher_id]').click();
    cy.get('.mat-option-text').contains('THIERCELIN Hélène').click();
    cy.get('textarea[formControlName=description]').type(
      'Join us for a yoga session'
    );
    cy.get('button[color="primary"').should('exist').and('not.be.disabled');

    cy.intercept(
      {
        method: 'POST',
        url: '/api/session',
      },
      {
        id: 1,
        name: 'Yoga session',
        date: new Date(),
        teacher_id: 2,
        description: 'Join us for a yoga session',
        users: [],
        createdAt: new Date(),
        updatedAt: new Date(),
      }
    ).as('session');

    cy.get('button[color="primary"').click();
  });
});
