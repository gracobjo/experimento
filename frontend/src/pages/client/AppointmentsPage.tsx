import React, { useState, useEffect } from 'react';
import api from '../../api/axios';
import { useAuth } from '../../context/AuthContext';

interface Appointment {
  id: string;
  date: string;
  location?: string;
  notes?: string;
  lawyer: {
    id: string;
    name: string;
    email: string;
  };
}

interface Lawyer {
  id: string;
  name: string;
  email: string;
}

const AppointmentsPage = () => {
  const [lawyers, setLawyers] = useState<Lawyer[]>([]);
  const [appointments, setAppointments] = useState<Appointment[]>([]);
  const [form, setForm] = useState({
    lawyerId: '',
    date: '',
    location: '',
    notes: '',
  });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState<string | null>(null);
  const { user } = useAuth();

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);
        const token = localStorage.getItem('token');
        const [lawyersRes, appointmentsRes] = await Promise.all([
          api.get('/users/lawyers'),
          api.get('/appointments')
        ]);
        setLawyers(lawyersRes.data);
        setAppointments(appointmentsRes.data);
        setError(null);
      } catch (err: any) {
        setError('Error al cargar los datos');
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, []);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    setSuccess(null);
    if (!form.lawyerId || !form.date) {
      setError('Selecciona abogado y fecha/hora');
      return;
    }
    try {
      const token = localStorage.getItem('token');
      const response = await api.post('/appointments', {
        lawyerId: form.lawyerId,
        date: form.date,
        location: form.location || undefined,
        notes: form.notes || undefined,
      }, {
        headers: { Authorization: `Bearer ${token}` }
      });
      setAppointments(prev => [response.data, ...prev]);
      setForm({ lawyerId: '', date: '', location: '', notes: '' });
      setSuccess('Cita agendada correctamente');
    } catch (err: any) {
      setError(err.response?.data?.message || 'Error al agendar la cita');
    }
  };

  return (
    <div className="py-6">
      <div className="max-w-2xl mx-auto px-4 sm:px-6 lg:px-8">
        <h1 className="text-2xl font-bold mb-6">Agendar Cita</h1>
        <form onSubmit={handleSubmit} className="bg-white p-6 rounded-lg shadow mb-8 space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">Abogado</label>
            <select
              name="lawyerId"
              value={form.lawyerId}
              onChange={handleChange}
              className="w-full px-3 py-2 border border-gray-300 rounded-md"
              required
            >
              <option value="">Selecciona un abogado</option>
              {lawyers.map(lawyer => (
                <option key={lawyer.id} value={lawyer.id}>{lawyer.name} ({lawyer.email})</option>
              ))}
            </select>
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">Fecha y hora</label>
            <input
              type="datetime-local"
              name="date"
              value={form.date}
              onChange={handleChange}
              className="w-full px-3 py-2 border border-gray-300 rounded-md"
              required
            />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">Ubicación (opcional)</label>
            <input
              type="text"
              name="location"
              value={form.location}
              onChange={handleChange}
              className="w-full px-3 py-2 border border-gray-300 rounded-md"
              placeholder="Oficina, online, etc."
            />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">Motivo / Notas (opcional)</label>
            <textarea
              name="notes"
              value={form.notes}
              onChange={handleChange}
              className="w-full px-3 py-2 border border-gray-300 rounded-md"
              rows={2}
              placeholder="Motivo de la cita, detalles, etc."
            />
          </div>
          {error && <div className="text-red-600 text-sm">{error}</div>}
          {success && <div className="text-green-600 text-sm">{success}</div>}
          <button
            type="submit"
            className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700"
          >
            Agendar Cita
          </button>
        </form>

        <h2 className="text-xl font-semibold mb-4">Mis Citas Agendadas</h2>
        {loading ? (
          <div className="text-gray-500">Cargando citas...</div>
        ) : appointments.length === 0 ? (
          <div className="text-gray-500">No tienes citas agendadas.</div>
        ) : (
          <ul className="space-y-4">
            {appointments.map(app => (
              <li key={app.id} className="bg-white p-4 rounded-lg shadow flex flex-col md:flex-row md:items-center md:justify-between">
                <div>
                  <div className="font-medium text-gray-900">{app.lawyer.name}</div>
                  <div className="text-sm text-gray-500">{app.lawyer.email}</div>
                  <div className="text-sm text-gray-700 mt-1">{app.location || 'Sin ubicación'}</div>
                  {app.notes && <div className="text-xs text-gray-500 mt-1">{app.notes}</div>}
                </div>
                <div className="mt-2 md:mt-0 text-right">
                  <div className="text-sm text-gray-700">{new Date(app.date).toLocaleString()}</div>
                </div>
              </li>
            ))}
          </ul>
        )}
      </div>
    </div>
  );
};

export default AppointmentsPage; 