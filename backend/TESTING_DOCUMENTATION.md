# Documentación de Testing - Sistema de Gestión Legal

## 📋 Índice
1. [Configuración del Entorno de Testing](#configuración-del-entorno-de-testing)
2. [Estructura de Tests](#estructura-de-tests)
3. [Tipos de Tests](#tipos-de-tests)
4. [Cómo Funcionan los Tests](#cómo-funcionan-los-tests)
5. [Patrones de Testing](#patrones-de-testing)
6. [Cómo Crear Nuevos Tests](#cómo-crear-nuevos-tests)
7. [Ejemplos Prácticos](#ejemplos-prácticos)
8. [Comandos de Testing](#comandos-de-testing)
9. [Cobertura de Tests](#cobertura-de-tests)
10. [Mejores Prácticas](#mejores-prácticas)

---

## 🛠 Configuración del Entorno de Testing

### Dependencias Instaladas
```json
{
  "devDependencies": {
    "@nestjs/testing": "^10.0.0",
    "@types/jest": "^29.5.2",
    "jest": "^29.5.0",
    "ts-jest": "^29.1.0",
    "supertest": "^6.3.3"
  }
}
```

### Configuración de Jest (package.json)
```json
{
  "jest": {
    "moduleFileExtensions": ["js", "json", "ts"],
    "rootDir": "src",
    "testRegex": ".*\\.spec\\.ts$",
    "transform": {
      "^.+\\.(t|j)s$": "ts-jest"
    },
    "collectCoverageFrom": ["**/*.(t|j)s"],
    "coverageDirectory": "../coverage",
    "testEnvironment": "node"
  }
}
```

---

## 📁 Estructura de Tests

### Convención de Nomenclatura
- **Archivos de test**: `*.spec.ts`
- **Ubicación**: Mismo directorio que el archivo a testear
- **Ejemplo**: `src/auth/auth.service.ts` → `src/auth/auth.service.spec.ts`

### Estructura de Directorios
```
src/
├── auth/
│   ├── auth.service.ts
│   ├── auth.service.spec.ts      ← Test del servicio
│   ├── auth.controller.ts
│   └── auth.controller.spec.ts   ← Test del controlador
├── cases/
│   ├── cases.service.ts
│   ├── cases.service.spec.ts
│   ├── cases.controller.ts
│   └── cases.controller.spec.ts
└── ...
```

---

## 🧪 Tipos de Tests

### 1. Tests Unitarios (Unit Tests)
- **Propósito**: Probar funciones/métodos individuales
- **Alcance**: Una sola clase o función
- **Dependencias**: Mockeadas
- **Ejemplo**: `auth.service.spec.ts`

### 2. Tests de Controladores (Controller Tests)
- **Propósito**: Probar endpoints HTTP
- **Alcance**: Controlador completo
- **Dependencias**: Servicios mockeados
- **Ejemplo**: `cases.controller.spec.ts`

### 3. Tests de Integración (Integration Tests)
- **Propósito**: Probar interacción entre módulos
- **Alcance**: Múltiples servicios
- **Dependencias**: Base de datos de prueba
- **Archivo**: `test/` directory

### 4. Tests E2E (End-to-End Tests)
- **Propósito**: Probar flujos completos
- **Alcance**: Aplicación completa
- **Dependencias**: Base de datos real
- **Archivo**: `test/` directory

---

## ⚙️ Cómo Funcionan los Tests

### 1. Framework de Testing: Jest + NestJS Testing
```typescript
import { Test, TestingModule } from '@nestjs/testing';

describe('MiServicio', () => {
  let service: MiServicio;
  let mockDependency: Partial<DependencyService>;

  beforeEach(async () => {
    // Configuración del módulo de testing
    const module: TestingModule = await Test.createTestingModule({
      providers: [
        MiServicio,
        { provide: DependencyService, useValue: mockDependency }
      ],
    }).compile();

    service = module.get<MiServicio>(MiServicio);
  });

  it('should do something', () => {
    // Test aquí
  });
});
```

### 2. Ciclo de Vida de los Tests
1. **beforeEach**: Se ejecuta antes de cada test
2. **Test Case**: Ejecuta la lógica del test
3. **Assertions**: Verifica los resultados
4. **Cleanup**: Limpieza automática

### 3. Inyección de Dependencias en Tests
```typescript
// Mock de dependencias
const mockPrismaService = {
  user: {
    findUnique: jest.fn().mockResolvedValue(mockUser),
    create: jest.fn(),
    update: jest.fn(),
  } as any,
};

// Inyección en el módulo de testing
const module: TestingModule = await Test.createTestingModule({
  providers: [
    AuthService,
    { provide: PrismaService, useValue: mockPrismaService }
  ],
}).compile();
```

---

## 🎯 Patrones de Testing

### 1. Patrón AAA (Arrange, Act, Assert)
```typescript
it('should validate user credentials', async () => {
  // Arrange (Preparar)
  const email = 'test@example.com';
  const password = 'password123';
  mockPrismaService.user.findUnique.mockResolvedValue(mockUser);

  // Act (Actuar)
  const result = await service.validateUser(email, password);

  // Assert (Verificar)
  expect(result).toBeDefined();
  expect(result.email).toBe(email);
});
```

### 2. Patrón Given-When-Then
```typescript
it('should return user and token on successful login', async () => {
  // Given (Dado que)
  const loginDto = { email: 'test@example.com', password: 'password' };
  mockJwtService.sign.mockReturnValue('fake-jwt-token');

  // When (Cuando)
  const response = await service.login(loginDto);

  // Then (Entonces)
  expect(response).toEqual({
    user: { id: 1, email: 'test@example.com', name: 'Test User', role: 'ADMIN' },
    token: 'fake-jwt-token',
  });
});
```

### 3. Patrón de Mocking Completo
```typescript
// Mock completo de PrismaService
mockPrismaService = {
  user: {
    findUnique: jest.fn(),
    findFirst: jest.fn(),
    create: jest.fn(),
    update: jest.fn(),
    delete: jest.fn(),
    findMany: jest.fn(),
    // ... todos los métodos requeridos
  } as any,
};
```

---

## 🚀 Cómo Crear Nuevos Tests

### Paso 1: Identificar qué testear
```bash
# Ejemplo: Crear test para UsersService
touch src/users/users.service.spec.ts
```

### Paso 2: Estructura básica del test
```typescript
import { Test, TestingModule } from '@nestjs/testing';
import { UsersService } from './users.service';
import { PrismaService } from '../prisma/prisma.service';

describe('UsersService', () => {
  let service: UsersService;
  let mockPrismaService: Partial<PrismaService>;

  beforeEach(async () => {
    // Configurar mocks
    mockPrismaService = {
      user: {
        findUnique: jest.fn(),
        create: jest.fn(),
        update: jest.fn(),
      } as any,
    };

    // Crear módulo de testing
    const module: TestingModule = await Test.createTestingModule({
      providers: [
        UsersService,
        { provide: PrismaService, useValue: mockPrismaService }
      ],
    }).compile();

    service = module.get<UsersService>(UsersService);
  });

  it('should be defined', () => {
    expect(service).toBeDefined();
  });

  // Tests específicos aquí...
});
```

### Paso 3: Escribir casos de test
```typescript
describe('findByEmail', () => {
  it('should return user when email exists', async () => {
    // Arrange
    const email = 'test@example.com';
    const mockUser = { id: 1, email, name: 'Test User' };
    mockPrismaService.user.findUnique.mockResolvedValue(mockUser);

    // Act
    const result = await service.findByEmail(email);

    // Assert
    expect(result).toEqual(mockUser);
    expect(mockPrismaService.user.findUnique).toHaveBeenCalledWith({
      where: { email }
    });
  });

  it('should return null when email does not exist', async () => {
    // Arrange
    const email = 'nonexistent@example.com';
    mockPrismaService.user.findUnique.mockResolvedValue(null);

    // Act
    const result = await service.findByEmail(email);

    // Assert
    expect(result).toBeNull();
  });
});
```

---

## 📝 Ejemplos Prácticos

### Ejemplo 1: Test de Servicio (AuthService)
```typescript
// src/auth/auth.service.spec.ts
describe('AuthService', () => {
  let service: AuthService;
  let mockPrismaService: Partial<PrismaService>;
  let mockJwtService: Partial<JwtService>;

  beforeEach(async () => {
    mockPrismaService = {
      user: {
        findUnique: jest.fn(),
        update: jest.fn(),
      } as any,
    };

    mockJwtService = {
      sign: jest.fn().mockReturnValue('fake-jwt-token'),
    };

    const module: TestingModule = await Test.createTestingModule({
      providers: [
        AuthService,
        { provide: PrismaService, useValue: mockPrismaService },
        { provide: JwtService, useValue: mockJwtService },
      ],
    }).compile();

    service = module.get<AuthService>(AuthService);
  });

  it('should validate user with correct credentials', async () => {
    const mockUser = {
      id: 1,
      email: 'test@example.com',
      password: await bcrypt.hash('password', 10),
      role: 'ADMIN',
    };

    mockPrismaService.user.findUnique.mockResolvedValue(mockUser);

    const result = await service.validateUser('test@example.com', 'password');
    
    expect(result).toBeDefined();
    expect(result.email).toBe('test@example.com');
  });
});
```

### Ejemplo 2: Test de Controlador (CasesController)
```typescript
// src/cases/cases.controller.spec.ts
describe('CasesController', () => {
  let controller: CasesController;
  let service: Partial<CasesService>;

  beforeEach(async () => {
    service = {
      findAll: jest.fn().mockResolvedValue([{ id: '1', title: 'Test Case' }]),
      create: jest.fn(),
      findOne: jest.fn(),
    };

    const module: TestingModule = await Test.createTestingModule({
      controllers: [CasesController],
      providers: [{ provide: CasesService, useValue: service }],
    }).compile();

    controller = module.get<CasesController>(CasesController);
  });

  it('should return an array of cases', async () => {
    const mockRequest = {
      user: { id: '1', role: 'ADMIN' }
    };

    const result = await controller.findAll(mockRequest);
    
    expect(result).toEqual([{ id: '1', title: 'Test Case' }]);
    expect(service.findAll).toHaveBeenCalledWith('1', 'ADMIN');
  });
});
```

### Ejemplo 3: Test de Casos de Error
```typescript
it('should throw UnauthorizedException for invalid credentials', async () => {
  // Arrange
  const invalidLoginDto = { email: 'test@example.com', password: 'wrongpass' };
  mockPrismaService.user.findUnique.mockResolvedValue(null);

  // Act & Assert
  await expect(service.login(invalidLoginDto))
    .rejects.toThrow('Credenciales inválidas');
});

it('should handle database errors gracefully', async () => {
  // Arrange
  mockPrismaService.user.findUnique.mockRejectedValue(new Error('DB Error'));

  // Act & Assert
  await expect(service.validateUser('test@example.com', 'password'))
    .rejects.toThrow('DB Error');
});
```

---

## 🎮 Comandos de Testing

### Comandos Básicos
```bash
# Ejecutar todos los tests
npm test

# Ejecutar tests en modo watch (desarrollo)
npm run test:watch

# Ejecutar tests con cobertura
npm run test:cov

# Ejecutar tests específicos
npm test -- --testNamePattern="AuthService"

# Ejecutar tests de un archivo específico
npm test -- auth.service.spec.ts

# Ejecutar tests con verbose output
npm test -- --verbose
```

### Comandos Avanzados
```bash
# Ejecutar tests con detección de memory leaks
npm test -- --detectOpenHandles

# Ejecutar tests con timeout personalizado
npm test -- --testTimeout=10000

# Ejecutar tests con coverage específico
npm test -- --coverage --collectCoverageFrom="src/auth/**/*.ts"

# Ejecutar tests en paralelo
npm test -- --maxWorkers=4
```

---

## 📊 Cobertura de Tests

### Configuración de Cobertura
```json
{
  "jest": {
    "collectCoverageFrom": [
      "**/*.(t|j)s",
      "!**/*.spec.ts",
      "!**/node_modules/**"
    ],
    "coverageDirectory": "../coverage",
    "coverageReporters": ["text", "lcov", "html"]
  }
}
```

### Interpretación de Cobertura
- **Statements**: Porcentaje de líneas de código ejecutadas
- **Branches**: Porcentaje de ramas condicionales ejecutadas
- **Functions**: Porcentaje de funciones llamadas
- **Lines**: Porcentaje de líneas ejecutadas

### Metas de Cobertura Recomendadas
- **Mínimo**: 70% de statements
- **Objetivo**: 80% de statements
- **Excelente**: 90%+ de statements

---

## ✅ Mejores Prácticas

### 1. Nomenclatura Clara
```typescript
// ✅ Bueno
it('should return user when email exists', async () => {});
it('should throw error when user not found', async () => {});

// ❌ Malo
it('should work', async () => {});
it('test 1', async () => {});
```

### 2. Tests Aislados
```typescript
// ✅ Cada test es independiente
beforeEach(async () => {
  // Reset mocks antes de cada test
  jest.clearAllMocks();
});

// ❌ Tests que dependen de otros
it('should create user', async () => {
  // Test que crea usuario
});

it('should update user', async () => {
  // Test que depende del usuario creado en el test anterior
});
```

### 3. Mocks Específicos
```typescript
// ✅ Mock específico para el test
it('should handle user not found', async () => {
  mockPrismaService.user.findUnique.mockResolvedValue(null);
  // Test específico
});

// ❌ Mock genérico que puede cambiar
beforeEach(async () => {
  mockPrismaService.user.findUnique.mockResolvedValue(anyUser);
});
```

### 4. Assertions Claras
```typescript
// ✅ Assertions específicos
expect(result).toEqual(expectedUser);
expect(mockService.method).toHaveBeenCalledWith(expectedArgs);
expect(mockService.method).toHaveBeenCalledTimes(1);

// ❌ Assertions vagos
expect(result).toBeTruthy();
expect(mockService.method).toHaveBeenCalled();
```

### 5. Manejo de Errores
```typescript
// ✅ Test de errores específicos
it('should throw UnauthorizedException for invalid credentials', async () => {
  await expect(service.login(invalidDto))
    .rejects.toThrow('Credenciales inválidas');
});

// ❌ Test de errores genéricos
it('should handle errors', async () => {
  await expect(service.login(invalidDto)).rejects.toThrow();
});
```

### 6. Organización de Tests
```typescript
describe('AuthService', () => {
  describe('validateUser', () => {
    it('should return user for valid credentials', async () => {});
    it('should return null for invalid credentials', async () => {});
  });

  describe('login', () => {
    it('should return user and token', async () => {});
    it('should throw for invalid credentials', async () => {});
  });
});
```

---

## 🔧 Troubleshooting

### Problemas Comunes

#### 1. Error: "Expected 1 arguments, but got 0"
```typescript
// ❌ Problema
const result = await controller.findAll();

// ✅ Solución
const mockRequest = { user: { id: '1', role: 'ADMIN' } };
const result = await controller.findAll(mockRequest);
```

#### 2. Error: "Type is missing properties"
```typescript
// ❌ Problema
const mockUser = { id: 1, email: 'test@example.com' };

// ✅ Solución
const mockUser = { 
  id: 1, 
  email: 'test@example.com', 
  name: 'Test User',
  role: 'ADMIN',
  password: 'hashedPassword'
};
```

#### 3. Error: "Cannot read property of undefined"
```typescript
// ❌ Problema
mockService.method.mockResolvedValue(result);

// ✅ Solución
(mockService.method as jest.Mock).mockResolvedValue(result);
```

#### 4. Error: "Timeout of 5000ms exceeded"
```typescript
// ✅ Solución: Aumentar timeout
it('should handle slow operation', async () => {
  // Test con operación lenta
}, 10000); // 10 segundos
```

---

## 📚 Recursos Adicionales

### Documentación Oficial
- [NestJS Testing](https://docs.nestjs.com/fundamentals/testing)
- [Jest Documentation](https://jestjs.io/docs/getting-started)
- [TypeScript Testing](https://www.typescriptlang.org/docs/handbook/testing.html)

### Herramientas Útiles
- **Jest CLI**: Comandos avanzados de testing
- **Coverage Reports**: Análisis de cobertura
- **Test Watchers**: Desarrollo con tests en tiempo real

### Extensiones de VS Code
- **Jest**: Integración con Jest
- **Jest Runner**: Ejecutar tests individuales
- **Coverage Gutters**: Ver cobertura en el editor

---

## 🎯 Conclusión

Los tests son fundamentales para mantener la calidad del código y asegurar que las funcionalidades trabajen correctamente. Esta documentación proporciona una guía completa para:

1. **Entender** cómo funcionan los tests en el proyecto
2. **Crear** nuevos tests siguiendo las mejores prácticas
3. **Mantener** tests existentes de manera eficiente
4. **Debuggear** problemas comunes en testing

Recuerda: **"Los tests son documentación ejecutable"** - escribirlos bien hace que el código sea más mantenible y confiable. 