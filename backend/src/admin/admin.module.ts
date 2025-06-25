import { Module } from '@nestjs/common';
import { AdminController } from './admin.controller';
import { AdminService } from './admin.service';
import { LayoutsController } from './layouts.controller';
import { AdminLayoutsController } from './layouts.controller';
import { LayoutsService } from './layouts.service';
import { PrismaModule } from '../prisma/prisma.module';

@Module({
  imports: [PrismaModule],
  controllers: [AdminController, LayoutsController, AdminLayoutsController],
  providers: [AdminService, LayoutsService],
  exports: [AdminService, LayoutsService],
})
export class AdminModule {} 