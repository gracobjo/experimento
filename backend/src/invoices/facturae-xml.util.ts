import { create } from 'xmlbuilder2';

export function generateFacturaeXML(invoice: any) {
  const items = Array.isArray(invoice.items) ? invoice.items.filter(Boolean) : [];
  return create({ version: '1.0', encoding: 'UTF-8' })
    .ele('Factura', { xmlns: 'http://www.facturae.es/Facturae/2009/v3.2/Facturae' })
      .ele('Cabecera')
        .ele('NumeroFactura').txt(invoice.numeroFactura).up()
        .ele('FechaFactura').txt(invoice.fechaFactura.toISOString().slice(0, 10)).up()
        .ele('TipoFactura').txt(invoice.tipoFactura).up()
      .up()
      .ele('Emisor')
        .ele('NIF').txt(invoice.emisor?.dni || invoice.emisor?.email || '').up()
        .ele('Nombre').txt(invoice.emisor?.name || '').up()
      .up()
      .ele('Receptor')
        .ele('NIF').txt(invoice.receptor?.dni || invoice.receptor?.email || '').up()
        .ele('Nombre').txt(invoice.receptor?.name || '').up()
      .up()
      .ele('DatosFactura')
        .ele('ImporteTotal').txt(invoice.importeTotal.toFixed(2)).up()
        .ele('IVA')
          .ele('Tipo').txt(invoice.tipoIVA.toString()).up()
          .ele('BaseImponible').txt(invoice.baseImponible.toFixed(2)).up()
          .ele('Cuota').txt(invoice.cuotaIVA.toFixed(2)).up()
        .up()
      .up()
      .ele('Items')
        .import(
          items.length > 0
            ? items.map((item: any) => ({
                Item: {
                  Description: item.description || '',
                  Quantity: item.quantity ?? 0,
                  UnitPrice: item.unitPrice ?? 0,
                  Total: item.total ?? 0,
                }
              }))
            : []
        )
      .up()
    .up()
    .end({ prettyPrint: true });
} 