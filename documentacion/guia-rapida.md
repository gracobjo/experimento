# ⚡ Guía Rápida de Instalación

## Requisitos Mínimos
- Node.js 18+
- Python 3.8+
- PostgreSQL 12+
- Git

## Instalación Express (5 minutos)

```bash
# 1. Clonar repositorio
git clone <URL_REPO>
cd experimento

# 2. Configurar base de datos
psql -U postgres
CREATE DATABASE despacho_abogados;
CREATE USER despacho_user WITH PASSWORD 'password123';
GRANT ALL PRIVILEGES ON DATABASE despacho_abogados TO despacho_user;
\q

# 3. Configurar variables de entorno
cd backend
cp .env.example .env
# Editar .env con tus credenciales

# 4. Instalar y configurar todo
cd backend && npm install && npx prisma generate && npx prisma migrate dev
cd ../frontend && npm install
cd ../chatbot && python -m venv venv && source venv/bin/activate && pip install -r requirements.txt

# 5. Ejecutar
# Windows: start-all.bat
# Unix: ./start-all.sh
```

## URLs de Acceso
- Frontend: http://localhost:5173
- Backend: http://localhost:3000
- Chatbot: http://localhost:8000

## Comandos Útiles

```bash
# Reiniciar servicios
cd backend && npm run start:dev
cd frontend && npm run dev
cd chatbot && python main_improved.py

# Ver logs
tail -f backend/logs/app.log

# Limpiar caché
npm run cleanup  # backend
rm -rf frontend/node_modules/.vite
```

## Problemas Comunes

**Puerto ocupado:**
```bash
# Windows
netstat -ano | findstr :3000 && taskkill /PID <PID> /F

# Unix
lsof -ti:3000 | xargs kill -9
```

**Base de datos:**
```bash
npx prisma migrate reset  # Reset completo
npx prisma db push        # Sincronizar schema
``` 