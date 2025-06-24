import React, { useEffect, useState, useMemo } from 'react';
import axios from 'axios';
import { Calendar, momentLocalizer, type Event as CalendarEvent } from 'react-big-calendar';
import moment from 'moment';
import 'react-big-calendar/lib/css/react-big-calendar.css';

const localizer = momentLocalizer(moment);

interface Appointment {
  id: string;
  client: { user: { name: string; email: string } };
  lawyer: { name: string; email: string };
  date: string;
  location: string;
  notes?: string;
}

const AppointmentsCalendarPage = () => {
  const [appointments, setAppointments] = useState<Appointment[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [selected, setSelected] = useState<Appointment | null>(null);

  useEffect(() => {
    const fetchAppointments = async () => {
      try {
        setLoading(true);
        const token = localStorage.getItem('token');
        const response = await axios.get('/api/appointments', {
          headers: { Authorization: `Bearer ${token}` }
        });
        setAppointments(response.data);
        setError(null);
      } catch (err: any) {
        setError('Error al cargar las citas');
      } finally {
        setLoading(false);
      }
    };
    fetchAppointments();
  }, []);

  // Estadísticas
  const stats = useMemo(() => {
    const total = appointments.length;
    const upcoming = appointments.filter(a => moment(a.date).isAfter(moment())).length;
    const past = appointments.filter(a => moment(a.date).isBefore(moment())).length;
    return { total, upcoming, past };
  }, [appointments]);

  // Map appointments to calendar events
  const events: CalendarEvent[] = appointments.map(a => ({
    id: a.id,
    title: `${a.client.user.name} - ${moment(a.date).format('HH:mm')}`,
    start: new Date(a.date),
    end: moment(a.date).add(1, 'hour').toDate(),
    resource: a,
  }));

  return (
    <div className="py-6">
      <div className="max-w-5xl mx-auto px-4 sm:px-6 lg:px-8">
        <h1 className="text-3xl font-bold mb-6">Calendario de Citas</h1>
        {/* Estadísticas */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-6">
          <div className="bg-white p-4 rounded shadow text-center">
            <div className="text-gray-500 text-sm">Total Citas</div>
            <div className="text-2xl font-bold">{stats.total}</div>
          </div>
          <div className="bg-white p-4 rounded shadow text-center">
            <div className="text-gray-500 text-sm">Próximas</div>
            <div className="text-2xl font-bold">{stats.upcoming}</div>
          </div>
          <div className="bg-white p-4 rounded shadow text-center">
            <div className="text-gray-500 text-sm">Pasadas</div>
            <div className="text-2xl font-bold">{stats.past}</div>
          </div>
        </div>
        {/* Calendario */}
        <div className="bg-white rounded shadow p-4">
          <Calendar
            localizer={localizer}
            events={events}
            startAccessor="start"
            endAccessor="end"
            style={{ height: 500 }}
            onSelectEvent={event => setSelected(event.resource)}
            messages={{
              next: 'Sig',
              previous: 'Ant',
              today: 'Hoy',
              month: 'Mes',
              week: 'Semana',
              day: 'Día',
              agenda: 'Agenda',
            }}
          />
        </div>
        {/* Modal Detalle de Cita */}
        {selected && (
          <div className="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-40">
            <div className="bg-white rounded-lg shadow-lg p-6 w-full max-w-md relative">
              <button className="absolute top-2 right-2 text-gray-500" onClick={() => setSelected(null)}>×</button>
              <h2 className="text-xl font-bold mb-4">Detalle de la Cita</h2>
              <div className="mb-2"><b>Cliente:</b> {selected.client.user.name} ({selected.client.user.email})</div>
              <div className="mb-2"><b>Abogado:</b> {selected.lawyer.name} ({selected.lawyer.email})</div>
              <div className="mb-2"><b>Fecha y hora:</b> {moment(selected.date).format('DD/MM/YYYY HH:mm')}</div>
              <div className="mb-2"><b>Lugar:</b> {selected.location}</div>
              {selected.notes && <div className="mb-2"><b>Notas:</b> {selected.notes}</div>}
              <div className="flex justify-end mt-4">
                <button className="px-4 py-2 bg-blue-600 text-white rounded" onClick={() => setSelected(null)}>Cerrar</button>
              </div>
            </div>
          </div>
        )}
        {loading && <div className="text-center py-8">Cargando...</div>}
        {error && <div className="text-center text-red-600 py-8">{error}</div>}
      </div>
    </div>
  );
};

export default AppointmentsCalendarPage; 