import { Module } from '@nestjs/common';
import { InvoicesService } from './invoices.service';
import { InvoicesController } from './invoices.controller';
import { FacturaeController } from './facturae.controller';
import { FacturaeService } from './facturae.service';
import { ExternalSystemsController } from './external-systems.controller';
import { ExternalSystemsService } from './external-systems.service';
 
@Module({
  controllers: [InvoicesController, FacturaeController, ExternalSystemsController],
  providers: [InvoicesService, FacturaeService, ExternalSystemsService],
})
export class InvoicesModule {} 