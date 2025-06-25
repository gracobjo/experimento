# 🚀 Guía Rápida - Home Builder

## ⚡ Inicio Rápido

### 1. Acceder al Home Builder
```
URL: http://localhost:5174/admin/home-builder
```

### 2. Diseñar tu Página en 5 Pasos

#### Paso 1: Arrastrar Banner Principal
- **Desde la izquierda**: Busca "Banner Principal"
- **Arrastra** al canvas central
- **Haz clic** en él para seleccionarlo
- **Edita** en el panel derecho:
  - Título: "Despacho de Abogados García & Asociados"
  - Subtítulo: "Más de 15 años de experiencia"
  - Botón: "Consulta Gratuita"

#### Paso 2: Añadir Servicios
- **Arrastra** "Tarjetas de Servicios"
- **Selecciónalo** y edita:
  - Título: "Nuestros Servicios Legales"
  - **Haz clic** en "+ Agregar" para añadir servicios
  - **Para cada servicio**:
    - Icono: ⚖️ 👥 🏢 💰
    - Título: "Derecho Civil", "Derecho Laboral", etc.
    - Descripción: Descripción del servicio

#### Paso 3: Añadir Estadísticas
- **Arrastra** "Estadísticas"
- **Edita**:
  - Título: "Nuestros Números"
  - **Añade estadísticas**:
    - "500+" - "Casos Exitosos"
    - "15" - "Años de Experiencia"
    - "1000+" - "Clientes Satisfechos"

#### Paso 4: Añadir Testimonios
- **Arrastra** "Testimonios"
- **Edita**:
  - Título: "Lo que dicen nuestros clientes"
  - **Añade testimonios** con nombre y texto

#### Paso 5: Añadir Formulario de Contacto
- **Arrastra** "Formulario de Contacto"
- **Edita**:
  - Título: "Solicita tu Consulta Gratuita"
  - Subtítulo: "Nuestros abogados están listos para ayudarte"

### 3. Personalización Rápida

#### Iconos Útiles para Servicios
```
⚖️  Derecho Civil
👥  Derecho Laboral
👨‍👩‍👧‍👦  Derecho Familiar
🏢  Derecho Empresarial
💰  Contratos
💼  Herencias
🚗  Accidentes
🏠  Propiedad
📊  Fiscal
🛡️  Penal
```

#### Ejemplo de Servicios Completos
```json
{
  "services": [
    {
      "icon": "⚖️",
      "title": "Derecho Civil",
      "description": "Asesoría especializada en casos civiles y comerciales"
    },
    {
      "icon": "👥",
      "title": "Derecho Laboral", 
      "description": "Protección de derechos laborales y resolución de conflictos"
    },
    {
      "icon": "👨‍👩‍👧‍👦",
      "title": "Derecho Familiar",
      "description": "Divorcios, custodia y asuntos familiares"
    },
    {
      "icon": "🏢",
      "title": "Derecho Empresarial",
      "description": "Constitución de empresas y asesoría mercantil"
    }
  ]
}
```

### 4. Funciones Principales

#### Reordenar Componentes
- **Arrastra** componentes dentro del canvas
- **Cambia** el orden según tu preferencia

#### Vista Previa
- **Haz clic** en "Vista Previa"
- **Ve** cómo se verá la página final
- **Haz clic** en "Editar" para volver

#### Guardar Diseño
- **Haz clic** en "Guardar"
- **El diseño** se guarda automáticamente

### 5. Consejos de Diseño

#### Orden Recomendado
1. **Banner Principal** (primera impresión)
2. **Servicios** (qué ofreces)
3. **Estadísticas** (credibilidad)
4. **Testimonios** (social proof)
5. **Formulario** (llamada a la acción)

#### Textos Efectivos
- **Banner**: Título llamativo + beneficio principal
- **Servicios**: Títulos claros + descripciones específicas
- **CTA**: "Consulta Gratuita", "Solicitar Asesoría"

### 6. Solución de Problemas Rápidos

#### No Veo los Componentes
- Verifica que estés en `/admin/home-builder`
- Revisa la consola del navegador (F12)
- Recarga la página

#### No Funciona el Drag & Drop
- Asegúrate de que el componente esté seleccionado
- Intenta arrastrar desde el icono del componente
- Verifica que no haya errores en la consola

#### No Se Guardan los Cambios
- Haz clic en "Guardar" después de cada cambio importante
- Verifica que el backend esté funcionando
- Revisa la conexión a la base de datos

### 7. URLs Importantes

```
Frontend: http://localhost:5174/
Home Builder: http://localhost:5174/admin/home-builder
Backend API: http://localhost:3000/
```

### 8. Comandos Útiles

```bash
# Iniciar Frontend
cd frontend
npm run dev

# Iniciar Backend
cd backend
npm run start:dev

# Verificar dependencias
npm install @dnd-kit/core @dnd-kit/sortable @dnd-kit/utilities
```

---

**¡Listo para diseñar! 🎨** 