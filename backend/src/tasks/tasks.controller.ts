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
  Query
} from '@nestjs/common';
import { TasksService } from './tasks.service';
import { CreateTaskDto, TaskStatus } from './dto/create-task.dto';
import { UpdateTaskDto } from './dto/update-task.dto';
import { JwtAuthGuard } from '../auth/guards/jwt-auth.guard';

@UseGuards(JwtAuthGuard)
@Controller('tasks')
export class TasksController {
  constructor(private readonly tasksService: TasksService) {}

  @Post()
  async create(@Body() createTaskDto: CreateTaskDto, @Request() req) {
    return this.tasksService.create(createTaskDto, req.user.id);
  }

  @Get()
  async findAll(@Request() req, @Query('status') status?: TaskStatus) {
    if (status) {
      return this.tasksService.getTasksByStatus(status, req.user.id, req.user.role);
    }
    return this.tasksService.findAll(req.user.id, req.user.role);
  }

  @Get('stats')
  async getStats(@Request() req) {
    return this.tasksService.getTasksStats(req.user.id, req.user.role);
  }

  @Get('upcoming')
  async getUpcomingTasks(@Request() req, @Query('days') days?: string) {
    const daysNumber = days ? parseInt(days) : 7;
    return this.tasksService.getUpcomingTasks(req.user.id, req.user.role, daysNumber);
  }

  @Get(':id')
  async findOne(@Param('id') id: string, @Request() req) {
    return this.tasksService.findOne(id, req.user.id, req.user.role);
  }

  @Patch(':id')
  async update(@Param('id') id: string, @Body() updateTaskDto: UpdateTaskDto, @Request() req) {
    return this.tasksService.update(id, updateTaskDto, req.user.id, req.user.role);
  }

  @Patch(':id/status')
  async updateStatus(@Param('id') id: string, @Body() body: { status: TaskStatus }, @Request() req) {
    return this.tasksService.updateStatus(id, body.status, req.user.id, req.user.role);
  }

  @Delete(':id')
  async remove(@Param('id') id: string, @Request() req) {
    return this.tasksService.remove(id, req.user.id, req.user.role);
  }
} 