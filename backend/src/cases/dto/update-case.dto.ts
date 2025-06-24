import { IsString, IsOptional, IsUUID } from 'class-validator';

export class UpdateCaseDto {
  @IsString()
  @IsOptional()
  title?: string;

  @IsString()
  @IsOptional()
  description?: string;

  @IsUUID()
  @IsOptional()
  clientId?: string;

  @IsUUID()
  @IsOptional()
  lawyerId?: string;
} 