import { Controller, Get, Post, Body, Param, Put, Delete, UseGuards, UsePipes, ValidationPipe, Request, HttpException, HttpStatus, Patch, Res } from '@nestjs/common';
import { ApiTags, ApiOperation, ApiResponse, ApiBearerAuth, ApiBody, ApiParam } from '@nestjs/swagger';
import { InvoicesService } from './invoices.service';
import { CreateInvoiceDto } from './dto/create-invoice.dto';
import { UpdateInvoiceDto } from './dto/update-invoice.dto';
import { JwtAuthGuard } from '../auth/guards/jwt-auth.guard';
import { RolesGuard } from '../auth/guards/roles.guard';
import { Roles } from '../auth/decorators/roles.decorator';
import { Role } from '@prisma/client';
import { Response } from 'express';

@ApiTags('invoices')
@Controller('invoices')
@ApiBearerAuth('JWT-auth')
export class InvoicesController {
  constructor(private readonly invoicesService: InvoicesService) {}

  @Post()
  @Roles(Role.CLIENTE, Role.ABOGADO)
  @UseGuards(JwtAuthGuard, RolesGuard)
  @UsePipes(new ValidationPipe({ whitelist: true }))
  @ApiOperation({ 
    summary: 'Crear factura',
    description: 'Crea una nueva factura electrónica (solo ABOGADO)'
  })
  @ApiBody({ type: CreateInvoiceDto })
  @ApiResponse({ 
    status: 201, 
    description: 'Factura creada exitosamente',
    schema: {
      type: 'object',
      properties: {
        id: { type: 'string' },
        numero: { type: 'string' },
        fecha: { type: 'string', format: 'date' },
        emisorId: { type: 'string' },
        receptorId: { type: 'string' },
        importeTotal: { type: 'number' },
        estado: { type: 'string' },
        createdAt: { type: 'string', format: 'date-time' }
      }
    }
  })
  @ApiResponse({ status: 400, description: 'Datos inválidos' })
  @ApiResponse({ status: 401, description: 'No autorizado' })
  @ApiResponse({ status: 403, description: 'Rol insuficiente' })
  async create(@Body() createInvoiceDto: CreateInvoiceDto, @Request() req) {
    // Forzar que el emisorId sea el del usuario autenticado
    createInvoiceDto.emisorId = req.user.id;
    try {
      return await this.invoicesService.create(createInvoiceDto);
    } catch (error: any) {
      console.error('Invoice creation error:', error);
      throw new HttpException({
        status: HttpStatus.INTERNAL_SERVER_ERROR,
        error: 'Error creating invoice',
        details: error?.response?.data || error?.message || String(error),
      }, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Get()
  @Roles(Role.CLIENTE, Role.ADMIN, Role.ABOGADO)
  @UseGuards(JwtAuthGuard, RolesGuard)
  @ApiOperation({ 
    summary: 'Obtener todas las facturas',
    description: 'Devuelve todas las facturas (ADMIN y ABOGADO)'
  })
  @ApiResponse({ 
    status: 200, 
    description: 'Lista de facturas',
    schema: {
      type: 'array',
      items: {
        type: 'object',
        properties: {
          id: { type: 'string' },
          numero: { type: 'string' },
          fecha: { type: 'string', format: 'date' },
          emisorId: { type: 'string' },
          receptorId: { type: 'string' },
          importeTotal: { type: 'number' },
          estado: { type: 'string' },
          createdAt: { type: 'string', format: 'date-time' }
        }
      }
    }
  })
  @ApiResponse({ status: 401, description: 'No autorizado' })
  @ApiResponse({ status: 403, description: 'Rol insuficiente' })
  findAll(@Request() req) {
    const user = req.user;
    return this.invoicesService.findAll(user);
  }

  @Get(':id/pdf-qr')
  async getInvoicePdfWithQR(@Param('id') id: string, @Res() res: Response) {
    try {
      // Obtener la factura de la base de datos
      const invoice = await this.invoicesService.findOne(id);
      if (!invoice) {
        return res.status(404).send('Factura no encontrada');
      }
      console.log('[PDF-QR] Datos de la factura:', JSON.stringify(invoice, null, 2));
      // Generar el PDF con QR
      const pdfBuffer = await this.invoicesService.generateInvoicePdfWithQR(invoice);
      res.set({
        'Content-Type': 'application/pdf',
        'Content-Disposition': `attachment; filename="factura_${invoice.numeroFactura || id}.pdf"`
      });
      res.send(pdfBuffer);
    } catch (error) {
      console.error('[PDF-QR] Error al generar PDF con QR:', error);
      res.status(500).send({ error: (error as any).message || error.toString() });
    }
  }

  @Get(':id')
  @Roles(Role.CLIENTE, Role.ADMIN, Role.ABOGADO)
  @UseGuards(JwtAuthGuard, RolesGuard)
  @ApiOperation({ 
    summary: 'Obtener factura por ID',
    description: 'Devuelve una factura específica por su ID'
  })
  @ApiParam({ name: 'id', description: 'ID de la factura', type: 'string' })
  @ApiResponse({ 
    status: 200, 
    description: 'Factura encontrada',
    schema: {
      type: 'object',
      properties: {
        id: { type: 'string' },
        numero: { type: 'string' },
        fecha: { type: 'string', format: 'date' },
        emisorId: { type: 'string' },
        receptorId: { type: 'string' },
        importeTotal: { type: 'number' },
        estado: { type: 'string' },
        createdAt: { type: 'string', format: 'date-time' }
      }
    }
  })
  @ApiResponse({ status: 401, description: 'No autorizado' })
  @ApiResponse({ status: 403, description: 'Rol insuficiente' })
  @ApiResponse({ status: 404, description: 'Factura no encontrada' })
  findOne(@Param('id') id: string) {
    return this.invoicesService.findOne(id);
  }

  @Put(':id')
  @Roles(Role.CLIENTE, Role.ABOGADO)
  @UseGuards(JwtAuthGuard, RolesGuard)
  @UsePipes(new ValidationPipe({ whitelist: true }))
  @ApiOperation({ 
    summary: 'Actualizar factura',
    description: 'Actualiza una factura existente (solo ABOGADO)'
  })
  @ApiParam({ name: 'id', description: 'ID de la factura', type: 'string' })
  @ApiBody({ type: UpdateInvoiceDto })
  @ApiResponse({ 
    status: 200, 
    description: 'Factura actualizada exitosamente',
    schema: {
      type: 'object',
      properties: {
        id: { type: 'string' },
        numero: { type: 'string' },
        fecha: { type: 'string', format: 'date' },
        emisorId: { type: 'string' },
        receptorId: { type: 'string' },
        importeTotal: { type: 'number' },
        estado: { type: 'string' },
        updatedAt: { type: 'string', format: 'date-time' }
      }
    }
  })
  @ApiResponse({ status: 400, description: 'Datos inválidos' })
  @ApiResponse({ status: 401, description: 'No autorizado' })
  @ApiResponse({ status: 403, description: 'Rol insuficiente' })
  @ApiResponse({ status: 404, description: 'Factura no encontrada' })
  update(@Param('id') id: string, @Body() updateInvoiceDto: UpdateInvoiceDto) {
    return this.invoicesService.update(id, updateInvoiceDto);
  }

  @Delete(':id')
  @Roles(Role.CLIENTE, Role.ABOGADO)
  @UseGuards(JwtAuthGuard, RolesGuard)
  @ApiOperation({ 
    summary: 'Eliminar factura',
    description: 'Elimina una factura del sistema (solo ABOGADO)'
  })
  @ApiParam({ name: 'id', description: 'ID de la factura', type: 'string' })
  @ApiResponse({ 
    status: 200, 
    description: 'Factura eliminada exitosamente',
    schema: {
      type: 'object',
      properties: {
        message: { type: 'string', example: 'Factura eliminada exitosamente' }
      }
    }
  })
  @ApiResponse({ status: 401, description: 'No autorizado' })
  @ApiResponse({ status: 403, description: 'Rol insuficiente' })
  @ApiResponse({ status: 404, description: 'Factura no encontrada' })
  remove(@Param('id') id: string) {
    return this.invoicesService.remove(id);
  }

  @Post(':id/sign')
  @Roles(Role.CLIENTE, Role.ABOGADO)
  @UseGuards(JwtAuthGuard, RolesGuard)
  @ApiOperation({ 
    summary: 'Firmar factura',
    description: 'Firma digitalmente una factura (solo ABOGADO)'
  })
  @ApiParam({ name: 'id', description: 'ID de la factura', type: 'string' })
  @ApiBody({
    schema: {
      type: 'object',
      properties: {
        certPath: { type: 'string', description: 'Ruta al certificado (opcional)' },
        keyPath: { type: 'string', description: 'Ruta a la clave privada (opcional)' },
        certContent: { type: 'string', description: 'Contenido del certificado (opcional)' },
        keyContent: { type: 'string', description: 'Contenido de la clave privada (opcional)' }
      }
    }
  })
  @ApiResponse({ 
    status: 200, 
    description: 'Factura firmada exitosamente',
    schema: {
      type: 'object',
      properties: {
        id: { type: 'string' },
        signedXml: { type: 'string' },
        signatureValid: { type: 'boolean' }
      }
    }
  })
  @ApiResponse({ status: 401, description: 'No autorizado' })
  @ApiResponse({ status: 403, description: 'Rol insuficiente' })
  @ApiResponse({ status: 404, description: 'Factura no encontrada' })
  async sign(@Param('id') id: string, @Body() body: { certPath?: string; keyPath?: string; certContent?: string; keyContent?: string }) {
    // Si se recibe el contenido, usarlo; si no, usar rutas o variables de entorno
    const certContent = body.certContent;
    const keyContent = body.keyContent;
    const certPath = body.certPath || process.env.FACTURAE_CERT_PATH;
    const keyPath = body.keyPath || process.env.FACTURAE_KEY_PATH;
    return this.invoicesService.sign(id, certPath, keyPath, certContent, keyContent);
  }

  @Post('generate-xml')
  @Roles(Role.CLIENTE, Role.ABOGADO)
  @UseGuards(JwtAuthGuard, RolesGuard)
  @ApiOperation({ 
    summary: 'Generar XML de facturas',
    description: 'Genera XML para múltiples facturas (solo ABOGADO)'
  })
  @ApiBody({
    schema: {
      type: 'object',
      properties: {
        ids: {
          type: 'array',
          items: { type: 'string' },
          description: 'Array de IDs de facturas'
        }
      }
    }
  })
  @ApiResponse({ 
    status: 200, 
    description: 'XML generado exitosamente',
    schema: {
      type: 'object',
      properties: {
        xml: { type: 'string' },
        facturas: {
          type: 'array',
          items: { type: 'string' }
        }
      }
    }
  })
  @ApiResponse({ status: 401, description: 'No autorizado' })
  @ApiResponse({ status: 403, description: 'Rol insuficiente' })
  async generateXml(@Body() body: { ids: string[] }, @Request() req) {
    // Validar que el usuario es el emisor de cada factura
    return this.invoicesService.generateXmlForInvoices(body.ids, req.user.id);
  }

  @Post('upload-signed')
  @Roles(Role.CLIENTE, Role.ABOGADO)
  @UseGuards(JwtAuthGuard, RolesGuard)
  @ApiOperation({ 
    summary: 'Subir factura firmada',
    description: 'Sube una factura ya firmada (solo ABOGADO)'
  })
  @ApiBody({
    schema: {
      type: 'object',
      properties: {
        id: { type: 'string', description: 'ID de la factura' },
        signedXml: { type: 'string', description: 'XML firmado de la factura' }
      }
    }
  })
  @ApiResponse({ 
    status: 200, 
    description: 'Factura firmada guardada exitosamente',
    schema: {
      type: 'object',
      properties: {
        id: { type: 'string' },
        signedXml: { type: 'string' },
        signatureValid: { type: 'boolean' }
      }
    }
  })
  @ApiResponse({ status: 401, description: 'No autorizado' })
  @ApiResponse({ status: 403, description: 'Rol insuficiente' })
  @ApiResponse({ status: 404, description: 'Factura no encontrada' })
  async uploadSigned(@Body() body: { id: string; signedXml: string }, @Request() req) {
    // Validar que el usuario es el emisor de la factura
    return this.invoicesService.saveSignedXml(body.id, body.signedXml, req.user.id);
  }

  @Patch(':id/anular')
  @Roles(Role.CLIENTE, Role.ABOGADO)
  @UseGuards(JwtAuthGuard, RolesGuard)
  @ApiOperation({ 
    summary: 'Anular factura',
    description: 'Anula una factura con motivo (solo ABOGADO)'
  })
  @ApiParam({ name: 'id', description: 'ID de la factura', type: 'string' })
  @ApiBody({
    schema: {
      type: 'object',
      properties: {
        motivoAnulacion: { 
          type: 'string', 
          description: 'Motivo de la anulación (mínimo 3 caracteres)',
          minLength: 3
        }
      }
    }
  })
  @ApiResponse({ 
    status: 200, 
    description: 'Factura anulada exitosamente',
    schema: {
      type: 'object',
      properties: {
        id: { type: 'string' },
        estado: { type: 'string', example: 'ANULADA' },
        motivoAnulacion: { type: 'string' },
        fechaAnulacion: { type: 'string', format: 'date-time' }
      }
    }
  })
  @ApiResponse({ status: 400, description: 'Motivo de anulación inválido' })
  @ApiResponse({ status: 401, description: 'No autorizado' })
  @ApiResponse({ status: 403, description: 'Rol insuficiente' })
  @ApiResponse({ status: 404, description: 'Factura no encontrada' })
  async annul(@Param('id') id: string, @Body() body: { motivoAnulacion: string }, @Request() req) {
    if (!body.motivoAnulacion || body.motivoAnulacion.trim().length < 3) {
      throw new HttpException('El motivo de anulación es obligatorio y debe tener al menos 3 caracteres.', HttpStatus.BAD_REQUEST);
    }
    return this.invoicesService.annul(id, body.motivoAnulacion, req.user.id);
  }

  @Get('by-client/:clientId')
  @Roles(Role.CLIENTE, Role.ADMIN, Role.ABOGADO)
  @UseGuards(JwtAuthGuard, RolesGuard)
  @ApiBearerAuth('JWT-auth')
  @ApiOperation({ summary: 'Obtener facturas por cliente', description: 'Lista todas las facturas de un cliente específico (ADMIN y ABOGADO)' })
  @ApiParam({ name: 'clientId', description: 'ID del cliente' })
  @ApiResponse({ status: 200, description: 'Lista de facturas del cliente' })
  getInvoicesByClient(@Param('clientId') clientId: string) {
    return this.invoicesService.findByClientId(clientId);
  }

  @Post('by-client/:clientId')
  @Roles(Role.CLIENTE, Role.ADMIN, Role.ABOGADO)
  @UseGuards(JwtAuthGuard, RolesGuard)
  @ApiBearerAuth('JWT-auth')
  @ApiOperation({ summary: 'Crear factura para cliente', description: 'Crea una nueva factura para un cliente específico (ADMIN y ABOGADO)' })
  @ApiParam({ name: 'clientId', description: 'ID del cliente' })
  @ApiBody({ type: CreateInvoiceDto })
  @ApiResponse({ status: 201, description: 'Factura creada para el cliente' })
  createInvoiceForClient(@Param('clientId') clientId: string, @Body() createInvoiceDto: CreateInvoiceDto, @Request() req) {
    return this.invoicesService.createForClient(clientId, createInvoiceDto, req.user.id);
  }

  @Put('by-client/:clientId/:invoiceId')
  @Roles(Role.CLIENTE, Role.ADMIN, Role.ABOGADO)
  @UseGuards(JwtAuthGuard, RolesGuard)
  @ApiBearerAuth('JWT-auth')
  @ApiOperation({ summary: 'Actualizar factura de cliente', description: 'Actualiza una factura de un cliente específico (ADMIN y ABOGADO)' })
  @ApiParam({ name: 'clientId', description: 'ID del cliente' })
  @ApiParam({ name: 'invoiceId', description: 'ID de la factura' })
  @ApiBody({ type: UpdateInvoiceDto })
  @ApiResponse({ status: 200, description: 'Factura actualizada' })
  updateInvoiceForClient(@Param('clientId') clientId: string, @Param('invoiceId') invoiceId: string, @Body() updateInvoiceDto: UpdateInvoiceDto, @Request() req) {
    return this.invoicesService.updateForClient(clientId, invoiceId, updateInvoiceDto, req.user.id);
  }

  @Patch('by-client/:clientId/:invoiceId')
  @Roles(Role.CLIENTE, Role.ADMIN, Role.ABOGADO)
  @UseGuards(JwtAuthGuard, RolesGuard)
  @ApiBearerAuth('JWT-auth')
  @ApiOperation({ summary: 'Actualizar parcialmente factura de cliente', description: 'Actualiza parcialmente una factura de un cliente específico (ADMIN y ABOGADO)' })
  @ApiParam({ name: 'clientId', description: 'ID del cliente' })
  @ApiParam({ name: 'invoiceId', description: 'ID de la factura' })
  @ApiBody({ type: UpdateInvoiceDto })
  @ApiResponse({ status: 200, description: 'Factura actualizada parcialmente' })
  patchInvoiceForClient(@Param('clientId') clientId: string, @Param('invoiceId') invoiceId: string, @Body() updateInvoiceDto: UpdateInvoiceDto, @Request() req) {
    return this.invoicesService.patchForClient(clientId, invoiceId, updateInvoiceDto, req.user.id);
  }

  @Delete('by-client/:clientId/:invoiceId')
  @Roles(Role.CLIENTE,Role.ADMIN, Role.ABOGADO)
  @UseGuards(JwtAuthGuard, RolesGuard)
  @ApiBearerAuth('JWT-auth')
  @ApiOperation({ summary: 'Eliminar factura de cliente', description: 'Elimina una factura de un cliente específico (ADMIN y ABOGADO)' })
  @ApiParam({ name: 'clientId', description: 'ID del cliente' })
  @ApiParam({ name: 'invoiceId', description: 'ID de la factura' })
  @ApiResponse({ status: 200, description: 'Factura eliminada' })
  deleteInvoiceForClient(@Param('clientId') clientId: string, @Param('invoiceId') invoiceId: string, @Request() req) {
    return this.invoicesService.deleteForClient(clientId, invoiceId, req.user.id);
  }

  @Get('my')
  @Roles(Role.CLIENTE, Role.ADMIN, Role.ABOGADO)
  @UseGuards(JwtAuthGuard, RolesGuard)
  @ApiOperation({ summary: 'Obtener mis facturas', description: 'Devuelve las facturas del cliente autenticado, con filtros por abogado y fecha de pago.' })
  @ApiResponse({ status: 200, description: 'Lista de facturas del cliente' })
  async getMyInvoices(@Request() req) {
    console.log('--- getMyInvoices CALLED [DEBUG] ---');
    console.log('Headers:', req.headers);
    console.log('User:', req.user);
    console.log('Role:', req.user?.role);
    console.log('Query params:', req.query);
    if (!req.user) {
      throw new HttpException('No autenticado', HttpStatus.UNAUTHORIZED);
    }
    const { lawyerId, paymentDate } = req.query;
    return this.invoicesService.findForClient(req.user.id, lawyerId, paymentDate);
  }

  @Get('clients-with-invoices')
  @Roles(Role.ADMIN, Role.ABOGADO)
  @UseGuards(JwtAuthGuard, RolesGuard)
  @ApiOperation({ summary: 'Listar clientes con facturas', description: 'Devuelve la lista de clientes que tienen al menos una factura.' })
  @ApiResponse({ status: 200, description: 'Lista de clientes con facturas', schema: { type: 'array', items: { type: 'object', properties: { clientId: { type: 'string' }, name: { type: 'string' }, email: { type: 'string' }, facturaCount: { type: 'number' } } } } })
  async getClientsWithInvoices() {
    return this.invoicesService.getClientsWithInvoices();
  }
} 