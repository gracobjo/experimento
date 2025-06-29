# 📚 Guía de Acceso a Swagger - Sistema de Gestión Legal

## 🚀 Acceso Rápido

### URL Principal
```
http://localhost:3000/api/docs
```

### Requisitos Previos
- ✅ Servidor backend corriendo en puerto 3000
- ✅ Base de datos PostgreSQL configurada
- ✅ Variables de entorno configuradas

## 🔧 Configuración Inicial

### 1. Iniciar el Servidor Backend
```bash
cd backend
npm run start:dev
```

### 2. Verificar que el Servidor Esté Funcionando
Deberías ver en la consola:
```
🚀 Servidor corriendo en http://localhost:3000
📁 Archivos estáticos disponibles en http://localhost:3000/uploads
📚 Documentación Swagger disponible en http://localhost:3000/api/docs
```

### 3. Acceder a Swagger
Abre tu navegador y ve a: `http://localhost:3000/api/docs`

## 🎯 Características de Swagger

### ✅ Funcionalidades Disponibles
- **Documentación Interactiva** - Todos los endpoints documentados
- **Pruebas en Tiempo Real** - Prueba las APIs directamente desde el navegador
- **Autenticación JWT Integrada** - Sistema de autenticación completo
- **Filtrado por Tags** - Organización por funcionalidad
- **Esquemas Detallados** - Estructura de datos completa
- **Códigos de Respuesta** - Documentación de todos los códigos HTTP
- **Ejemplos de Uso** - Ejemplos prácticos para cada endpoint

### 🎨 Interfaz Personalizada
- **Diseño Adaptado** - Interfaz personalizada para el sistema legal
- **Sintaxis Resaltada** - Código con colores para mejor legibilidad
- **Persistencia de Autorización** - El token se mantiene entre sesiones
- **Filtrado Inteligente** - Búsqueda rápida de endpoints
- **Duración de Requests** - Muestra el tiempo de respuesta

## 🔐 Autenticación en Swagger

### Paso 1: Obtener Token
1. Expande la sección **auth** en Swagger
2. Busca el endpoint `POST /api/auth/login`
3. Haz clic en "Try it out"
4. Ingresa las credenciales:
   ```json
   {
     "email": "admin@despacho.com",
     "password": "password123"
   }
   ```
5. Ejecuta la petición
6. Copia el token de la respuesta

### Paso 2: Configurar Autorización
1. Haz clic en el botón **"Authorize"** en la parte superior
2. En el campo de autorización, ingresa:
   ```
   Bearer tu_token_jwt_aqui
   ```
3. Haz clic en "Authorize"
4. Cierra el modal

### Paso 3: Probar Endpoints Protegidos
Ahora puedes probar todos los endpoints que requieren autenticación.

## 📋 Organización de Endpoints

### 🔐 Autenticación (auth)
- `POST /api/auth/login` - Iniciar sesión
- `POST /api/auth/register` - Registrar usuario
- `POST /api/auth/forgot-password` - Recuperar contraseña
- `POST /api/auth/reset-password` - Restablecer contraseña
- `GET /api/auth/profile` - Obtener perfil del usuario

### 👤 Usuarios (users)
- `GET /api/users` - Listar usuarios
- `GET /api/users/:id` - Obtener usuario específico
- `POST /api/users` - Crear usuario
- `PATCH /api/users/:id` - Actualizar usuario
- `DELETE /api/users/:id` - Eliminar usuario

### 📋 Casos (cases)
- `GET /api/cases` - Listar casos
- `GET /api/cases/:id` - Obtener caso específico
- `POST /api/cases` - Crear caso
- `PATCH /api/cases/:id` - Actualizar caso
- `DELETE /api/cases/:id` - Eliminar caso
- `GET /api/cases/recent-activities` - Actividad reciente (abogados)
- `GET /api/cases/recent` - Casos recientes

### 📅 Citas (appointments)
- `GET /api/appointments` - Listar citas
- `POST /api/appointments` - Crear cita
- `DELETE /api/appointments/:id` - Eliminar cita
- `PUT /api/appointments/:id` - Actualizar cita

### 📄 Documentos (documents)
- `GET /api/documents` - Listar documentos
- `POST /api/documents` - Subir documento
- `GET /api/documents/:id` - Obtener documento
- `DELETE /api/documents/:id` - Eliminar documento

### ✅ Tareas (tasks)
- `GET /api/tasks` - Listar tareas
- `POST /api/tasks` - Crear tarea
- `GET /api/tasks/:id` - Obtener tarea
- `PATCH /api/tasks/:id` - Actualizar tarea
- `DELETE /api/tasks/:id` - Eliminar tarea

### 💰 Facturación (invoices)
- `GET /api/invoices` - Listar facturas
- `POST /api/invoices` - Crear factura
- `GET /api/invoices/:id` - Obtener factura
- `PATCH /api/invoices/:id` - Actualizar factura
- `DELETE /api/invoices/:id` - Eliminar factura

### 💳 Provisiones de Fondos (provision-fondos)
- `GET /api/provision-fondos` - Listar provisiones
- `POST /api/provision-fondos` - Crear provisión
- `PATCH /api/provision-fondos/:id` - Actualizar provisión

### 💬 Chat (chat)
- `GET /api/chat/messages` - Obtener mensajes
- `POST /api/chat/messages` - Enviar mensaje
- `GET /api/chat/conversations` - Obtener conversaciones

### 📊 Reportes (reports)
- `GET /api/reports` - Obtener reportes

