import { Injectable, ForbiddenException, NotFoundException } from '@nestjs/common';
import { PrismaService } from '../prisma/prisma.service';
import { CreateAppointmentDto } from './dto/create-appointment.dto';

@Injectable()
export class AppointmentsService {
  constructor(private prisma: PrismaService) {}

  async findAll(user: any) {
    if (user.role === 'ADMIN') {
      // Admin ve todas las citas
      return this.prisma.appointment.findMany({
        include: {
          lawyer: { select: { id: true, name: true, email: true } },
          client: {
            include: {
              user: { select: { id: true, name: true, email: true } }
            }
          }
        },
        orderBy: { date: 'desc' }
      });
    } else if (user.role === 'ABOGADO') {
      // Abogado ve solo sus citas
      return this.prisma.appointment.findMany({
        where: { lawyerId: user.id },
        include: {
          lawyer: { select: { id: true, name: true, email: true } },
          client: {
            include: {
              user: { select: { id: true, name: true, email: true } }
            }
          }
        },
        orderBy: { date: 'desc' }
      });
    } else {
      // Cliente ve solo sus citas
      const client = await this.prisma.client.findUnique({
        where: { userId: user.id }
      });
      if (!client) throw new ForbiddenException('No eres cliente');
      return this.prisma.appointment.findMany({
        where: { clientId: client.id },
        include: {
          lawyer: { select: { id: true, name: true, email: true } },
          client: {
            include: {
              user: { select: { id: true, name: true, email: true } }
            }
          }
        },
        orderBy: { date: 'desc' }
      });
    }
  }

  async create(dto: CreateAppointmentDto, user: any) {
    // Solo clientes pueden crear citas
    if (user.role !== 'CLIENTE') throw new ForbiddenException('Solo los clientes pueden agendar citas');
    const client = await this.prisma.client.findUnique({ where: { userId: user.id } });
    if (!client) throw new ForbiddenException('No eres cliente');
    // Verificar que el abogado existe
    const lawyer = await this.prisma.user.findUnique({ where: { id: dto.lawyerId, role: 'ABOGADO' } });
    if (!lawyer) throw new NotFoundException('Abogado no encontrado');
    // Crear cita
    const appointment = await this.prisma.appointment.create({
      data: {
        clientId: client.id,
        lawyerId: dto.lawyerId,
        date: new Date(dto.date),
        location: dto.location || null,
        notes: dto.notes || null,
      },
      include: {
        lawyer: { select: { id: true, name: true, email: true } },
        client: {
          include: {
            user: { select: { id: true, name: true, email: true } }
          }
        }
      }
    });
    return appointment;
  }
} 