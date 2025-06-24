import { Controller, Get, Post, Put, Delete, Body, Param, UseGuards } from '@nestjs/common';
import { ParametrosService } from './parametros.service';
import { JwtAuthGuard } from '../auth/guards/jwt-auth.guard';
import { RolesGuard } from '../auth/guards/roles.guard';
import { Roles } from '../auth/decorators/roles.decorator';
import { Role } from '@prisma/client';

@Controller('parametros')
@UseGuards(JwtAuthGuard, RolesGuard)
@Roles('ADMIN')
export class ParametrosController {
  constructor(private readonly parametrosService: ParametrosService) {}

  @Get()
  findAll() {
    return this.parametrosService.findAll();
  }

  @Get(':id')
  findOne(@Param('id') id: string) {
    return this.parametrosService.findOne(id);
  }

  @Post()
  create(@Body() data: { clave: string; valor: string; etiqueta: string; tipo: string }) {
    return this.parametrosService.create(data);
  }

  @Put(':id')
  update(@Param('id') id: string, @Body() data: { valor?: string; etiqueta?: string; tipo?: string }) {
    return this.parametrosService.update(id, data);
  }

  @Delete(':id')
  remove(@Param('id') id: string) {
    return this.parametrosService.remove(id);
  }
} 