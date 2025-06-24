import { IsString, IsNotEmpty, IsOptional, IsUUID } from 'class-validator';

export class CreateCaseDto {
  @IsString()
  @IsNotEmpty()
  title: string;

  @IsString()
  @IsOptional()
  description?: string;

  @IsUUID()
  @IsNotEmpty()
  clientId: string;

  @IsUUID()
  @IsNotEmpty()
  lawyerId: string;
} 