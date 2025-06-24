import { IsString, IsNumber, IsOptional, IsDateString } from 'class-validator';

export class CreateProvisionFondosDto {
  @IsString()
  clientId: string;

  @IsOptional()
  @IsString()
  expedienteId?: string;

  @IsOptional()
  @IsString()
  invoiceId?: string;

  @IsNumber()
  amount: number;

  @IsOptional()
  @IsDateString()
  date?: string;

  @IsOptional()
  @IsString()
  description?: string;
} 