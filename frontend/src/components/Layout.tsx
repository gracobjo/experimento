import { Outlet, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import ChatWidget from './ChatWidget';
import { useState } from 'react';

const Layout = () => {
  const { user, logout } = useAuth();
  const [openBilling, setOpenBilling] = useState(false);

  const isAdmin = user?.role === 'ADMIN';
  const isLawyer = user?.role === 'ABOGADO';
  const isClient = user?.role === 'CLIENTE';

  return (
    <div className="min-h-screen flex flex-col">
      {/* Navigation */}
      <nav className="bg-blue-800 text-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex items-center justify-between h-16">
            <div className="flex items-center">
              <Link to="/" className="text-xl font-bold">
                Despacho Legal
              </Link>
            </div>
            <div className="flex items-center space-x-4 relative">
              {user ? (
                <>
                  {isAdmin && (
                    <>
                      <Link to="/admin/dashboard" className="hover:text-blue-200">
                        Admin Dashboard
                      </Link>
                      <Link to="/admin/users" className="hover:text-blue-200">
                        Usuarios
                      </Link>
                      <Link to="/admin/cases" className="hover:text-blue-200">
                        Expedientes
                      </Link>
                      <Link to="/admin/appointments" className="hover:text-blue-200">
                        Citas
                      </Link>
                      <Link to="/admin/tasks" className="hover:text-blue-200">
                        Tareas
                      </Link>
                      <Link to="/admin/documents" className="hover:text-blue-200">
                        Documentos
                      </Link>
                      <Link to="/admin/reports" className="hover:text-blue-200">
                        Reportes
                      </Link>
                    </>
                  )}
                  {isLawyer && (
                    <>
                      <Link to="/dashboard" className="hover:text-blue-200">
                        Dashboard
                      </Link>
                      <Link to="/lawyer/cases" className="hover:text-blue-200">
                        Mis Expedientes
                      </Link>
                      <Link to="/lawyer/appointments" className="hover:text-blue-200">
                        Citas
                      </Link>
                      <Link to="/lawyer/tasks" className="hover:text-blue-200">
                        Tareas
                      </Link>
                      <Link to="/lawyer/chat" className="hover:text-blue-200">
                        Chat
                      </Link>
                      <Link to="/lawyer/reports" className="hover:text-blue-200">
                        Reportes
                      </Link>
                      {/* Menú desplegable de Facturación */}
                      <div className="relative">
                        <button
                          className="hover:text-blue-200 px-2 py-1 rounded focus:outline-none"
                          onClick={() => setOpenBilling((v) => !v)}
                          onBlur={() => setTimeout(() => setOpenBilling(false), 150)}
                        >
                          Facturación
                          <svg className="inline w-4 h-4 ml-1" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" /></svg>
                        </button>
                        {openBilling && (
                          <div className="absolute right-0 mt-2 w-56 bg-white text-gray-900 rounded shadow-lg z-50">
                            <Link
                              to="/lawyer/provisiones"
                              className="block px-4 py-2 hover:bg-blue-100"
                              onClick={() => setOpenBilling(false)}
                            >
                              Provisión de Fondos
                            </Link>
                            <Link
                              to="/lawyer/facturacion"
                              className="block px-4 py-2 hover:bg-blue-100"
                              onClick={() => setOpenBilling(false)}
                            >
                              Facturación Electrónica
                            </Link>
                          </div>
                        )}
                      </div>
                    </>
                  )}
                  {isClient && (
                    <>
                      <Link to="/dashboard" className="hover:text-blue-200">
                        Dashboard
                      </Link>
                      <Link to="/client/cases" className="hover:text-blue-200">
                        Mis Expedientes
                      </Link>
                      <Link to="/client/appointments" className="hover:text-blue-200">
                        Mis Citas
                      </Link>
                      <Link to="/client/chat" className="hover:text-blue-200">
                        Chat
                      </Link>
                    </>
                  )}
                  {user && (
                    <span className="text-sm font-medium text-white bg-blue-900 rounded px-3 py-1">
                      {user.name} {user.email && <span className="text-gray-300">({user.email})</span>}
                    </span>
                  )}
                  <button
                    onClick={logout}
                    className="hover:text-blue-200"
                  >
                    Cerrar Sesión
                  </button>
                </>
              ) : (
                <>
                  <Link to="/login" className="hover:text-blue-200">
                    Iniciar Sesión
                  </Link>
                  <Link to="/register" className="hover:text-blue-200">
                    Registro
                  </Link>
                </>
              )}
            </div>
          </div>
        </div>
      </nav>

      {/* Main content */}
      <main className="flex-grow">
        <div className="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
          <Outlet />
        </div>
      </main>

      {/* Footer */}
      <footer className="bg-gray-800 text-white">
        <div className="max-w-7xl mx-auto py-6 px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center">
            <div>
              <p>&copy; 2024 Despacho Legal. Todos los derechos reservados.</p>
            </div>
            <div className="flex space-x-4">
              <Link to="/privacidad" className="hover:text-gray-300">
                Política de Privacidad
              </Link>
              <Link to="/terminos" className="hover:text-gray-300">
                Términos de Servicio
              </Link>
            </div>
          </div>
        </div>
      </footer>

      {/* Chat Widget - Solo visible para usuarios autenticados */}
      {user && <ChatWidget />}
    </div>
  );
};

export default Layout; 