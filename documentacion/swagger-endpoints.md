# 📚 Documentación de Endpoints - Sistema de Gestión Legal

## 🎯 Resumen de la API

**URL Base**: `http://localhost:3000/api`  
**Documentación Swagger**: `http://localhost:3000/api/docs`  
**Autenticación**: JWT Bearer Token

## 🔐 Autenticación y Usuarios

### Base: `/auth`

| Método | Endpoint | Descripción | Roles | Autenticación |
|--------|----------|-------------|-------|---------------|
| `POST` | `/auth/login` | Iniciar sesión | Todos | No |
| `POST` | `/auth/register` | Registrar nuevo usuario | Todos | No |
| `POST` | `/auth/forgot-password` | Solicitar recuperación de contraseña | Todos | No |
| `POST` | `/auth/reset-password` | Restablecer contraseña | Todos | No |
| `GET` | `/auth/me` | Obtener perfil del usuario actual | Todos | Sí |

---

## 👥 Gestión de Usuarios

### Base: `/users`

| Método | Endpoint | Descripción | Roles | Autenticación |
|--------|----------|-------------|-------|---------------|
| `POST` | `/users` | Crear nuevo usuario | ADMIN, ABOGADO | Sí |
| `GET` | `/users` | Obtener todos los usuarios | ADMIN | Sí |
| `GET` | `/users/:id` | Obtener usuario por ID | ADMIN | Sí |
| `PATCH` | `/users/:id` | Actualizar usuario | ADMIN | Sí |
| `DELETE` | `/users/:id` | Eliminar usuario | ADMIN | Sí |
| `GET` | `/users/clients` | Obtener todos los clientes | ADMIN, ABOGADO | Sí |
| `GET` | `/users/clients/my` | Obtener mis clientes | ABOGADO | Sí |
| `GET` | `/users/clients/stats` | Estadísticas de clientes | ADMIN, ABOGADO | Sí |
| `GET` | `/users/clients/report` | Reporte de clientes | ADMIN, ABOGADO | Sí |
| `GET` | `/users/lawyers` | Obtener todos los abogados | ADMIN, ABOGADO, CLIENTE | Sí |

---

## 📋 Gestión de Casos

### Base: `/cases`

| Método | Endpoint | Descripción | Roles | Autenticación |
|--------|----------|-------------|-------|---------------|
| `POST` | `/cases` | Crear nuevo caso | ADMIN, ABOGADO | Sí |
| `GET` | `/cases` | Obtener todos los casos | ADMIN, ABOGADO, CLIENTE | Sí |
| `GET` | `/cases/stats` | Estadísticas de casos | Todos | Sí |
| `GET` | `/cases/:id` | Obtener caso por ID | Todos | Sí |
| `PATCH` | `/cases/:id` | Actualizar caso | ADMIN, ABOGADO | Sí |
| `DELETE` | `/cases/:id` | Eliminar caso | ADMIN | Sí |

---

## 📅 Gestión de Citas

### Base: `/appointments`

| Método | Endpoint | Descripción | Roles | Autenticación |
|--------|----------|-------------|-------|---------------|
| `POST` | `/appointments` | Crear nueva cita | Todos | Sí |
| `GET` | `/appointments` | Obtener todas las citas | Todos | Sí |

---

## 📝 Gestión de Documentos

### Base: `/documents`

| Método | Endpoint | Descripción | Roles | Autenticación |
|--------|----------|-------------|-------|---------------|
| `POST` | `/documents` | Subir nuevo documento | Todos | Sí |
| `GET` | `/documents` | Obtener todos los documentos | Todos | Sí |
| `GET` | `/documents/:id` | Obtener documento por ID | Todos | Sí |
| `GET` | `/documents/expediente/:expedienteId` | Obtener documentos por expediente | Todos | Sí |
| `DELETE` | `/documents/:id` | Eliminar documento | Todos | Sí |

---

## ✅ Gestión de Tareas

### Base: `/tasks`

