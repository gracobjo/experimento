import axios from './axios';

export async function getInvoices(token: string) {
  const res = await axios.get('/invoices', {
    headers: { Authorization: `Bearer ${token}` },
  });
  return res.data;
}

export async function createInvoice(data: any, token: string) {
  const res = await axios.post('/invoices', data, {
    headers: { Authorization: `Bearer ${token}` },
  });
  return res.data;
}

export async function signInvoice(id: string, certContent: string, keyContent: string, token: string) {
  const res = await axios.post(`/invoices/${id}/sign`, { certContent, keyContent }, {
    headers: { Authorization: `Bearer ${token}` },
  });
  return res.data;
} 