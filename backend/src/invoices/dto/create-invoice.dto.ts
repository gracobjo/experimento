import { IsString, IsNotEmpty, IsDateString, IsNumber, IsArray, ValidateNested, IsOptional } from 'class-validator';
import { Type } from 'class-transformer';

class InvoiceItemDto {
  @IsString()
  @IsNotEmpty()
  description: string;

  @IsNumber()
  quantity: number;

  @IsNumber()
  unitPrice: number;

  @IsNumber()
  total: number;
}

export class CreateInvoiceDto {
  @IsString()
  @IsOptional()
  numeroFactura?: string;

  @IsDateString()
  fechaFactura: string;

  @IsString()
  @IsNotEmpty()
  tipoFactura: string;

  @IsString()
  @IsNotEmpty()
  emisorId: string;

  @IsString()
  @IsNotEmpty()
  receptorId: string;

  @IsOptional()
  @IsString()
  expedienteId?: string;

  @IsNumber()
  importeTotal: number;

  @IsNumber()
  baseImponible: number;

  @IsNumber()
  cuotaIVA: number;

  @IsNumber()
  tipoIVA: number;

  @IsString()
  @IsNotEmpty()
  regimenIvaEmisor: string;

  @IsString()
  @IsNotEmpty()
  claveOperacion: string;

  @IsString()
  @IsNotEmpty()
  metodoPago: string;

  @IsDateString()
  fechaOperacion: string;

  @IsArray()
  @ValidateNested({ each: true })
  @Type(() => InvoiceItemDto)
  items: InvoiceItemDto[];
} 