| Método | Endpoint | Descripción | Roles | Autenticación |
|--------|----------|-------------|-------|---------------|
| `POST` | `/tasks` | Crear nueva tarea | Todos | Sí |
| `GET` | `/tasks` | Obtener todas las tareas | Todos | Sí |
| `GET` | `/tasks/stats` | Estadísticas de tareas | Todos | Sí |
| `GET` | `/tasks/upcoming` | Tareas próximas | Todos | Sí |
| `GET` | `/tasks/:id` | Obtener tarea por ID | Todos | Sí |
| `PATCH` | `/tasks/:id` | Actualizar tarea | Todos | Sí |
| `PATCH` | `/tasks/:id/status` | Actualizar estado de tarea | Todos | Sí |
| `DELETE` | `/tasks/:id` | Eliminar tarea | Todos | Sí |

---

## 💰 Facturación Electrónica

### Base: `/invoices`

| Método | Endpoint | Descripción | Roles | Autenticación |
|--------|----------|-------------|-------|---------------|
| `POST` | `/invoices` | Crear nueva factura | ABOGADO | Sí |
| `GET` | `/invoices` | Obtener todas las facturas | ADMIN, ABOGADO | Sí |
| `GET` | `/invoices/:id` | Obtener factura por ID | ADMIN, ABOGADO | Sí |
| `PATCH` | `/invoices/:id` | Actualizar factura | ABOGADO | Sí |
| `DELETE` | `/invoices/:id` | Eliminar factura | ABOGADO | Sí |
| `POST` | `/invoices/:id/sign` | Firmar factura digitalmente | ABOGADO | Sí |

---

## 💳 Gestión de Provisiones de Fondos

### Base: `/provision-fondos`

| Método | Endpoint | Descripción | Roles | Autenticación |
|--------|----------|-------------|-------|---------------|
| `POST` | `/provision-fondos` | Crear nueva provisión | Todos | Sí |
| `GET` | `/provision-fondos` | Obtener todas las provisiones | Todos | Sí |
| `GET` | `/provision-fondos/:id` | Obtener provisión por ID | Todos | Sí |
| `PATCH` | `/provision-fondos/link-to-invoice` | Vincular a factura | Todos | Sí |

---

## 💬 Chat y Mensajería

### Base: `/chat`

| Método | Endpoint | Descripción | Roles | Autenticación |
|--------|----------|-------------|-------|---------------|
| `GET` | `/chat/test` | Probar conexión del chat | Todos | No |
| `GET` | `/chat/messages` | Obtener mensajes | Todos | Sí |
| `POST` | `/chat/messages` | Enviar mensaje | Todos | Sí |
| `GET` | `/chat/conversations` | Obtener conversaciones | Todos | Sí |
| `GET` | `/chat/messages/:userId` | Obtener mensajes con usuario específico | Todos | Sí |

---

## 📊 Reportes y Estadísticas

### Base: `/lawyer/reports`

| Método | Endpoint | Descripción | Roles | Autenticación |
|--------|----------|-------------|-------|---------------|
| `GET` | `/lawyer/reports` | Reportes del abogado | ABOGADO | Sí |

---

## ⚙️ Funciones Administrativas

### Base: `/admin`

| Método | Endpoint | Descripción | Roles | Autenticación |
|--------|----------|-------------|-------|---------------|
| `GET` | `/admin/dashboard` | Dashboard administrativo | ADMIN | Sí |
| `GET` | `/admin/users` | Gestión de usuarios | ADMIN | Sí |
| `GET` | `/admin/users/:id` | Usuario específico | ADMIN | Sí |
| `PUT` | `/admin/users/:id` | Actualizar usuario | ADMIN | Sí |
| `DELETE` | `/admin/users/:id` | Eliminar usuario | ADMIN | Sí |
| `GET` | `/admin/cases` | Gestión de casos | ADMIN | Sí |
| `GET` | `/admin/cases/:id` | Caso específico | ADMIN | Sí |
| `PUT` | `/admin/cases/:id` | Actualizar caso | ADMIN | Sí |
| `DELETE` | `/admin/cases/:id` | Eliminar caso | ADMIN | Sí |
| `GET` | `/admin/appointments` | Gestión de citas | ADMIN | Sí |
| `GET` | `/admin/appointments/:id` | Cita específica | ADMIN | Sí |
| `PUT` | `/admin/appointments/:id` | Actualizar cita | ADMIN | Sí |
| `DELETE` | `/admin/appointments/:id` | Eliminar cita | ADMIN | Sí |
| `GET` | `/admin/tasks` | Gestión de tareas | ADMIN | Sí |
| `GET` | `/admin/tasks/:id` | Tarea específica | ADMIN | Sí |
| `PUT` | `/admin/tasks/:id` | Actualizar tarea | ADMIN | Sí |
| `DELETE` | `/admin/tasks/:id` | Eliminar tarea | ADMIN | Sí |
| `GET` | `/admin/documents` | Gestión de documentos | ADMIN | Sí |
| `GET` | `/admin/documents/:id` | Documento específico | ADMIN | Sí |
| `DELETE` | `/admin/documents/:id` | Eliminar documento | ADMIN | Sí |
| `GET` | `/admin/reports` | Reportes del sistema | ADMIN | Sí |

