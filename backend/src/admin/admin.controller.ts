import { Controller, Get, Post, Put, Delete, Param, Body, UseGuards } from '@nestjs/common';
import { AdminService } from './admin.service';
import { JwtAuthGuard } from '../auth/guards/jwt-auth.guard';
import { RolesGuard } from '../auth/guards/roles.guard';
import { Roles } from '../auth/decorators/roles.decorator';

@Controller('admin')
@UseGuards(JwtAuthGuard, RolesGuard)
@Roles('ADMIN')
export class AdminController {
  constructor(private readonly adminService: AdminService) {}

  // Dashboard
  @Get('dashboard')
  async getDashboardStats() {
    return this.adminService.getDashboardStats();
  }

  // User Management
  @Get('users')
  async getAllUsers() {
    return this.adminService.getAllUsers();
  }

  @Get('users/:id')
  async getUserById(@Param('id') id: string) {
    return this.adminService.getUserById(id);
  }

  @Put('users/:id')
  async updateUser(@Param('id') id: string, @Body() data: any) {
    return this.adminService.updateUser(id, data);
  }

  @Delete('users/:id')
  async deleteUser(@Param('id') id: string) {
    return this.adminService.deleteUser(id);
  }

  // Case Management
  @Get('cases')
  async getAllCases() {
    return this.adminService.getAllCases();
  }

  @Get('cases/:id')
  async getCaseById(@Param('id') id: string) {
    return this.adminService.getCaseById(id);
  }

  @Put('cases/:id')
  async updateCase(@Param('id') id: string, @Body() data: any) {
    return this.adminService.updateCase(id, data);
  }

  @Delete('cases/:id')
  async deleteCase(@Param('id') id: string) {
    return this.adminService.deleteCase(id);
  }

  // Appointment Management
  @Get('appointments')
  async getAllAppointments() {
    return this.adminService.getAllAppointments();
  }

  @Get('appointments/:id')
  async getAppointmentById(@Param('id') id: string) {
    return this.adminService.getAppointmentById(id);
  }

  @Put('appointments/:id')
  async updateAppointment(@Param('id') id: string, @Body() data: any) {
    return this.adminService.updateAppointment(id, data);
  }

  @Delete('appointments/:id')
  async deleteAppointment(@Param('id') id: string) {
    return this.adminService.deleteAppointment(id);
  }

  // Task Management
  @Get('tasks')
  async getAllTasks() {
    return this.adminService.getAllTasks();
  }

  @Get('tasks/:id')
  async getTaskById(@Param('id') id: string) {
    return this.adminService.getTaskById(id);
  }

  @Put('tasks/:id')
  async updateTask(@Param('id') id: string, @Body() data: any) {
    return this.adminService.updateTask(id, data);
  }

  @Delete('tasks/:id')
  async deleteTask(@Param('id') id: string) {
    return this.adminService.deleteTask(id);
  }

  // Document Management
  @Get('documents')
  async getAllDocuments() {
    return this.adminService.getAllDocuments();
  }

  @Get('documents/:id')
  async getDocumentById(@Param('id') id: string) {
    return this.adminService.getDocumentById(id);
  }

  @Delete('documents/:id')
  async deleteDocument(@Param('id') id: string) {
    return this.adminService.deleteDocument(id);
  }

  // Reports
  @Get('reports')
  async getSystemReports() {
    return this.adminService.getSystemReports();
  }
} 