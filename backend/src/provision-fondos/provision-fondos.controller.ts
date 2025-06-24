import { Controller, Get, Post, Body, Param, Query, Patch } from '@nestjs/common';
import { ProvisionFondosService } from './provision-fondos.service';
import { CreateProvisionFondosDto } from './dto/create-provision-fondos.dto';
// Si tienes el DTO, descomenta la siguiente línea:
// import { LinkToInvoiceDto } from './dto/link-to-invoice.dto';

@Controller('provision-fondos')
export class ProvisionFondosController {
  constructor(private readonly service: ProvisionFondosService) {}

  @Get()
  findAll(
    @Query('clientId') clientId?: string,
    @Query('expedienteId') expedienteId?: string,
    @Query('invoiceId') invoiceId?: string,
    @Query('soloPendientes') soloPendientes?: string
  ) {
    return this.service.findAll({
      clientId,
      expedienteId,
      invoiceId,
      soloPendientes: soloPendientes === 'true',
    });
  }

  @Get(':id')
  findOne(@Param('id') id: string) {
    return this.service.findOne(id);
  }

  @Post()
  create(@Body() dto: CreateProvisionFondosDto) {
    return this.service.create(dto);
  }

  @Patch('link-to-invoice')
  linkToInvoice(@Body() dto: { provisionId: string; invoiceId: string }) {
    return this.service.linkToInvoice(dto.provisionId, dto.invoiceId);
  }
} 