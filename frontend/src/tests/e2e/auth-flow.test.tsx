import { test, expect } from '@playwright/test';

test.describe('Authentication Flow', () => {
  test.beforeEach(async ({ page }) => {
    // Navegar a la página de login antes de cada test
    await page.goto('/login');
  });

  test('should display login form correctly', async ({ page }) => {
    // Verificar que el formulario de login se muestra correctamente
    await expect(page.getByLabel('Email')).toBeVisible();
    await expect(page.getByLabel('Contraseña')).toBeVisible();
    await expect(page.getByRole('button', { name: 'Iniciar Sesión' })).toBeVisible();
    await expect(page.getByText('¿No tienes cuenta?')).toBeVisible();
  });

  test('should show validation errors for empty form', async ({ page }) => {
    // Intentar enviar formulario vacío
    await page.getByRole('button', { name: 'Iniciar Sesión' }).click();

    // Verificar mensajes de error
    await expect(page.getByText('El email es requerido')).toBeVisible();
    await expect(page.getByText('La contraseña es requerida')).toBeVisible();
  });

  test('should show validation error for invalid email', async ({ page }) => {
    // Ingresar email inválido
    await page.getByLabel('Email').fill('invalid-email');
    await page.getByLabel('Contraseña').fill('password123');
    await page.getByRole('button', { name: 'Iniciar Sesión' }).click();

    // Verificar mensaje de error
    await expect(page.getByText('Email inválido')).toBeVisible();
  });

  test('should navigate to register page', async ({ page }) => {
    // Hacer clic en el enlace de registro
    await page.getByText('Registrarse').click();

    // Verificar que se navega a la página de registro
    await expect(page).toHaveURL('/register');
    await expect(page.getByText('Crear Cuenta')).toBeVisible();
  });

  test('should navigate to forgot password page', async ({ page }) => {
    // Hacer clic en el enlace de olvidé contraseña
    await page.getByText('¿Olvidaste tu contraseña?').click();

    // Verificar que se navega a la página de recuperación
    await expect(page).toHaveURL('/forgot-password');
    await expect(page.getByText('Recuperar Contraseña')).toBeVisible();
  });

  test('should toggle password visibility', async ({ page }) => {
    const passwordInput = page.getByLabel('Contraseña');
    const toggleButton = page.getByRole('button', { name: 'Toggle password visibility' });

    // Password should be hidden by default
    await expect(passwordInput).toHaveAttribute('type', 'password');

    // Click toggle button
    await toggleButton.click();

    // Password should be visible
    await expect(passwordInput).toHaveAttribute('type', 'text');

    // Click toggle button again
    await toggleButton.click();

    // Password should be hidden again
    await expect(passwordInput).toHaveAttribute('type', 'password');
  });

  test('should handle successful login', async ({ page }) => {
    // Mock de la respuesta de login exitoso
    await page.route('**/api/auth/login', async route => {
      await route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: JSON.stringify({
          user: {
            id: 1,
            email: 'test@example.com',
            name: 'Test User',
            role: 'CLIENTE'
          },
          token: 'mock-jwt-token'
        })
      });
    });

    // Ingresar credenciales válidas
    await page.getByLabel('Email').fill('test@example.com');
    await page.getByLabel('Contraseña').fill('password123');
    await page.getByRole('button', { name: 'Iniciar Sesión' }).click();

    // Verificar que se navega al dashboard
    await expect(page).toHaveURL('/dashboard');
    await expect(page.getByText('Bienvenido, Test User')).toBeVisible();
  });

  test('should handle login error', async ({ page }) => {
    // Mock de la respuesta de error
    await page.route('**/api/auth/login', async route => {
      await route.fulfill({
        status: 401,
        contentType: 'application/json',
        body: JSON.stringify({
          message: 'Credenciales inválidas'
        })
      });
    });

    // Ingresar credenciales inválidas
    await page.getByLabel('Email').fill('wrong@example.com');
    await page.getByLabel('Contraseña').fill('wrongpassword');
    await page.getByRole('button', { name: 'Iniciar Sesión' }).click();

    // Verificar mensaje de error
    await expect(page.getByText('Error al iniciar sesión')).toBeVisible();
  });

  test('should handle network error', async ({ page }) => {
    // Mock de error de red
    await page.route('**/api/auth/login', async route => {
      await route.abort();
    });

    // Ingresar credenciales
    await page.getByLabel('Email').fill('test@example.com');
    await page.getByLabel('Contraseña').fill('password123');
    await page.getByRole('button', { name: 'Iniciar Sesión' }).click();

    // Verificar mensaje de error de red
    await expect(page.getByText('Error de conexión')).toBeVisible();
  });

  test('should show loading state during login', async ({ page }) => {
    // Mock de respuesta lenta
    await page.route('**/api/auth/login', async route => {
      await new Promise(resolve => setTimeout(resolve, 2000));
      await route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: JSON.stringify({
          user: { id: 1, email: 'test@example.com', name: 'Test User', role: 'CLIENTE' },
          token: 'mock-jwt-token'
        })
      });
    });

    // Ingresar credenciales
    await page.getByLabel('Email').fill('test@example.com');
    await page.getByLabel('Contraseña').fill('password123');
    await page.getByRole('button', { name: 'Iniciar Sesión' }).click();

    // Verificar estado de carga
    await expect(page.getByText('Iniciando sesión...')).toBeVisible();
    await expect(page.getByRole('button', { name: 'Iniciando sesión...' })).toBeDisabled();
  });
});

