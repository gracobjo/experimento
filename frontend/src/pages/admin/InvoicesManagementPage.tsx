import React, { useEffect, useState } from 'react';
import api from '../../api/axios';

interface Invoice {
  id: string;
  numeroFactura: string;
  fechaFactura: string;
  importeTotal: number;
  estado: string;
  paymentDate?: string;
  emisor?: {
    id: string;
    name: string;
  };
  receptor?: {
    id: string;
    name: string;
  };
}

interface User {
  id: string;
  name: string;
}

const InvoicesManagementPage = () => {
  const [invoices, setInvoices] = useState<Invoice[]>([]);
  const [lawyers, setLawyers] = useState<User[]>([]);
  const [clients, setClients] = useState<User[]>([]);
  const [selectedLawyer, setSelectedLawyer] = useState('');
  const [selectedClient, setSelectedClient] = useState('');
  const [selectedPaymentDate, setSelectedPaymentDate] = useState('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchLawyersAndClients = async () => {
      try {
        const token = localStorage.getItem('token');
        const [lawyersRes, clientsRes] = await Promise.all([
          api.get('/users/lawyers', { headers: { Authorization: `Bearer ${token}` } }),
          api.get('/users/clients', { headers: { Authorization: `Bearer ${token}` } })
        ]);
        setLawyers(lawyersRes.data);
        setClients(clientsRes.data);
      } catch {}
    };
    fetchLawyersAndClients();
  }, []);

  useEffect(() => {
    const fetchInvoices = async () => {
      try {
        setLoading(true);
        const token = localStorage.getItem('token');
        const params: any = {};
        if (selectedLawyer) params.lawyerId = selectedLawyer;
        if (selectedClient) params.clientId = selectedClient;
        if (selectedPaymentDate) params.paymentDate = selectedPaymentDate;
        const response = await api.get('/invoices', {
          headers: { Authorization: `Bearer ${token}` },
          params,
        });
        setInvoices(response.data);
        setError(null);
      } catch (err: any) {
        setError('Error al cargar las facturas');
        setInvoices([]);
      } finally {
        setLoading(false);
      }
    };
    fetchInvoices();
  }, [selectedLawyer, selectedClient, selectedPaymentDate]);

  return (
    <div className="py-6">
      <div className="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="bg-white rounded-lg shadow p-6">
          <h1 className="text-2xl font-semibold mb-2">Gestión de Facturas</h1>
          <div className="flex flex-wrap gap-4 mb-6">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Abogado</label>
              <select value={selectedLawyer} onChange={e => setSelectedLawyer(e.target.value)} className="border rounded px-2 py-1">
                <option value="">Todos</option>
                {lawyers.map(lawyer => (
                  <option key={lawyer.id} value={lawyer.id}>{lawyer.name}</option>
                ))}
              </select>
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Cliente</label>
              <select value={selectedClient} onChange={e => setSelectedClient(e.target.value)} className="border rounded px-2 py-1">
                <option value="">Todos</option>
                {clients.map(client => (
                  <option key={client.id} value={client.id}>{client.name}</option>
                ))}
              </select>
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Fecha de pago</label>
              <input type="date" value={selectedPaymentDate} onChange={e => setSelectedPaymentDate(e.target.value)} className="border rounded px-2 py-1" />
            </div>
          </div>
          {loading ? (
            <div className="flex items-center justify-center h-40">
              <div className="animate-spin rounded-full h-10 w-10 border-t-2 border-b-2 border-blue-500"></div>
            </div>
          ) : error ? (
            <div className="bg-red-100 text-red-700 p-3 rounded mb-4">{error}</div>
          ) : invoices.length === 0 ? (
            <div className="text-gray-500">No hay facturas registradas.</div>
          ) : (
            <div className="overflow-x-auto">
              <table className="min-w-full divide-y divide-gray-200">
                <thead>
                  <tr>
                    <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Nº Factura</th>
                    <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Fecha</th>
                    <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Importe</th>
                    <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Estado</th>
                    <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Abogado</th>
                    <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Cliente</th>
                    <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Fecha de pago</th>
                  </tr>
                </thead>
                <tbody className="bg-white divide-y divide-gray-200">
                  {invoices.map((invoice) => (
                    <tr key={invoice.id}>
                      <td className="px-4 py-2">{invoice.numeroFactura || 'N/A'}</td>
                      <td className="px-4 py-2">{invoice.fechaFactura ? new Date(invoice.fechaFactura).toLocaleDateString() : 'N/A'}</td>
                      <td className="px-4 py-2">{(invoice.importeTotal || 0).toFixed(2)} €</td>
                      <td className="px-4 py-2">{invoice.estado || 'N/A'}</td>
                      <td className="px-4 py-2">{invoice.emisor?.name || '-'}</td>
                      <td className="px-4 py-2">{invoice.receptor?.name || '-'}</td>
                      <td className="px-4 py-2">{invoice.paymentDate ? new Date(invoice.paymentDate).toLocaleDateString() : '-'}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default InvoicesManagementPage; 