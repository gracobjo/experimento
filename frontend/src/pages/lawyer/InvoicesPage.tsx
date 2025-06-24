import React, { useEffect, useState } from 'react';
import { getInvoices, createInvoice, signInvoice } from '../../api/invoices';
import { getClients } from '../../api/clients';
import { useAuth } from '../../context/AuthContext';
import { getPendingProvisions } from '../../api/provisionFondos';
import api from '../../api/axios';

const initialForm = {
  numeroFactura: '',
  fechaFactura: '',
  tipoFactura: 'F',
  receptorId: '',
  importeTotal: 0,
  baseImponible: 0,
  cuotaIVA: 0,
  tipoIVA: 21,
  regimenIvaEmisor: 'General',
  claveOperacion: '01',
  metodoPago: 'TRANSFERENCIA',
  fechaOperacion: '',
  items: [{ description: '', quantity: 1, unitPrice: 0, total: 0 }],
};

const InvoicesPage = () => {
  const { user } = useAuth();
  const token = localStorage.getItem('token');
  const [invoices, setInvoices] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showModal, setShowModal] = useState(false);
  const [form, setForm] = useState<any>(initialForm);
  const [signingId, setSigningId] = useState<string | null>(null);
  const [certPath, setCertPath] = useState('');
  const [keyPath, setKeyPath] = useState('');
  const [clients, setClients] = useState<any[]>([]);
  const [formError, setFormError] = useState<string | null>(null);
  const [successMsg, setSuccessMsg] = useState<string | null>(null);
  const [creating, setCreating] = useState(false);
  const [certFile, setCertFile] = useState<File | null>(null);
  const [keyFile, setKeyFile] = useState<File | null>(null);
  const [selected, setSelected] = useState<string[]>([]);
  const [signStatus, setSignStatus] = useState<string>('');
  const [pendingProvision, setPendingProvision] = useState<any>(null);
  const [expedientesCliente, setExpedientesCliente] = useState<any[]>([]);
  const [filteredProvisions, setFilteredProvisions] = useState<any[]>([]);

  // Encuentra el perfil de cliente seleccionado
  const selectedClient = clients.find((c: any) => c.user.id === form.receptorId);
  const clientProfileId = selectedClient?.id;

  useEffect(() => {
    fetchInvoices();
    fetchClients();
    // eslint-disable-next-line
  }, []);

  useEffect(() => {
    const fetchPendingProvision = async () => {
      if (form.receptorId && form.expedienteId) {
        try {
          const token = localStorage.getItem('token');
          const res = await getPendingProvisions(token);
          console.log('Provisiones recibidas:', res);
          console.log('Buscando clientProfileId:', clientProfileId, 'expedienteId:', form.expedienteId);
          const provisions = res.filter(
            (p: any) =>
              p.clientId === clientProfileId &&
              p.expedienteId === form.expedienteId &&
              !p.invoiceId
          );
          console.log('Provisiones filtradas:', provisions);
          if (provisions.length > 0) {
            setPendingProvision(provisions[0]);
            setFilteredProvisions(provisions);
            // Añade el concepto si no existe
            setForm((prev: any) => {
              const exists = prev.items.some((i: any) => i.description === 'Provisión de fondos');
              if (exists) return prev;
              const totalProvision = provisions.reduce((sum: number, p: any) => sum + p.amount, 0);
              return {
                ...prev,
                items: [
                  ...prev.items,
                  { description: 'Provisión de fondos', quantity: 1, unitPrice: -totalProvision, total: -totalProvision }
                ]
              };
            });
          } else {
            setPendingProvision(null);
            setFilteredProvisions([]);
          }
        } catch {
          setPendingProvision(null);
          setFilteredProvisions([]);
        }
      } else {
        setPendingProvision(null);
        setFilteredProvisions([]);
      }
    };
    fetchPendingProvision();
  }, [form.receptorId, form.expedienteId, clientProfileId]);

  const fetchInvoices = async () => {
    setLoading(true);
    try {
      if (!token) {
        throw new Error('No token available');
      }
      const data = await getInvoices(token);
      setInvoices(data);
    } catch (err) {
      setError('Error al cargar las facturas');
    } finally {
      setLoading(false);
    }
  };

  const fetchClients = async () => {
    try {
      if (!token) {
        throw new Error('No token available');
      }
      const data = await getClients(token);
      setClients(data);
    } catch {
      setClients([]);
    }
  };

  const handleDownload = (xml: string, name: string) => {
    const blob = new Blob([xml], { type: 'application/xml' });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = name;
    a.click();
    window.URL.revokeObjectURL(url);
  };

  const handleOpenModal = () => {
    setForm(initialForm);
    setShowModal(true);
  };

  const handleFormChange = (e: any) => {
    const { name, value } = e.target;
    setForm((prev: any) => ({ ...prev, [name]: value }));
  };

  const handleClientChange = async (e: any) => {
    handleFormChange(e);
    const clientId = e.target.value;
    setForm((prev: any) => ({ ...prev, expedienteId: '' })); // Resetea expediente
    if (clientId) {
      try {
        const token = localStorage.getItem('token');
        const res = await api.get('/cases', { headers: { Authorization: `Bearer ${token}` } });
        const expedientes = res.data.filter((exp: any) => exp.client?.user?.id === clientId || exp.clientId === clientId);
        setExpedientesCliente(expedientes);
      } catch {
        setExpedientesCliente([]);
      }
    } else {
      setExpedientesCliente([]);
    }
  };

  const handleItemChange = (idx: number, field: string, value: any) => {
    setForm((prev: any) => {
      const items = [...prev.items];
      items[idx][field] = value;
      items[idx].total = items[idx].quantity * items[idx].unitPrice;
      return { ...prev, items };
    });
  };

  const handleAddItem = () => {
    setForm((prev: any) => ({ ...prev, items: [...prev.items, { description: '', quantity: 1, unitPrice: 0, total: 0 }] }));
  };

  const handleCreate = async (e: any) => {
    e.preventDefault();
    setFormError(null);
    setSuccessMsg(null);
    setCreating(true);
    // Validaciones mínimas
    if (!form.fechaFactura || !form.receptorId || !form.fechaOperacion || !form.items.length) {
      setFormError('Completa todos los campos obligatorios.');
      setCreating(false);
      return;
    }
    if (form.items.some((i: any) => !i.description || i.quantity <= 0 || i.unitPrice < 0)) {
      setFormError('Todos los conceptos deben tener descripción, cantidad y precio válidos.');
      setCreating(false);
      return;
    }
    try {
      // Calcula totales
      const baseImponible = form.items.reduce((sum: number, i: any) => sum + Number(i.total), 0);
      const cuotaIVA = baseImponible * (form.tipoIVA / 100);
      const importeTotal = baseImponible + cuotaIVA;
      const { numeroFactura, ...formWithoutNumero } = form;
      const invoiceData = {
        ...formWithoutNumero,
        emisorId: user?.id,
        items: form.items.map((item: any) => ({
          ...item,
          quantity: Number(item.quantity),
          unitPrice: Number(item.unitPrice),
          total: Number(item.quantity) * Number(item.unitPrice)
        })),
        tipoIVA: Number(form.tipoIVA),
        importeTotal,
        baseImponible,
        cuotaIVA,
      };
      await createInvoice(invoiceData, token!);
      setShowModal(false);
      setSuccessMsg('Factura creada correctamente.');
      fetchInvoices();
    } catch (err) {
      setFormError('Error al crear la factura');
    } finally {
      setCreating(false);
    }
  };

  const handleSign = async (id: string) => {
    let certContent = certPath;
    let keyContent = keyPath;
    if (certFile) {
      certContent = await certFile.text();
    }
    if (keyFile) {
      keyContent = await keyFile.text();
    }
    if (!certContent || !keyContent) {
      alert('Debes indicar la ruta o subir el certificado y la clave privada.');
      return;
    }
    setSigningId(id);
    try {
      await signInvoice(id, certContent, keyContent, token!);
      fetchInvoices();
    } catch (err) {
      setFormError('Error al firmar la factura');
    } finally {
      setSigningId(null);
    }
  };

  // Panel informativo sobre el proceso de firma electrónica
  const InfoPanel = () => (
    <div className="mb-6 p-4 bg-blue-50 border-l-4 border-blue-400 rounded">
      <h2 className="font-bold mb-2 text-blue-800">¿Cómo funciona la firma electrónica de facturas?</h2>
      <ol className="list-decimal pl-6 mb-2 text-sm text-blue-900">
        <li>Selecciona una o varias facturas pendientes de firma en la tabla.</li>
        <li>Pulsa <b>"Firmar electrónicamente"</b> para iniciar el proceso.</li>
        <li>El sistema generará el XML Facturae y lo enviará a <b>Autofirma</b> (debes tenerlo instalado y abierto).</li>
        <li>Autofirma te pedirá seleccionar tu certificado y firmará el XML localmente, sin exponer tu clave privada.</li>
        <li>El XML firmado se subirá automáticamente y la factura quedará firmada digitalmente.</li>
      </ol>
      <div className="text-xs text-blue-700">
        <b>Seguridad:</b> Tu clave privada <b>nunca se almacena</b> en el sistema. Si subes archivos PEM, solo se usan en memoria para la firma y se descartan después.<br/>
        <b>¿Qué es Autofirma?</b> Es una aplicación oficial del Gobierno de España para firmar documentos electrónicos con tu certificado digital.<br/>
        <b>¿Problemas?</b> Si no tienes Autofirma, puedes descargarlo en <a href="https://firmaelectronica.gob.es/Home/Descargas.html" target="_blank" rel="noopener noreferrer" className="underline text-blue-600">firmaelectronica.gob.es</a>.
      </div>
    </div>
  );

  // Selección de facturas
  const handleSelect = (id: string) => {
    setSelected((prev) => prev.includes(id) ? prev.filter(x => x !== id) : [...prev, id]);
  };

  // Intenta abrir Autofirma usando el protocolo custom
  const openAutofirma = () => {
    const a = document.createElement('a');
    a.href = 'afirma://service?op=launch';
    a.style.display = 'none';
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
  };

  // Proceso de firma electrónica con Autofirma
  const handleSignSelected = async () => {
    // 1. Intentar abrir Autofirma
    openAutofirma();
    setSignStatus('Generando XML de las facturas seleccionadas...');
    try {
      // 2. Solicitar XMLs al backend
      const res = await fetch('/api/invoices/generate-xml', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        },
        body: JSON.stringify({ ids: selected }),
      });
      if (!res.ok) throw new Error('Error generando XML');
      const xmls = await res.json();
      for (const { id, xml } of xmls) {
        setSignStatus(`Firmando factura ${id} con Autofirma...`);
        // 3. Llamar a Autofirma (puerto local)
        const resp = await fetch('http://127.0.0.1:8080/afirma-signature-http', {
          method: 'POST',
          body: new Blob([xml], { type: 'text/xml' }),
          headers: { 'Content-Type': 'application/xml' },
        });
        if (!resp.ok) throw new Error('Error en Autofirma');
        const signedXml = await resp.text();
        setSignStatus(`Subiendo XML firmado de la factura ${id}...`);
        // 4. Subir XML firmado al backend
        const uploadRes = await fetch('/api/invoices/upload-signed', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`,
          },
          body: JSON.stringify({ id, signedXml }),
        });
        if (!uploadRes.ok) throw new Error('Error subiendo XML firmado');
      }
      setSignStatus('¡Facturas firmadas correctamente!');
      setSelected([]);
      fetchInvoices();
    } catch (e: any) {
      setSignStatus('Error en el proceso de firma: ' + (e.message || e));
    }
  };

  return (
    <div className="py-6 max-w-5xl mx-auto">
      <InfoPanel />
      <div className="flex justify-between items-center mb-4">
        <h1 className="text-2xl font-bold">Facturación Electrónica</h1>
        <button className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700" onClick={handleOpenModal}>Nueva factura</button>
      </div>
      {signStatus && <div className="mb-2 text-blue-700 font-semibold">{signStatus}</div>}
      {successMsg && <div className="mb-2 text-green-700 font-semibold">{successMsg}</div>}
      {loading ? (
        <div className="flex items-center gap-2"><span className="animate-spin h-5 w-5 border-2 border-blue-500 rounded-full"></span> Cargando...</div>
      ) : error ? (
        <div className="text-red-600">{error}</div>
      ) : (
        <>
          <table className="min-w-full bg-white border">
            <thead>
              <tr>
                <th className="border px-2 py-1"></th>
                <th className="border px-2 py-1">Nº Factura</th>
                <th className="border px-2 py-1">Fecha</th>
                <th className="border px-2 py-1">Cliente</th>
                <th className="border px-2 py-1">Importe</th>
                <th className="border px-2 py-1">Estado</th>
                <th className="border px-2 py-1">Provisiones</th>
                <th className="border px-2 py-1">Acciones</th>
              </tr>
            </thead>
            <tbody>
              {invoices.map((inv: any) => (
                <tr key={inv.id}>
                  <td className="border px-2 py-1 text-center">
                    {!inv.xmlFirmado && inv.xml && (
                      <input
                        type="checkbox"
                        checked={selected.includes(inv.id)}
                        onChange={() => handleSelect(inv.id)}
                        aria-label={`Seleccionar factura ${inv.numeroFactura}`}
                      />
                    )}
                  </td>
                  <td className="border px-2 py-1">{inv.numeroFactura}</td>
                  <td className="border px-2 py-1">{inv.fechaFactura?.slice(0, 10)}</td>
                  <td className="border px-2 py-1">{inv.receptor?.name || '-'}</td>
                  <td className="border px-2 py-1">{inv.importeTotal?.toFixed(2)} €</td>
                  <td className="border px-2 py-1">{inv.estado}</td>
                  <td className="border px-2 py-1">
                    {inv.provisiones && inv.provisiones.length > 0 ? (
                      <ul className="list-disc pl-4">
                        {inv.provisiones.map((prov: any) => (
                          <li key={prov.id}>{prov.amount || prov.importe}€ - {prov.description}</li>
                        ))}
                      </ul>
                    ) : (
                      <span className="text-gray-500">Sin provisiones</span>
                    )}
                  </td>
                  <td className="border px-2 py-1 space-x-2">
                    {inv.xml && (
                      <button className="text-blue-600 underline" onClick={() => handleDownload(inv.xml, `${inv.numeroFactura}.xml`)}>Descargar XML</button>
                    )}
                    {inv.xmlFirmado && (
                      <button className="text-green-600 underline" onClick={() => handleDownload(inv.xmlFirmado, `${inv.numeroFactura}-firmado.xml`)}>Descargar XML Firmado</button>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
          {/* Lista de pasos antes de los inputs de archivo y botón de firma */}
          <div className="mt-6 mb-2 p-3 bg-yellow-50 border-l-4 border-yellow-400 rounded">
            <ol className="list-decimal pl-6 text-sm text-yellow-900">
              <li>A continuación tiene que elegir su <b>certificado digital</b> (.pem).</li>
              <li>Después, elija la <b>clave privada asociada</b> (.pem).</li>
              <li>Con eso, el sistema preparará el XML de las facturas seleccionadas y lo enviará a <b>Autofirma</b>.</li>
              <li>Finalmente, pulse en <b>Firmar Electrónicamente</b>: se producirá la firma digital y la subida automática del XML firmado.</li>
            </ol>
          </div>
          {/* Inputs de PEM y botón de firma múltiple después de la tabla */}
          <div className="mt-6 flex gap-2 items-center">
            <input type="file" accept=".pem" onChange={e => setCertFile(e.target.files?.[0] || null)} title="Subir certificado PEM" aria-label="Subir certificado PEM" />
            <span className="text-xs text-gray-600">Certificado digital (.pem)</span>
            <input type="file" accept=".pem" onChange={e => setKeyFile(e.target.files?.[0] || null)} title="Subir clave privada PEM" aria-label="Subir clave privada PEM" />
            <span className="text-xs text-gray-600">Clave privada (.pem)</span>
            <button
              className="ml-4 px-4 py-2 bg-green-600 text-white rounded disabled:bg-gray-300"
              disabled={selected.length === 0 || !!signStatus}
              onClick={handleSignSelected}
            >
              Firmar electrónicamente ({selected.length})
            </button>
          </div>
        </>
      )}
      {/* Modal de nueva factura */}
      {showModal && (
        <div className="fixed inset-0 bg-black bg-opacity-30 flex items-center justify-center z-50">
          <form className="bg-white p-6 rounded shadow max-w-lg w-full" onSubmit={handleCreate}>
            <h2 className="text-xl font-bold mb-4">Nueva factura</h2>
            {formError && <div className="mb-2 text-red-600">{formError}</div>}
            <div className="mb-2">
              <label className="block text-sm">Nº Factura (se asigna automáticamente)</label>
              <input name="numeroFactura" value={form.numeroFactura || ''} readOnly className="border px-2 py-1 rounded w-full bg-gray-100" title="Número de factura" aria-label="Número de factura" />
            </div>
            <div className="mb-2">
              <label className="block text-sm">Fecha</label>
              <input name="fechaFactura" type="date" value={form.fechaFactura} onChange={handleFormChange} className="border px-2 py-1 rounded w-full" required title="Fecha de factura" aria-label="Fecha de factura" />
            </div>
            <div className="mb-2">
              <label className="block text-sm">Cliente</label>
              <select name="receptorId" value={form.receptorId} onChange={handleClientChange} className="border px-2 py-1 rounded w-full" required title="Cliente" aria-label="Cliente">
                <option value="">Selecciona un cliente</option>
                {clients.map((c: any) => (
                  <option key={c.user.id} value={c.user.id}>{c.user.name} ({c.dni})</option>
                ))}
              </select>
            </div>
            {form.receptorId && (
              <div className="mb-2">
                <label className="block text-sm">Expediente</label>
                <select name="expedienteId" value={form.expedienteId} onChange={handleFormChange} className="border px-2 py-1 rounded w-full" required title="Expediente" aria-label="Expediente">
                  <option value="">Selecciona un expediente</option>
                  {expedientesCliente.map((exp: any) => (
                    <option key={exp.id} value={exp.id}>{exp.title} (ID: {exp.id})</option>
                  ))}
                </select>
              </div>
            )}
            {pendingProvision && (
              <div className="mb-2 p-2 bg-green-50 border-l-4 border-green-400 text-green-800 text-sm rounded">
                <b>Provisión de fondos pendiente:</b><br />
                Se han encontrado {filteredProvisions.length} provisiones de fondos asociadas a este cliente y expediente.<br />
                <ul className="list-disc pl-5">
                  {filteredProvisions.map((p: any, idx: number) => (
                    <li key={p.id}>Provisión #{idx + 1}: {p.amount} € (ID: {p.id})</li>
                  ))}
                </ul>
                <b>Total a descontar en la factura:</b> {filteredProvisions.reduce((sum: number, p: any) => sum + p.amount, 0)} €<br />
                Este importe se ha añadido como concepto negativo y se restará del importe final de la factura.
              </div>
            )}
            <div className="mb-2">
              <label className="block text-sm">Tipo Factura</label>
              <select name="tipoFactura" value={form.tipoFactura} onChange={handleFormChange} className="border px-2 py-1 rounded w-full" title="Tipo de factura" aria-label="Tipo de factura">
                <option value="F">Completa</option>
                <option value="R">Rectificativa</option>
              </select>
            </div>
            <div className="mb-2">
              <label className="block text-sm">Fecha Operación</label>
              <input name="fechaOperacion" type="date" value={form.fechaOperacion} onChange={handleFormChange} className="border px-2 py-1 rounded w-full" required title="Fecha de operación" aria-label="Fecha de operación" />
            </div>
            <div className="mb-2">
              <label className="block text-sm">Método de Pago</label>
              <input name="metodoPago" value={form.metodoPago} onChange={handleFormChange} className="border px-2 py-1 rounded w-full" required title="Método de pago" aria-label="Método de pago" />
            </div>
            <div className="mb-2">
              <label className="block text-sm">Régimen IVA Emisor</label>
              <input name="regimenIvaEmisor" value={form.regimenIvaEmisor} onChange={handleFormChange} className="border px-2 py-1 rounded w-full" required title="Régimen IVA emisor" aria-label="Régimen IVA emisor" />
            </div>
            <div className="mb-2">
              <label className="block text-sm">Clave Operación</label>
              <input name="claveOperacion" value={form.claveOperacion} onChange={handleFormChange} className="border px-2 py-1 rounded w-full" required title="Clave de operación" aria-label="Clave de operación" />
            </div>
            <div className="mb-2">
              <label className="block text-sm">Tipo IVA (%)</label>
              <input name="tipoIVA" type="number" value={form.tipoIVA} onChange={handleFormChange} className="border px-2 py-1 rounded w-full" required title="Tipo de IVA" aria-label="Tipo de IVA" />
            </div>
            <div className="mb-2">
              <label className="block text-sm font-bold">Conceptos</label>
              {form.items.map((item: any, idx: number) => (
                <div key={idx} className="flex gap-2 mb-1">
                  <input placeholder="Descripción" value={item.description} onChange={e => handleItemChange(idx, 'description', e.target.value)} className="border px-2 py-1 rounded w-1/2" required title="Descripción del concepto" aria-label="Descripción del concepto" />
                  <input type="number" placeholder="Cantidad" value={item.quantity} onChange={e => handleItemChange(idx, 'quantity', Number(e.target.value))} className="border px-2 py-1 rounded w-1/4" required title="Cantidad" aria-label="Cantidad" />
                  <input type="number" placeholder="Precio unitario" value={item.unitPrice} onChange={e => handleItemChange(idx, 'unitPrice', Number(e.target.value))} className="border px-2 py-1 rounded w-1/4" required title="Precio unitario" aria-label="Precio unitario" />
                  <span className="px-2">Total: {item.total}</span>
                </div>
              ))}
              <button type="button" className="text-blue-600 underline mt-1" onClick={handleAddItem}>Añadir concepto</button>
            </div>
            <div className="flex justify-end gap-2 mt-4">
              <button type="button" className="px-4 py-2 bg-gray-300 rounded" onClick={() => setShowModal(false)} disabled={creating}>Cancelar</button>
              <button type="submit" className="px-4 py-2 bg-blue-600 text-white rounded" disabled={creating}>{creating ? 'Creando...' : 'Crear'}</button>
            </div>
          </form>
        </div>
      )}
    </div>
  );
};

export default InvoicesPage; 