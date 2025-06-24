import { Injectable } from '@nestjs/common';
import { PrismaService } from '../prisma/prisma.service';
import { CreateInvoiceDto } from './dto/create-invoice.dto';
import { UpdateInvoiceDto } from './dto/update-invoice.dto';
import { generateFacturaeXML } from './facturae-xml.util';
import { signFacturaeXML } from './xades-sign.util';
import * as fs from 'fs';

@Injectable()
export class InvoicesService {
  constructor(private prisma: PrismaService) {}

  async create(data: CreateInvoiceDto) {
    console.log('DATA RECIBIDA EN SERVICE:', JSON.stringify(data, null, 2));
    const { items, expedienteId, ...invoiceData } = data;
    console.log('items:', items);
    console.log('expedienteId:', expedienteId);
    console.log('invoiceData:', invoiceData);

    // Generar numeroFactura si no viene en la petición
    let numeroFactura = invoiceData.numeroFactura;
    if (!numeroFactura) {
      const year = new Date().getFullYear();
      // Buscar el último número de factura de este año
      const lastInvoice = await this.prisma.invoice.findFirst({
        where: {
          numeroFactura: { startsWith: `fac-${year}-` },
        },
        orderBy: { createdAt: 'desc' },
      });
      let nextNumber = 1;
      if (lastInvoice && lastInvoice.numeroFactura) {
        const match = lastInvoice.numeroFactura.match(/fac-\d{4}-(\d{4})/);
        if (match) {
          nextNumber = parseInt(match[1], 10) + 1;
        }
      }
      numeroFactura = `fac-${year}-${nextNumber.toString().padStart(4, '0')}`;
    }

    // Construir el objeto data para Prisma, solo incluyendo expedienteId si existe
    const prismaData: any = {
      ...invoiceData,
      numeroFactura,
      fechaFactura: new Date(invoiceData.fechaFactura),
      fechaOperacion: new Date(invoiceData.fechaOperacion),
      items: { create: items },
      estado: 'emitida',
    };
    if (expedienteId) {
      prismaData.expedienteId = expedienteId;
    }
    // Crear la factura y sus items
    const invoice = await this.prisma.invoice.create({
      data: prismaData,
      include: { items: true, emisor: true, receptor: true, expediente: true },
    });
    // Generar el XML Facturae
    const xml = generateFacturaeXML(invoice);
    // Guardar el XML en la base de datos
    await this.prisma.invoice.update({
      where: { id: invoice.id },
      data: { xml },
    });
    // Devolver la factura con el XML
    return { ...invoice, xml };
  }

  async findAll() {
    return this.prisma.invoice.findMany({ include: { items: true, emisor: true, receptor: true, expediente: true } });
  }

  async findOne(id: string) {
    return this.prisma.invoice.findUnique({ where: { id }, include: { items: true, emisor: true, receptor: true, expediente: true } });
  }

  async update(id: string, data: UpdateInvoiceDto) {
    const { items, ...invoiceData } = data;
    // Si hay items, los actualizamos (borrado y recreación simplificada)
    if (items) {
      await this.prisma.invoiceItem.deleteMany({ where: { invoiceId: id } });
    }
    return this.prisma.invoice.update({
      where: { id },
      data: {
        ...invoiceData,
        ...(items ? { items: { create: items } } : {}),
      },
      include: { items: true, emisor: true, receptor: true, expediente: true },
    });
  }

  async remove(id: string) {
    return this.prisma.invoice.delete({ where: { id } });
  }

  async sign(id: string, certPath?: string, keyPath?: string, certContent?: string, keyContent?: string) {
    // Obtener la factura y su XML
    const invoice = await this.prisma.invoice.findUnique({ where: { id } });
    if (!invoice || !invoice.xml) {
      throw new Error('Factura o XML no encontrado');
    }
    // Obtener el contenido del certificado y la clave
    let cert = certContent;
    let key = keyContent;
    if (!cert && certPath) {
      cert = fs.readFileSync(certPath, 'utf8');
    }
    if (!key && keyPath) {
      key = fs.readFileSync(keyPath, 'utf8');
    }
    if (!cert || !key) {
      throw new Error('Certificado o clave no proporcionados');
    }
    // Firmar el XML
    const xmlFirmado = await signFacturaeXML(invoice.xml, cert, key);
    // Guardar el XML firmado
    await this.prisma.invoice.update({
      where: { id },
      data: { xmlFirmado },
    });
    return { ...invoice, xmlFirmado };
  }

  async generateXmlForInvoices(ids: string[], userId: string) {
    const result = [];
    for (const id of ids) {
      const invoice = await this.prisma.invoice.findUnique({ where: { id }, include: { emisor: true, items: true, receptor: true, expediente: true } });
      if (!invoice) throw new Error(`Factura ${id} no encontrada`);
      if (invoice.emisorId !== userId) throw new Error(`No autorizado para la factura ${id}`);
      // Generar XML si no existe
      let xml = invoice.xml;
      if (!xml) {
        xml = generateFacturaeXML(invoice);
        await this.prisma.invoice.update({ where: { id }, data: { xml } });
      }
      result.push({ id, xml });
    }
    return result;
  }

  async saveSignedXml(id: string, signedXml: string, userId: string) {
    const invoice = await this.prisma.invoice.findUnique({ where: { id } });
    if (!invoice) throw new Error('Factura no encontrada');
    if (invoice.emisorId !== userId) throw new Error('No autorizado para firmar esta factura');
    await this.prisma.invoice.update({ where: { id }, data: { xmlFirmado: signedXml } });
    return { id, status: 'signed' };
  }
} 