import {
  Controller,
  Get,
  Post,
  Body,
  Patch,
  Param,
  Delete,
  UseGuards,
  Request,
  Query,
  ParseEnumPipe
} from '@nestjs/common';
import { CasesService } from './cases.service';
import { CreateCaseDto } from './dto/create-case.dto';
import { UpdateCaseDto } from './dto/update-case.dto';
import { JwtAuthGuard } from '../auth/guards/jwt-auth.guard';
import { RolesGuard } from '../auth/guards/roles.guard';
import { Roles } from '../auth/decorators/roles.decorator';
import { Role, Status } from '@prisma/client';

@Controller('cases')
@UseGuards(JwtAuthGuard, RolesGuard)
export class CasesController {
  constructor(private readonly casesService: CasesService) { }

  @Post()
  @Roles(Role.ADMIN, Role.ABOGADO)
  create(@Body() createCaseDto: CreateCaseDto, @Request() req) {
    return this.casesService.create(createCaseDto, req.user.id);
  }

  /* @Get()
  findAll(@Request() req) {
    return this.casesService.findAll(req.user.id, req.user.role);
  } */

  @Get()
  @Roles(Role.ADMIN, Role.ABOGADO, Role.CLIENTE) // Añade explicitamente todos los roles
  findAll(@Request() req) {
    return this.casesService.findAll(req.user.id, req.user.role);
  }

  @Get('stats')
  getStats(@Request() req) {
    return this.casesService.getCasesStats(req.user.id, req.user.role);
  }

  @Get('status/:status')
  getCasesByStatus(
    @Param('status', new ParseEnumPipe(Status)) status: Status,
    @Request() req
  ) {
    return this.casesService.getCasesByStatus(status, req.user.id, req.user.role);
  }

  @Get(':id')
  findOne(@Param('id') id: string, @Request() req) {
    return this.casesService.findOne(id, req.user.id, req.user.role);
  }

  @Patch(':id')
  @Roles(Role.ADMIN, Role.ABOGADO)
  update(
    @Param('id') id: string,
    @Body() updateCaseDto: UpdateCaseDto,
    @Request() req
  ) {
    return this.casesService.update(id, updateCaseDto, req.user.id, req.user.role);
  }

  @Patch(':id/status')
  @Roles(Role.ADMIN, Role.ABOGADO)
  updateStatus(
    @Param('id') id: string,
    @Body('status', new ParseEnumPipe(Status)) status: Status,
    @Request() req
  ) {
    return this.casesService.updateStatus(id, status, req.user.id, req.user.role);
  }

  @Delete(':id')
  @Roles(Role.ADMIN)
  remove(@Param('id') id: string, @Request() req) {
    return this.casesService.remove(id, req.user.id, req.user.role);
  }
} 