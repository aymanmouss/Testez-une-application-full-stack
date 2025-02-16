describe('User session spec', () => {
  it('Login successfull ', () => {
    cy.login('yoga@studio.com', 'test!1234', false);
  });

  it('detail', () => {
    cy.intercept(
      {
        method: 'GET',
        url: '/api/session/1',
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

    cy.intercept('GET', '/api/teacher/2', {
      body: {
        id: 1,
        lastName: 'Hélène',
        firstName: 'THIERCELIN',
        createdAt: new Date(),
        updatedAt: new Date(),
      },
    });
    cy.contains('button', 'Detail').click();
    cy.url().should('include', 'detail/1');

    cy.get('span.ml1').should('contain', 'THIERCELIN HÉLÈNE');
    cy.get('span.ml1').should('contain', '0 attendees');
    cy.get('.description').should('contain', 'Join us for a yoga session');
    cy.get('button[color="primary"]').should('exist');
  });

  it('Participate to a session', () => {
    const sessionUsers: number[] = [];
    cy.intercept(
      {
        method: 'GET',
        url: '/api/session/1',
      },
      {
        id: 1,
        name: 'Yoga session',
        date: new Date(),
        teacher_id: 2,
        description: 'Join us for a yoga session',
        users: sessionUsers,
        createdAt: new Date(),
        updatedAt: new Date(),
      }
    ).as('session');
    cy.intercept('GET', '/api/teacher/2', {
      body: {
        id: 1,
        lastName: 'Hélène',
        firstName: 'THIERCELIN',
        createdAt: new Date(),
        updatedAt: new Date(),
      },
    });
    cy.intercept('POST', '/api/session/1/participate/1', {
      status: 200,
    });
    sessionUsers.push(1);
    cy.get('button[color="primary"]').click();
    cy.get('button[color="warn"]').should('exist');
    cy.get('span.ml1').should('contain', '1 attendees');
  });

  it('Withdraw participation from a session', () => {
    let sessionUsers: number[] = [];

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session/1',
      },
      {
        id: 1,
        name: 'Yoga session',
        date: new Date(),
        teacher_id: 2,
        description: 'Join us for a yoga session',
        users: sessionUsers,
        createdAt: new Date(),
        updatedAt: new Date(),
      }
    ).as('session');
    cy.intercept('GET', '/api/teacher/2', {
      body: {
        id: 1,
        lastName: 'Hélène',
        firstName: 'THIERCELIN',
        createdAt: new Date(),
        updatedAt: new Date(),
      },
    });
    cy.intercept('DELETE', '/api/session/1/participate/1', {
      status: 200,
    });
    cy.get('button span').contains('Do not participate').click();
    sessionUsers = [];

    cy.get('button[color="primary"]').should('exist');
    cy.get('span.ml1').should('contain', '0 attendees');
  });
});
