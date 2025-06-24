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
} from '@nestjs/common';
import { UsersService } from './users.service';
import { CreateUserDto } from './dto/create-user.dto';
import { UpdateUserDto } from './dto/update-user.dto';
import { JwtAuthGuard } from '../auth/guards/jwt-auth.guard';
import { RolesGuard } from '../auth/guards/roles.guard';
import { Roles } from '../auth/decorators/roles.decorator';
import { Role } from '@prisma/client';

@Controller('users')
@UseGuards(JwtAuthGuard, RolesGuard)
export class UsersController {
  constructor(private readonly usersService: UsersService) {}

  @Post()
  @Roles(Role.ADMIN, Role.ABOGADO)
  create(@Body() createUserDto: CreateUserDto) {
    return this.usersService.create(createUserDto);
  }

  @Get()
  @Roles(Role.ADMIN)
  findAll() {
    return this.usersService.findAll();
  }

  @Get('clients')
  @Roles(Role.ADMIN, Role.ABOGADO)
  findClients() {
    return this.usersService.findClients();
  }

  @Get('clients/my')
  @Roles(Role.ABOGADO)
  findMyClients(@Request() req) {
    return this.usersService.findClientsByLawyer(req.user.id);
  }

  @Get('clients/stats')
  @Roles(Role.ADMIN, Role.ABOGADO)
  getClientStats(@Request() req) {
    const lawyerId = req.user.role === 'ABOGADO' ? req.user.id : undefined;
    return this.usersService.getClientStats(lawyerId);
  }

  @Get('clients/report')
  @Roles(Role.ADMIN, Role.ABOGADO)
  getClientReport(@Request() req) {
    const lawyerId = req.user.role === 'ABOGADO' ? req.user.id : undefined;
    return this.usersService.getClientReport(lawyerId);
  }

  @Get('lawyers')
  @Roles(Role.ADMIN, Role.ABOGADO, Role.CLIENTE)
  findLawyers() {
    return this.usersService.findLawyers();
  }

  @Get(':id')
  @Roles(Role.ADMIN)
  findOne(@Param('id') id: string) {
    return this.usersService.findOne(id);
  }

  @Patch(':id')
  @Roles(Role.ADMIN)
  update(@Param('id') id: string, @Body() updateUserDto: UpdateUserDto) {
    return this.usersService.update(id, updateUserDto);
  }

  @Delete(':id')
  @Roles(Role.ADMIN)
  remove(@Param('id') id: string) {
    return this.usersService.remove(id);
  }
} 