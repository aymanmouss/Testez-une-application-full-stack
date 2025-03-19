# Application Full Stack

## Spécifications techniques

### Stack Technique
- **Front-end**: Angular
- **Back-end**: 
  - Java Spring Boot
  - MySQL

## Guide d'installation

### Cloner le projet
```bash
git clone https://github.com/aymanmouss/Testez-une-application-full-stack
```

### Lancer la partie Front
1. Se rendre dans le dossier front
```bash
cd front
```

2. Installer les dépendances
```bash
npm install
```

3. Lancer le serveur
```bash
npm run start
```

### Lancer la partie Back
1. Se rendre dans le dossier back
```bash
cd back
```

2. Installer les dépendances
```bash
mvn clean install
```

3. Lancer le script SQL
```bash
mysql -u root -p
CREATE DATABASE test;
```
Puis lancer le script `ressources/sql/script.sql`

4. Lancer le serveur
```bash
mvn spring-boot:run
```

## Tests

### Lancer les tests sur la partie front
1. Se rendre dans le dossier front
```bash
cd front
```

2. Installer les dépendances
```bash
npm install
```

3. Lancer les tests

**Technologies utilisées**: Jest & Cypress

#### Lancer les tests Jest
```bash
npm run test
npm run test:coverage
```

#### Lancer les tests Cypress
```bash
npm run e2e
npm run e2e:coverage
```

### Lancer les tests sur la partie back
1. Se rendre dans le dossier back
```bash
cd back
```

2. Installer les dépendances
```bash
mvn clean install
```

3. Lancer le script SQL
Lancer le script de données de test `ressources/sql/script.sql`

4. Lancer les tests

**Technologies utilisées**: JUnit, Mockito, Jacoco
```bash
mvn test
mvn jacoco:report
```