---

## 🔧 Configuración de Parámetros

### Base: `/parametros`

| Método | Endpoint | Descripción | Roles | Autenticación |
|--------|----------|-------------|-------|---------------|
| `GET` | `/parametros` | Obtener todos los parámetros | ADMIN | Sí |
| `GET` | `/parametros/:id` | Obtener parámetro por ID | ADMIN | Sí |
| `POST` | `/parametros` | Crear nuevo parámetro | ADMIN | Sí |
| `PUT` | `/parametros/:id` | Actualizar parámetro | ADMIN | Sí |
| `DELETE` | `/parametros/:id` | Eliminar parámetro | ADMIN | Sí |

---

## 🔑 Autenticación y Autorización

### JWT Token
Todos los endpoints protegidos requieren un token JWT en el header:
```
Authorization: Bearer <token>
```

### Roles del Sistema
- **ADMIN**: Acceso completo a todas las funcionalidades
- **ABOGADO**: Gestión de casos, clientes, citas y tareas
- **CLIENTE**: Acceso limitado a sus propios casos y documentos

### Códigos de Respuesta
- `200`: Operación exitosa
- `201`: Recurso creado exitosamente
- `400`: Datos inválidos
- `401`: No autorizado (token inválido o faltante)
- `403`: Prohibido (rol insuficiente)
- `404`: Recurso no encontrado
- `409`: Conflicto (recurso ya existe)
- `500`: Error interno del servidor

---

## 📋 Ejemplos de Uso

### 1. Iniciar Sesión
```bash
curl -X POST http://localhost:3000/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@despacho.com",
    "password": "password123"
  }'
```

### 2. Crear un Caso (con autenticación)
```bash
curl -X POST http://localhost:3000/api/cases \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "title": "Caso de divorcio",
    "description": "Proceso de divorcio por mutuo acuerdo",
    "clientId": "client-uuid",
    "lawyerId": "lawyer-uuid"
  }'
```

### 3. Obtener Casos del Usuario
```bash
curl -X GET http://localhost:3000/api/cases \
  -H "Authorization: Bearer <token>"
```

---

## 🚀 Acceso a la Documentación

### Swagger UI
La documentación interactiva está disponible en:
```
http://localhost:3000/api/docs
```

### Características de Swagger
- ✅ Documentación interactiva
- ✅ Pruebas de endpoints en tiempo real
- ✅ Esquemas de datos detallados
- ✅ Ejemplos de request/response
- ✅ Autenticación JWT integrada
- ✅ Filtrado por tags
- ✅ Búsqueda de endpoints

### Organización por Tags
- **auth**: Autenticación y gestión de usuarios
- **users**: Gestión de usuarios y perfiles
- **cases**: Gestión de casos y expedientes
- **appointments**: Gestión de citas y agendas
- **documents**: Gestión de documentos
- **tasks**: Gestión de tareas y seguimiento
- **invoices**: Facturación electrónica
- **provision-fondos**: Gestión de provisiones de fondos
- **chat**: Chat y mensajería
- **reports**: Reportes y estadísticas
- **admin**: Funciones administrativas
- **parametros**: Configuración de parámetros del sistema

---

**Última actualización**: Diciembre 2024  
**Versión de la API**: 1.0.0  
**Total de endpoints**: 60+ 