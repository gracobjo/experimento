# Facturación Electrónica Avanzada - Facturae 3.2.2

Este módulo implementa funcionalidades avanzadas de facturación electrónica siguiendo el estándar Facturae 3.2.2 del Ministerio de Industria, Comercio y Turismo de España.

## 🚀 Características Implementadas

### ✅ **Fase 1: Estructura XML Completa**
- **XML Facturae 3.2.2 completo** con todos los campos obligatorios
- **Interfaces TypeScript** para tipado seguro
- **Validación XSD** contra esquemas oficiales
- **Generación automática** de elementos requeridos

### ✅ **Fase 2: Firma Digital Avanzada**
- **XAdES-BES**: Firma básica electrónica
- **XAdES-T**: Con sello de tiempo (TSA)
- **XAdES-C**: Con validación de certificados (OCSP)
- **XAdES-X**: Con sello de tiempo de la firma
- **XAdES-XL**: Con validación de largo plazo

### ✅ **Fase 3: Validación y Verificación**
- **Validación de esquema XSD**
- **Reglas de negocio** específicas de Facturae
- **Verificación de certificados** digitales
- **Validación de firmas** criptográficas
- **Reportes de validación** detallados

### ✅ **Fase 4: Gestión de Certificados**
- **Validación de certificados** X.509
- **Verificación OCSP** en tiempo real
- **Gestión de sellos de tiempo** TSA
- **Información de certificados** detallada

## 📁 Estructura de Archivos

```
src/invoices/
├── facturae-xml.util.ts          # Generación XML Facturae 3.2.2
├── xades-sign.util.ts            # Firma digital XAdES avanzada
├── facturae-validator.util.ts    # Validación y verificación
├── facturae.service.ts           # Servicio principal integrado
├── facturae.controller.ts        # Controlador REST API
└── invoices.service.ts           # Servicio actualizado

src/config/
└── facturae.config.ts            # Configuración centralizada
```

## 🔧 Configuración

### Variables de Entorno

```bash
# Rutas de certificados
FACTURAE_CERT_PATH=./certs/certificate.pem
FACTURAE_KEY_PATH=./certs/private_key.pem

# Servicios externos
FACTURAE_TSA_URL=https://tsa.example.com/timestamp
FACTURAE_OCSP_URL=https://ocsp.example.com

# Configuración de firma
FACTURAE_XADES_LEVEL=BES  # BES, T, C, X, XL

# Rutas de salida
FACTURAE_OUTPUT_PATH=./output

# Configuración de validación
FACTURAE_STRICT_VALIDATION=false
FACTURAE_ENABLE_LOGS=true
```

### Certificados Digitales

Los certificados deben estar en formato PEM:

```bash
# Crear directorio de certificados
mkdir -p certs

# Copiar certificado y clave privada
cp tu_certificado.pem certs/certificate.pem
cp tu_clave_privada.pem certs/private_key.pem
```

## 🛠️ Uso de la API

### 1. Generar y Firmar Factura Avanzada

```http
POST /facturae/{id}/generate-and-sign
Content-Type: application/json

{
  "level": "T",
  "tsaUrl": "https://tsa.example.com/timestamp",
  "ocspUrl": "https://ocsp.example.com",
  "policy": "urn:oid:2.16.724.1.3.1.1.2.1.9",
  "signerRole": "Emisor"
}
```

### 2. Validar Factura

```http
GET /facturae/{id}/validate?signature=true
```

### 3. Información del Certificado

```http
GET /facturae/certificate/info
GET /facturae/certificate/status
```

### 4. Reporte de Validación

```http
GET /facturae/{id}/validation-report
```

### 5. Validar XML Directamente

```http
POST /facturae/validate-xml
Content-Type: application/json

{
  "xml": "<Facturae>...</Facturae>",
  "checkSignature": true
}
```

## 📊 Niveles XAdES Soportados

| Nivel | Descripción | Requisitos |
|-------|-------------|------------|
| **BES** | Basic Electronic Signature | Certificado + Clave privada |
| **T** | Timestamp | BES + Servidor TSA |
| **C** | Certificate Validation | T + Servidor OCSP |
| **X** | Extended Validation | C + Sellos adicionales |
| **XL** | Long-term Validation | X + Archivo de largo plazo |

## 🔍 Validaciones Implementadas

### Validación de Esquema
- ✅ Estructura XML correcta
- ✅ Namespaces válidos
- ✅ Elementos obligatorios presentes
- ✅ Tipos de datos correctos

### Validación de Negocio
- ✅ NIF válido (formato español)
- ✅ Totales coherentes
- ✅ Fechas válidas
- ✅ Códigos de país ISO
- ✅ Al menos un item por factura

### Validación de Firma
- ✅ Estructura de firma correcta
- ✅ Certificado presente
- ✅ Elementos XAdES válidos
- ✅ Sellos de tiempo (si aplica)

## 🚨 Manejo de Errores

El sistema proporciona errores detallados:

```json
{
  "success": false,
  "errors": [
    "NIF inválido: 12345678A",
    "Los totales de la factura no coinciden"
  ],
  "warnings": [
    "El documento no contiene firma digital"
  ]
}
```

## 📈 Reportes de Validación

Los reportes incluyen:

- **Información del certificado**
- **Estado de validación**
- **Errores encontrados**
- **Advertencias**
- **Recomendaciones**

## 🔐 Seguridad

- **Certificados seguros**: Solo certificados válidos
- **Firmas criptográficas**: RSA-SHA256
- **Validación OCSP**: Verificación en tiempo real
- **Sellos de tiempo**: Garantía de no repudio
- **Logs seguros**: Sin información sensible

## 🧪 Testing

```bash
# Ejecutar tests específicos de facturación
npm run test -- --testPathPattern=facturae

# Tests de validación
npm run test -- --testPathPattern=validator

# Tests de firma
npm run test -- --testPathPattern=xades
```

## 📚 Referencias

- [Facturae 3.2.2 - Ministerio de Industria](https://www.facturae.gob.es/)
- [XAdES - ETSI](https://www.etsi.org/standards)
- [XML-DSig - W3C](https://www.w3.org/TR/xmldsig-core/)

## 🤝 Contribución

Para contribuir al desarrollo:

1. Seguir estándares de código
2. Agregar tests para nuevas funcionalidades
3. Documentar cambios en este README
4. Validar contra esquemas oficiales

## 📞 Soporte

Para problemas o consultas:

- Revisar logs del sistema
- Verificar configuración de certificados
- Consultar documentación oficial de Facturae
- Contactar al equipo de desarrollo 