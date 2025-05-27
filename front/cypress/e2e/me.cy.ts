describe('User details /me ', () => {
  it('me', () => {
    cy.login('yoga@studio.com', 'test!1234', true);

    cy.intercept(
      {
        method: 'GET',
        url: '/api/user/1',
      },
      {
        id: 1,
        firstName: 'John',
        lastName: 'Doe',
        email: 'yoga@studio.com',
        admin: false,
        createdAt: new Date(),
        updatedAt: new Date(),
      }
    ).as('user');

    cy.get('span[routerLink=me]').click();
    cy.url().should('include', '/me');

    cy.get('mat-card').within(() => {
      cy.get('mat-card-title h1').should('contain', 'User information');

      cy.get('mat-card-content').within(() => {
        cy.get('p').should('contain', 'Name: John DOE');
        cy.get('p').should('contain', 'Email: yoga@studio.com');
        cy.get('p').contains('You are admin').should('not.exist');
        cy.get('button[color="warn"]').should('exist');
      });
    });

    cy.get('button mat-icon').contains('arrow_back').click();
    cy.url().should('include', '/sessions');
  });
});
