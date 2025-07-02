# 🔄 Guía de Actualizaciones - Sistema Legal Experimento

## 📋 Proceso de Actualización de Repositorios

### **Opción 1: Actualización Manual (Recomendada)**

#### Para cambios en el Backend:
```bash
# 1. Ir al directorio del backend
cd experimento-backend

# 2. Hacer los cambios en tu código

# 3. Agregar cambios al staging
git add .

# 4. Hacer commit con mensaje descriptivo
git commit -m "feat: agregar nueva funcionalidad de reportes"

# 5. Subir cambios al repositorio
git push origin main
```

#### Para cambios en el Frontend:
```bash
# 1. Ir al directorio del frontend
cd experimento-frontend

# 2. Hacer los cambios en tu código

# 3. Agregar cambios al staging
git add .

# 4. Hacer commit con mensaje descriptivo
git commit -m "feat: mejorar interfaz de usuario del dashboard"

# 5. Subir cambios al repositorio
git push origin main
```

#### Para cambios en el Chatbot:
```bash
# 1. Ir al directorio del chatbot
cd experimento-chatbot

# 2. Hacer los cambios en tu código

# 3. Agregar cambios al staging
git add .

# 4. Hacer commit con mensaje descriptivo
git commit -m "feat: agregar nuevas respuestas del chatbot"

# 5. Subir cambios al repositorio
git push origin main
```

### **Opción 2: Actualización Automática con GitHub Actions**

Si configuras GitHub Actions, los cambios se despliegan automáticamente:

1. **Hacer cambios** en tu código local
2. **Subir a GitHub** con `git push`
3. **GitHub Actions** detecta los cambios automáticamente
4. **Se despliega automáticamente** en Render/Vercel

### **Opción 3: Script de Actualización Automatizada**

Puedes crear un script que actualice todos los repositorios de una vez:

```bash
#!/bin/bash
# update-all.sh

echo "🔄 Actualizando todos los repositorios..."

# Actualizar Backend
echo "📦 Actualizando Backend..."
cd experimento-backend
git add .
git commit -m "feat: actualización automática - $(date)"
git push origin main
cd ..

# Actualizar Frontend
echo "🌐 Actualizando Frontend..."
cd experimento-frontend
git add .
git commit -m "feat: actualización automática - $(date)"
git push origin main
cd ..

# Actualizar Chatbot
echo "🤖 Actualizando Chatbot..."
cd experimento-chatbot
git add .
git commit -m "feat: actualización automática - $(date)"
git push origin main
cd ..

echo "✅ Todos los repositorios actualizados"
```

## 🚀 Despliegue Automático

### **Render (Backend y Chatbot)**
- Los cambios se despliegan automáticamente cuando detecta un push a `main`
- Puedes ver el progreso en el dashboard de Render
- Tiempo estimado: 2-5 minutos

### **Vercel (Frontend)**
- Los cambios se despliegan automáticamente cuando detecta un push a `main`
- Puedes ver el progreso en el dashboard de Vercel
- Tiempo estimado: 1-3 minutos

## 📝 Convenciones de Commits

### **Formato Recomendado:**
```
tipo: descripción breve

feat: nueva funcionalidad
fix: corrección de bug
docs: cambios en documentación
style: cambios de formato
refactor: refactorización de código
test: agregar o modificar tests
chore: tareas de mantenimiento
```

### **Ejemplos:**
```bash
git commit -m "feat: agregar sistema de notificaciones push"
git commit -m "fix: corregir error en login de usuarios"
git commit -m "docs: actualizar documentación de API"
git commit -m "style: mejorar diseño del dashboard"
```

## 🔍 Verificación de Actualizaciones

### **Verificar Despliegue:**
1. **Backend**: `curl https://tu-backend.onrender.com/health`
2. **Chatbot**: `curl https://tu-chatbot.onrender.com/health`
3. **Frontend**: Visitar la URL de Vercel

### **Logs de Despliegue:**
- **Render**: Dashboard → Services → Logs
- **Vercel**: Dashboard → Deployments → View Function Logs

## 🛠️ Casos Especiales

### **Cambios en Base de Datos (Backend)**
```bash
# Si agregas nuevas migraciones de Prisma
cd experimento-backend
npx prisma migrate dev --name nueva_migracion
git add .
git commit -m "feat: agregar nueva migración de base de datos"
git push origin main
```

### **Cambios en Variables de Entorno**
1. **Render**: Dashboard → Services → Environment
2. **Vercel**: Dashboard → Settings → Environment Variables

### **Cambios en Dependencias**
```bash
# Backend
cd experimento-backend
npm install nueva-dependencia
git add package.json package-lock.json
git commit -m "feat: agregar nueva dependencia"
git push origin main

# Frontend
cd experimento-frontend
npm install nueva-dependencia
git add package.json package-lock.json
git commit -m "feat: agregar nueva dependencia"
git push origin main

# Chatbot
cd experimento-chatbot
pip install nueva-dependencia
pip freeze > requirements.txt
git add requirements.txt
git commit -m "feat: agregar nueva dependencia"
git push origin main
```

## 🔄 Flujo de Trabajo Recomendado

### **Para Desarrollo Diario:**
1. **Hacer cambios** en tu código local
2. **Probar localmente** que funciona
3. **Hacer commit** con mensaje descriptivo
4. **Subir cambios** con `git push`
5. **Verificar despliegue** automático
6. **Probar en producción**

### **Para Versiones Importantes:**
1. **Crear rama** para la nueva funcionalidad
2. **Desarrollar** en la rama
3. **Hacer merge** a main
4. **Subir cambios** con `git push`
5. **Verificar despliegue** automático
6. **Probar en producción**

## 📊 Monitoreo de Actualizaciones

### **GitHub:**
- Ver commits recientes
- Verificar que los pushes fueron exitosos
- Revisar GitHub Actions (si configurado)

### **Plataformas de Despliegue:**
- **Render**: Dashboard → Services → Deployments
- **Vercel**: Dashboard → Deployments

## 🚨 Troubleshooting

### **Error: Push Rechazado**
```bash
# Si hay cambios en el repositorio remoto
git pull origin main
# Resolver conflictos si los hay
git push origin main
```

### **Error: Despliegue Fallido**
1. **Revisar logs** en la plataforma de despliegue
2. **Verificar variables de entorno**
3. **Probar localmente** antes de subir
4. **Revisar dependencias** y configuración

### **Error: Build Fallido**
1. **Verificar sintaxis** del código
2. **Revisar dependencias** en package.json/requirements.txt
3. **Probar build local** antes de subir

## 🎯 Resumen

### **Proceso Simple:**
1. **Hacer cambios** en tu código
2. **git add .** - Agregar cambios
3. **git commit -m "mensaje"** - Hacer commit
4. **git push origin main** - Subir cambios
5. **Esperar despliegue** automático (2-5 minutos)
6. **Verificar** que funciona en producción

### **Ventajas:**
- ✅ **Despliegue automático** en cada push
- ✅ **Rollback automático** si algo falla
- ✅ **Historial completo** de cambios
- ✅ **Fácil colaboración** con otros desarrolladores
- ✅ **Monitoreo** de despliegues

¡Con este proceso, mantener tu aplicación actualizada será muy sencillo! 🚀 