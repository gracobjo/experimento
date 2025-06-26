import { IsString, IsNotEmpty, IsDateString, IsNumber, IsArray, ValidateNested, IsOptional, IsBoolean } from 'class-validator';
import { Type, Transform } from 'class-transformer';

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
  @IsOptional()
  fechaFactura?: string;

  @IsString()
  @IsNotEmpty()
  tipoFactura: string;

  @IsString()
  @IsOptional()
  emisorId?: string;

  @IsString()
  @IsNotEmpty()
  receptorId: string;

  @IsOptional()
  @IsString()
  expedienteId?: string;

  @IsOptional()
  importeTotal?: any;

  @IsOptional()
  baseImponible?: any;

  @IsOptional()
  cuotaIVA?: any;

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

  @IsOptional()
  @IsString()
  motivoAnulacion?: string;

  @IsOptional()
  @IsArray()
  @IsString({ each: true })
  provisionIds?: string[];

  @IsOptional()
  @Transform(({ value }) => {
    if (value === '' || value === null || value === undefined) return 0;
    const num = Number(value);
    return isNaN(num) ? 0 : num;
  })
  descuento?: number;

  @IsOptional()
  @Transform(({ value }) => {
    if (value === '' || value === null || value === undefined) return 0;
    const num = Number(value);
    return isNaN(num) ? 0 : num;
  })
  retencion?: number;

  @IsOptional()
  @IsBoolean()
  aplicarIVA?: boolean;

  @IsArray()
  @ValidateNested({ each: true })
  @Type(() => InvoiceItemDto)
  items: InvoiceItemDto[];
} 