import { NestFactory } from '@nestjs/core';
import { ValidationPipe } from '@nestjs/common';
import { SwaggerModule, DocumentBuilder } from '@nestjs/swagger';
import { AppModule } from './app.module';
import * as express from 'express';
import * as path from 'path';
import helmet from 'helmet';
import rateLimit from 'express-rate-limit';
import * as compression from 'compression';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);
  
  // Configuración de CORS - debe ir ANTES de otros middleware
  app.enableCors({
    origin: ['http://localhost:5173', 'http://localhost:3000'],
    credentials: true,
    methods: ['GET', 'POST', 'PUT', 'DELETE', 'PATCH', 'OPTIONS'],
    allowedHeaders: ['Content-Type', 'Authorization', 'X-Requested-With'],
  });
  
  // Configuración global de pipes
  app.useGlobalPipes(new ValidationPipe({
    whitelist: true,
    transform: true,
    forbidNonWhitelisted: true,
  }));

  // Security Headers - configuración más permisiva para desarrollo
  app.use(helmet({
    contentSecurityPolicy: {
      directives: {
        defaultSrc: ["'self'"],
        styleSrc: ["'self'", "'unsafe-inline'"],
        scriptSrc: ["'self'", "'unsafe-inline'"],
        imgSrc: ["'self'", "data:", "https:"],
        connectSrc: ["'self'", "http://localhost:5173", "http://localhost:3000"],
        fontSrc: ["'self'"],
        objectSrc: ["'none'"],
        mediaSrc: ["'self'"],
        frameSrc: ["'none'"],
      },
    },
    crossOriginEmbedderPolicy: false,
    crossOriginResourcePolicy: { policy: "cross-origin" },
  }));

  // Rate Limiting
  const limiter = rateLimit({
    windowMs: 1 * 60 * 1000, // 1 minuto
    max: 10000, // hasta 10000 peticiones por IP cada minuto
    message: 'Too many requests from this IP, please try again later.',
    standardHeaders: true,
    legacyHeaders: false,
  });
  app.use('/api/', limiter);

  // Stricter rate limiting for auth endpoints (desarrollo: mucho más permisivo)
  const authLimiter = rateLimit({
    windowMs: 1 * 60 * 1000, // 1 minuto
    max: 10000, // hasta 10000 intentos de login por IP cada minuto
    message: 'Too many authentication attempts, please try again later.',
    standardHeaders: true,
    legacyHeaders: false,
  });
  app.use('/api/auth/', authLimiter);

  // Compression
  app.use(compression());

  // Prefijo global para la API
  app.setGlobalPrefix('api');

  // Configuración de Swagger
  const config = new DocumentBuilder()
    .setTitle('Sistema de Gestión Legal API')
    .setDescription(`
      API completa para el sistema de gestión de despacho de abogados.
      
      ## Funcionalidades Principales:
      
      ### 🔐 Autenticación y Usuarios
      - Registro e inicio de sesión de usuarios
      - Gestión de roles (ADMIN, ABOGADO, CLIENTE)
      - Recuperación de contraseñas
      
      ### 📋 Gestión de Casos
      - Creación y gestión de expedientes
      - Asignación de casos a abogados
      - Seguimiento del estado de casos
      
      ### 📅 Citas y Agendas
      - Programación de citas entre abogados y clientes
      - Gestión de calendarios
      - Notificaciones de citas
      
      ### 📝 Documentos
      - Subida y gestión de documentos
      - Organización por expedientes
      - Control de acceso por roles
      
      ### ✅ Tareas y Seguimiento
      - Creación y asignación de tareas
      - Seguimiento de estado y prioridades
      - Notificaciones de tareas vencidas
      
      ### 💰 Facturación
      - Generación de facturas electrónicas
      - Gestión de provisiones de fondos
      - Firma digital de documentos
      
      ### 💬 Chat y Comunicación
      - Mensajería entre usuarios
      - Chat con IA para asistencia
      - Historial de conversaciones
      
      ### 📊 Reportes y Analytics
      - Estadísticas de casos y tareas
      - Reportes de productividad
      - Métricas de abogados
      
      ### ⚙️ Administración
      - Gestión de usuarios y permisos
      - Configuración de parámetros del sistema
      - Monitoreo y auditoría
    `)
    .setVersion('1.0.0')
    .addTag('auth', 'Autenticación y gestión de usuarios')
    .addTag('users', 'Gestión de usuarios y perfiles')
    .addTag('cases', 'Gestión de casos y expedientes')
    .addTag('appointments', 'Gestión de citas y agendas')
    .addTag('documents', 'Gestión de documentos')
    .addTag('tasks', 'Gestión de tareas y seguimiento')
    .addTag('invoices', 'Facturación electrónica')
    .addTag('provision-fondos', 'Gestión de provisiones de fondos')
    .addTag('chat', 'Chat y mensajería')
    .addTag('reports', 'Reportes y estadísticas')
    .addTag('admin', 'Funciones administrativas')
    .addTag('parametros', 'Configuración de parámetros del sistema')
    .addBearerAuth(
      {
        type: 'http',
        scheme: 'bearer',
        bearerFormat: 'JWT',
        name: 'JWT',
        description: 'Enter JWT token',
        in: 'header',
      },
      'JWT-auth',
    )
    .build();

  const document = SwaggerModule.createDocument(app, config);
  
  // Configurar Swagger UI con opciones personalizadas
  SwaggerModule.setup('api/docs', app, document, {
    swaggerOptions: {
      persistAuthorization: true,
      docExpansion: 'none',
      filter: true,
      showRequestDuration: true,
      syntaxHighlight: {
        activate: true,
        theme: 'monokai'
      },
      tryItOutEnabled: true,
    },
    customSiteTitle: 'Sistema de Gestión Legal - API Documentation',
    customCss: `
      .swagger-ui .opblock.opblock-patch .opblock-summary-method { background: #50e3c2; }
    `,
  });

  // Servir archivos estáticos desde la carpeta uploads
  app.use('/uploads', express.static(path.join(__dirname, '..', 'uploads')));

  const port = process.env.PORT || 3000;
  await app.listen(port);
  console.log(`🚀 Servidor corriendo en http://localhost:${port}`);
  console.log(`📁 Archivos estáticos disponibles en http://localhost:${port}/uploads`);
  console.log(`📚 Documentación Swagger disponible en http://localhost:${port}/api/docs`);
}
bootstrap();