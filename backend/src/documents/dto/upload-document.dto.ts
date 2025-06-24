import { IsString, IsNotEmpty, IsUUID, IsOptional } from 'class-validator';

export class UploadDocumentDto {
  @IsUUID()
  @IsNotEmpty()
  expedienteId: string;

  @IsString()
  @IsOptional()
  description?: string;
} 