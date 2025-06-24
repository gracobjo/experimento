import {
  Controller,
  Get,
  Post,
  Body,
  Param,
  Delete,
  UseGuards,
  Request,
  UseInterceptors,
  UploadedFile,
  Res,
  ParseFilePipe,
  MaxFileSizeValidator,
  FileTypeValidator,
} from '@nestjs/common';
import { FileInterceptor } from '@nestjs/platform-express';
import { Response } from 'express';
import { DocumentsService } from './documents.service';
import { UploadDocumentDto } from './dto/upload-document.dto';
import { JwtAuthGuard } from '../auth/guards/jwt-auth.guard';
import { RolesGuard } from '../auth/guards/roles.guard';
import { Roles } from '../auth/decorators/roles.decorator';
import { Role } from '@prisma/client';

@Controller('documents')
@UseGuards(JwtAuthGuard, RolesGuard)
export class DocumentsController {
  constructor(private readonly documentsService: DocumentsService) {}

  @Post('upload')
  @Roles(Role.ADMIN, Role.ABOGADO, Role.CLIENTE)
  @UseInterceptors(FileInterceptor('file'))
  async uploadDocument(
    @UploadedFile(
      new ParseFilePipe({
        validators: [
          new MaxFileSizeValidator({ maxSize: 5 * 1024 * 1024 }), // 5MB
          new FileTypeValidator({ fileType: '.(pdf|txt|csv|doc|docx|jpg|jpeg|png|gif|webp)' }),
        ],
      }),
    )
    file: any,
    @Body() uploadDocumentDto: UploadDocumentDto,
    @Request() req,
  ) {
    return this.documentsService.uploadDocument(
      file,
      uploadDocumentDto,
      req.user.id,
      req.user.role,
    );
  }

  @Get()
  findAll(@Request() req) {
    return this.documentsService.findAll(req.user.id, req.user.role);
  }

  @Get('stats')
  getStats(@Request() req) {
    return this.documentsService.getDocumentsStats(req.user.id, req.user.role);
  }

  @Get('expediente/:expedienteId')
  findByExpediente(
    @Param('expedienteId') expedienteId: string,
    @Request() req,
  ) {
    return this.documentsService.findByExpediente(
      expedienteId,
      req.user.id,
      req.user.role,
    );
  }

  @Get(':id')
  findOne(@Param('id') id: string, @Request() req) {
    return this.documentsService.findOne(id, req.user.id, req.user.role);
  }

  @Get(':id/download')
  async downloadDocument(
    @Param('id') id: string,
    @Request() req,
    @Res() res: Response,
  ) {
    const document = await this.documentsService.findOne(
      id,
      req.user.id,
      req.user.role,
    );

    const fileStream = this.documentsService.getFileStream(document.filename);

    res.setHeader('Content-Type', document.mimeType);
    res.setHeader(
      'Content-Disposition',
      `attachment; filename="${document.originalName}"`,
    );

    fileStream.pipe(res);
  }

  @Delete(':id')
  @Roles(Role.ADMIN, Role.ABOGADO)
  remove(@Param('id') id: string, @Request() req) {
    return this.documentsService.remove(id, req.user.id, req.user.role);
  }
} 