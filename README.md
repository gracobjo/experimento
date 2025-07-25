cd# 🏛️ Sistema de Gestión Legal - Experimento

## Descripción
Plataforma integral para la gestión de despachos legales: casos, documentos, facturación electrónica, chat, teleasistencia y cumplimiento legal. Basada en NestJS, Prisma, React y PostgreSQL.

---

## Características Principales
- Gestión de usuarios y roles (ADMIN, ABOGADO, CLIENTE)
- Gestión de casos legales y documentos
- Facturación electrónica (Facturae XML, PDF)
- Provisión de fondos y control de anticipos
- Citas y agenda
- Chat en tiempo real (WebSocket)
- Teleasistencia integrada
- Panel de administración
- Parámetros y contenido legal editable (privacidad, cookies, términos)
- API REST documentada con Swagger

---

## Tecnologías
- **Frontend:** React + Vite + Tailwind CSS
- **Backend:** NestJS + Prisma ORM
- **Base de datos:** PostgreSQL
- **Comunicación:** REST API + WebSocket
- **Infraestructura:** Scripts de setup, migraciones Prisma

---

## Instalación Rápida

### 1. Clona el repositorio
```bash
git clone <URL_DEL_REPO>
cd experimento
```

### 2. Configura variables de entorno
- Copia `.env.example` a `.env` en `backend/` y `frontend/`.
- Ajusta las credenciales de base de datos y JWT.

### 3. Instala dependencias y ejecuta migraciones
```bash
cd backend
npm install
npx prisma migrate deploy
npx prisma generate
cd ../frontend
npm install
```

### 4. Inicia los servicios
```bash
# Backend
cd backend
npm run start:dev
# Frontend
cd ../frontend
npm run dev
```

---

## Endpoints y URLs
- **Frontend:** http://localhost:5173
- **Backend API:** http://localhost:3000
- **Swagger Docs:** http://localhost:3000/api
- **Panel Admin:** http://localhost:5173/admin
- **Páginas legales:** `/privacidad`, `/terminos`, `/cookies`

---

## Scripts Útiles
- `setup-completo.bat`: Configuración y despliegue rápido
- `check-status.bat`: Verificación de estado de servicios
- `setup-params.bat`: Inicialización de parámetros legales/contacto

---

## Documentación para Desarrolladores
Consulta `documentacion/DESARROLLADORES.md` para:
- Arquitectura y modelo de datos
- Casos de uso y diagramas UML
- Especificación de endpoints y flujos
- Guía para subir documentación a Notion

---

## Despliegue en Producción
- **Backend:** Heroku, Railway, Render (PostgreSQL gratuito)
- **Frontend:** Vercel, Netlify
- **Configura variables de entorno y ejecuta migraciones**

---

## Contribuir
1. Haz un fork del repositorio
2. Crea una rama (`git checkout -b feature/nueva-funcionalidad`)
3. Haz tus cambios y commitea (`git commit -am 'feat: nueva funcionalidad'`)
4. Haz push a tu rama (`git push origin feature/nueva-funcionalidad`)
5. Abre un Pull Request

---

## Licencia
MIT

---

**¿Dudas o sugerencias?**
- Abre un issue en GitHub
- Contacta al equipo de desarrollo
