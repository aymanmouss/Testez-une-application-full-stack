describe('Not Found Page', () => {
  it('should redirect to 404 page for undefined routes', () => {
    cy.visit('/undefined-route');
    cy.url().should('include', '/404');
    cy.get('h1').should('contain', 'Page not found !');
  });
});
