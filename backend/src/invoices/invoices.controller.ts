import { Controller, Get, Post, Body, Param, Put, Delete, UseGuards, UsePipes, ValidationPipe, Request, HttpException, HttpStatus, Patch } from '@nestjs/common';
import { InvoicesService } from './invoices.service';
import { CreateInvoiceDto } from './dto/create-invoice.dto';
import { UpdateInvoiceDto } from './dto/update-invoice.dto';
import { JwtAuthGuard } from '../auth/guards/jwt-auth.guard';
import { RolesGuard } from '../auth/guards/roles.guard';
import { Roles } from '../auth/decorators/roles.decorator';
import { Role } from '@prisma/client';

@Controller('invoices')
@UseGuards(JwtAuthGuard, RolesGuard)
export class InvoicesController {
  constructor(private readonly invoicesService: InvoicesService) {}

  @Post()
  @Roles(Role.ABOGADO)
  @UsePipes(new ValidationPipe({ whitelist: true }))
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
  @Roles(Role.ADMIN, Role.ABOGADO)
  findAll() {
    return this.invoicesService.findAll();
  }

  @Get(':id')
  @Roles(Role.ADMIN, Role.ABOGADO)
  findOne(@Param('id') id: string) {
    return this.invoicesService.findOne(id);
  }

  @Put(':id')
  @Roles(Role.ABOGADO)
  @UsePipes(new ValidationPipe({ whitelist: true }))
  update(@Param('id') id: string, @Body() updateInvoiceDto: UpdateInvoiceDto) {
    return this.invoicesService.update(id, updateInvoiceDto);
  }

  @Delete(':id')
  @Roles(Role.ABOGADO)
  remove(@Param('id') id: string) {
    return this.invoicesService.remove(id);
  }

  @Post(':id/sign')
  @Roles(Role.ABOGADO)
  async sign(@Param('id') id: string, @Body() body: { certPath?: string; keyPath?: string; certContent?: string; keyContent?: string }) {
    // Si se recibe el contenido, usarlo; si no, usar rutas o variables de entorno
    const certContent = body.certContent;
    const keyContent = body.keyContent;
    const certPath = body.certPath || process.env.FACTURAE_CERT_PATH;
    const keyPath = body.keyPath || process.env.FACTURAE_KEY_PATH;
    return this.invoicesService.sign(id, certPath, keyPath, certContent, keyContent);
  }

  @Post('generate-xml')
  @Roles(Role.ABOGADO)
  async generateXml(@Body() body: { ids: string[] }, @Request() req) {
    // Validar que el usuario es el emisor de cada factura
    return this.invoicesService.generateXmlForInvoices(body.ids, req.user.id);
  }

  @Post('upload-signed')
  @Roles(Role.ABOGADO)
  async uploadSigned(@Body() body: { id: string; signedXml: string }, @Request() req) {
    // Validar que el usuario es el emisor de la factura
    return this.invoicesService.saveSignedXml(body.id, body.signedXml, req.user.id);
  }

  @Patch(':id/anular')
  @Roles(Role.ABOGADO)
  async annul(@Param('id') id: string, @Body() body: { motivoAnulacion: string }, @Request() req) {
    if (!body.motivoAnulacion || body.motivoAnulacion.trim().length < 3) {
      throw new HttpException('El motivo de anulación es obligatorio y debe tener al menos 3 caracteres.', HttpStatus.BAD_REQUEST);
    }
    return this.invoicesService.annul(id, body.motivoAnulacion, req.user.id);
  }
} 