### ⚙️ Administración (admin)
- `GET /api/admin/dashboard` - Dashboard administrativo
- `GET /api/admin/users` - Gestión de usuarios
- `GET /api/admin/cases` - Gestión de casos
- `GET /api/admin/appointments` - Gestión de citas
- `GET /api/admin/documents` - Gestión de documentos
- `GET /api/admin/tasks` - Gestión de tareas
- `GET /api/admin/reports` - Gestión de reportes
- `GET /api/admin/layouts` - Gestión de layouts
- `POST /api/admin/layouts` - Crear layout
- `PUT /api/admin/layouts/:id` - Actualizar layout
- `DELETE /api/admin/layouts/:id` - Eliminar layout

### 🔧 Parámetros (parametros)
- `GET /api/parametros` - Obtener parámetros
- `POST /api/parametros` - Crear parámetro
- `PUT /api/parametros/:id` - Actualizar parámetro
- `DELETE /api/parametros/:id` - Eliminar parámetro

## 🧪 Ejemplos de Prueba

### Ejemplo 1: Crear un Caso
1. Ve a la sección **cases**
2. Expande `POST /api/cases`
3. Haz clic en "Try it out"
4. Ingresa los datos:
   ```json
   {
     "title": "Caso de divorcio",
     "description": "Proceso de divorcio por mutuo acuerdo",
     "clientId": "client-uuid",
     "lawyerId": "lawyer-uuid"
   }
   ```
5. Ejecuta la petición

### Ejemplo 2: Obtener Casos Recientes
1. Ve a la sección **cases**
2. Expande `GET /api/cases/recent`
3. Haz clic en "Try it out"
4. Ejecuta la petición

### Ejemplo 3: Crear una Tarea
1. Ve a la sección **tasks**
2. Expande `POST /api/tasks`
3. Haz clic en "Try it out"
4. Ingresa los datos:
   ```json
   {
     "title": "Revisar documentación",
     "description": "Revisar documentos del caso de divorcio",
     "caseId": "case-uuid",
     "assignedTo": "lawyer-uuid",
     "priority": "MEDIA",
     "status": "PENDIENTE"
   }
   ```
5. Ejecuta la petición

## 🔍 Búsqueda y Filtrado

### Búsqueda de Endpoints
- Usa el campo de búsqueda en la parte superior
- Filtra por método HTTP (GET, POST, PUT, DELETE)
- Filtra por tags (auth, users, cases, etc.)

### Filtrado por Tags
- Haz clic en un tag específico para ver solo esos endpoints
- Usa múltiples tags para filtrar más específicamente

## 📊 Códigos de Respuesta

### Códigos Comunes
- **200** - Operación exitosa
- **201** - Recurso creado exitosamente
- **400** - Datos inválidos
- **401** - No autorizado (token inválido)
- **403** - Prohibido (rol insuficiente)
- **404** - Recurso no encontrado
- **409** - Conflicto (recurso duplicado)
- **500** - Error interno del servidor

## 🛠️ Solución de Problemas

### Problema: Swagger no carga
**Solución:**
1. Verificar que el servidor backend esté corriendo
2. Verificar que el puerto 3000 esté disponible
3. Revisar los logs del servidor

### Problema: Autenticación falla
**Solución:**
1. Verificar que el token esté en formato correcto: `Bearer token`
2. Verificar que el token no haya expirado
3. Obtener un nuevo token con login

### Problema: Endpoints no aparecen
**Solución:**
1. Verificar que los decoradores Swagger estén en los controladores
2. Reiniciar el servidor backend
3. Limpiar caché del navegador

### Problema: Errores de CORS
**Solución:**
1. Verificar configuración de CORS en `main.ts`
2. Verificar que el frontend esté en la URL permitida
3. Revisar variables de entorno

## 📚 Documentación Relacionada

- **[Configuración de Swagger](swagger-configuracion.md)** - Detalles técnicos de la implementación
- **[Endpoints Completos](swagger-endpoints.md)** - Lista detallada de todos los endpoints
- **[Guía Rápida](guia-rapida.md)** - Instalación y configuración básica
- **[Configuración Avanzada](configuracion-avanzada.md)** - Configuraciones detalladas

## 🎯 Beneficios de Usar Swagger

### Para Desarrolladores
- ✅ **Documentación Automática** - Siempre actualizada
- ✅ **Pruebas Interactivas** - Sin herramientas externas
- ✅ **Esquemas Claros** - Estructura de datos definida
- ✅ **Ejemplos de Uso** - Implementación guiada

### Para el Equipo
- ✅ **Comunicación Mejorada** - API clara para todos
- ✅ **Onboarding Rápido** - Nuevos desarrolladores
- ✅ **Testing Simplificado** - Pruebas directas
- ✅ **Documentación Viva** - Siempre sincronizada

### Para el Cliente
- ✅ **Transparencia** - API completamente documentada
- ✅ **Facilidad de Integración** - Ejemplos claros
- ✅ **Soporte Mejorado** - Problemas más fáciles de resolver

## 🔄 Mantenimiento

### Actualizaciones Automáticas
- Los cambios en los controladores se reflejan automáticamente
- Los DTOs actualizados se documentan automáticamente
- Los nuevos endpoints aparecen automáticamente

### Buenas Prácticas
- ✅ Mantener descripciones claras y concisas
- ✅ Usar ejemplos relevantes
- ✅ Documentar todos los códigos de respuesta
- ✅ Organizar endpoints por funcionalidad
- ✅ Mantener consistencia en la nomenclatura

---

**URL de Acceso**: `http://localhost:3000/api/docs`  
**Total de endpoints documentados**: 60+  
**Fecha de última actualización**: Diciembre 2024  
**Versión**: 1.0.0 