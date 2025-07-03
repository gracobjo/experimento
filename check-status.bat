@echo off
echo ========================================
echo   VERIFICACION DE ESTADO DEL SISTEMA
echo ========================================
echo.

echo 🔍 Verificando servicios...
echo.

REM Verificar backend
echo 1. Backend (NestJS - Puerto 3000):
curl -s -o nul -w "HTTP Status: %%{http_code}\n" http://localhost:3000/health 2>nul
if %errorlevel% equ 0 (
    echo ✅ Backend está ejecutándose correctamente
) else (
    echo ❌ Backend no está respondiendo
    echo    Verifica que esté ejecutándose con: cd backend ^&^& npm run start:dev
)
echo.

REM Verificar frontend
echo 2. Frontend (React - Puerto 5173):
curl -s -o nul -w "HTTP Status: %%{http_code}\n" http://localhost:5173 2>nul
if %errorlevel% equ 0 (
    echo ✅ Frontend está ejecutándose correctamente
) else (
    echo ❌ Frontend no está respondiendo
    echo    Verifica que esté ejecutándose con: cd frontend ^&^& npm run dev
)
echo.

REM Verificar base de datos
echo 3. Base de datos (PostgreSQL):
cd backend
npx prisma db execute --stdin <<< "SELECT 1;" 2>nul
if %errorlevel% equ 0 (
    echo ✅ Base de datos está conectada
) else (
    echo ❌ Error conectando a la base de datos
    echo    Verifica que PostgreSQL esté ejecutándose
)
cd ..
echo.

REM Verificar parámetros del sistema
echo 4. Parámetros del sistema:
curl -s http://localhost:3000/parametros/contact 2>nul | findstr "email" >nul
if %errorlevel% equ 0 (
    echo ✅ Parámetros de contacto configurados
) else (
    echo ⚠️  Parámetros de contacto no encontrados
    echo    Ejecuta: cd backend ^&^& npx ts-node scripts/initializeParams.ts
)
echo.

REM Verificar documentación Swagger
echo 5. Documentación API (Swagger):
curl -s -o nul -w "HTTP Status: %%{http_code}\n" http://localhost:3000/api 2>nul
if %errorlevel% equ 0 (
    echo ✅ Documentación Swagger disponible en http://localhost:3000/api
) else (
    echo ❌ Documentación Swagger no disponible
)
echo.

echo ========================================
echo   RESUMEN DE ESTADO
echo ========================================
echo.
echo 📊 URLs de acceso:
echo.
echo Frontend: http://localhost:5173
echo Backend: http://localhost:3000
echo API Docs: http://localhost:3000/api
echo Health Check: http://localhost:3000/health
echo.
echo 🔧 Comandos de reinicio:
echo.
echo Backend: cd backend ^&^& npm run start:dev
echo Frontend: cd frontend ^&^& npm run dev
echo.
echo 📝 Logs útiles:
echo.
echo Para ver logs del backend: cd backend ^&^& npm run start:dev
echo Para ver logs del frontend: cd frontend ^&^& npm run dev
echo Para abrir Prisma Studio: cd backend ^&^& npx prisma studio
echo.
pause 