test.describe('Registration Flow', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('/register');
  });

  test('should display registration form correctly', async ({ page }) => {
    await expect(page.getByLabel('Nombre')).toBeVisible();
    await expect(page.getByLabel('Email')).toBeVisible();
    await expect(page.getByLabel('Contraseña')).toBeVisible();
    await expect(page.getByLabel('Confirmar Contraseña')).toBeVisible();
    await expect(page.getByRole('button', { name: 'Registrarse' })).toBeVisible();
  });

  test('should show validation errors for empty form', async ({ page }) => {
    await page.getByRole('button', { name: 'Registrarse' }).click();

    await expect(page.getByText('El nombre es requerido')).toBeVisible();
    await expect(page.getByText('El email es requerido')).toBeVisible();
    await expect(page.getByText('La contraseña es requerida')).toBeVisible();
  });

  test('should show error for password mismatch', async ({ page }) => {
    await page.getByLabel('Nombre').fill('Test User');
    await page.getByLabel('Email').fill('test@example.com');
    await page.getByLabel('Contraseña').fill('password123');
    await page.getByLabel('Confirmar Contraseña').fill('differentpassword');
    await page.getByRole('button', { name: 'Registrarse' }).click();

    await expect(page.getByText('Las contraseñas no coinciden')).toBeVisible();
  });

  test('should handle successful registration', async ({ page }) => {
    // Mock de respuesta exitosa
    await page.route('**/api/auth/register', async route => {
      await route.fulfill({
        status: 201,
        contentType: 'application/json',
        body: JSON.stringify({
          id: 1,
          email: 'test@example.com',
          name: 'Test User',
          role: 'CLIENTE'
        })
      });
    });

    await page.getByLabel('Nombre').fill('Test User');
    await page.getByLabel('Email').fill('test@example.com');
    await page.getByLabel('Contraseña').fill('password123');
    await page.getByLabel('Confirmar Contraseña').fill('password123');
    await page.getByRole('button', { name: 'Registrarse' }).click();

    // Verificar que se navega al dashboard
    await expect(page).toHaveURL('/dashboard');
  });
});

test.describe('Dashboard Access', () => {
  test('should redirect to login if not authenticated', async ({ page }) => {
    // Intentar acceder al dashboard sin autenticación
    await page.goto('/dashboard');

    // Verificar que se redirige al login
    await expect(page).toHaveURL('/login');
  });

  test('should show dashboard for authenticated user', async ({ page }) => {
    // Mock de usuario autenticado
    await page.addInitScript(() => {
      localStorage.setItem('token', 'mock-jwt-token');
      localStorage.setItem('user', JSON.stringify({
        id: 1,
        email: 'test@example.com',
        name: 'Test User',
        role: 'CLIENTE'
      }));
    });

    await page.goto('/dashboard');

    // Verificar que se muestra el dashboard
    await expect(page.getByText('Dashboard')).toBeVisible();
    await expect(page.getByText('Bienvenido, Test User')).toBeVisible();
  });
}); 