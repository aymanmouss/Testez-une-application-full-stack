import { defineConfig } from 'cypress';

export default defineConfig({
  projectId: 'rn2ioc',
  videosFolder: 'cypress/videos',
  screenshotsFolder: 'cypress/screenshots',
  fixturesFolder: 'cypress/fixtures',
  video: false,
  e2e: {
    setupNodeEvents(on, config) {
      require('@cypress/code-coverage/task')(on, config);
      // if you have other plugins, add them here
      return config;
    },
    baseUrl: 'http://localhost:4200',
    supportFile: 'cypress/support/e2e.ts',
  },
});
