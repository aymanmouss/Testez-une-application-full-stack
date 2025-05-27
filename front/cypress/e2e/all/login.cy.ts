describe('Login spec', () => {
  it('Login successfull', () => {
    cy.login('yoga@studio.com', 'test!1234', true);
  });
});
