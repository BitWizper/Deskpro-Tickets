import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './App.css';

const API_URL = 'http://localhost:8080';

function App() {
  const [tickets, setTickets] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [activeTab, setActiveTab] = useState('tickets');
  const [tecnicos, setTecnicos] = useState([]);
  const [reportes, setReportes] = useState({});
  const [newTicket, setNewTicket] = useState({
    titulo: '',
    descripcion: '',
    estado: 'ABIERTO',
    prioridad: 'MEDIA'
  });
  const [asignacionData, setAsignacionData] = useState({
    ticketId: '',
    tecnico: ''
  });

  useEffect(() => {
    cargarTickets();
    cargarTecnicosActivos();
  }, []);

  const cargarTickets = async () => {
    try {
      setError(null);
      const response = await axios.get(`${API_URL}/ticket`, {
        timeout: 5000
      });
      setTickets(response.data);
      setLoading(false);
    } catch (error) {
      console.error('Error cargando tickets:', error);
      setError(`Error: ${error.response?.data?.message || error.message}`);
      setLoading(false);
    }
  };

  const cargarTecnicosActivos = async () => {
    try {
      const response = await axios.get(`${API_URL}/api/tecnicos/activos`);
      setTecnicos(response.data);
    } catch (error) {
      console.error('Error cargando técnicos:', error);
    }
  };

  const cargarReportes = async () => {
    try {
      const [estadoRes, prioridadRes, tecnicoRes, metricasRes] = await Promise.all([
        axios.get(`${API_URL}/api/reportes/por-estado`),
        axios.get(`${API_URL}/api/reportes/por-prioridad`),
        axios.get(`${API_URL}/api/reportes/por-tecnico`),
        axios.get(`${API_URL}/api/reportes/metricas`)
      ]);

      setReportes({
        porEstado: estadoRes.data,
        porPrioridad: prioridadRes.data,
        porTecnico: tecnicoRes.data,
        metricas: metricasRes.data
      });
    } catch (error) {
      console.error('Error cargando reportes:', error);
      setError(`Error cargando reportes: ${error.message}`);
    }
  };

  const crearTicket = async (e) => {
    e.preventDefault();
    try {
      setError(null);
      const response = await axios.post(`${API_URL}/ticket`, newTicket, {
        timeout: 5000
      });
      setNewTicket({ titulo: '', descripcion: '', estado: 'ABIERTO', prioridad: 'MEDIA' });
      setTickets(prev => [...prev, response.data]);
      alert('✅ Ticket creado exitosamente!');
    } catch (error) {
      console.error('Error creando ticket:', error);
      setError(`Error al crear ticket: ${error.response?.data?.message || error.message}`);
    }
  };

  const cambiarEstado = async (id, nuevoEstado) => {
    try {
      setError(null);
      const response = await axios.put(`${API_URL}/ticket/${id}/estado?estado=${nuevoEstado}`, {}, {
        timeout: 5000
      });
      
      setTickets(prev => prev.map(ticket => 
        ticket.id === id ? response.data : ticket
      ));
      alert('✅ Estado actualizado!');
    } catch (error) {
      console.error('Error actualizando estado:', error);
      setError(`Error actualizando estado: ${error.response?.data?.message || error.message}`);
    }
  };

  const asignarTecnico = async (e) => {
    e.preventDefault();
    try {
      setError(null);
      const response = await axios.put(
        `${API_URL}/api/tecnicos/asignar/${asignacionData.ticketId}?tecnico=${asignacionData.tecnico}`,
        {},
        { timeout: 5000 }
      );
      
      setTickets(prev => prev.map(ticket => 
        ticket.id === response.data.id ? response.data : ticket
      ));
      setAsignacionData({ ticketId: '', tecnico: '' });
      alert('✅ Técnico asignado exitosamente!');
      cargarTecnicosActivos();
    } catch (error) {
      console.error('Error asignando técnico:', error);
      setError(`Error asignando técnico: ${error.response?.data?.message || error.message}`);
    }
  };

  const removerTecnico = async (ticketId) => {
    try {
      setError(null);
      const response = await axios.delete(`${API_URL}/api/tecnicos/remover/${ticketId}`);
      
      setTickets(prev => prev.map(ticket => 
        ticket.id === response.data.id ? response.data : ticket
      ));
      alert('✅ Técnico removido exitosamente!');
      cargarTecnicosActivos();
    } catch (error) {
      console.error('Error removiendo técnico:', error);
      setError(`Error removiendo técnico: ${error.response?.data?.message || error.message}`);
    }
  };

  const obtenerTicketsSinAsignar = async () => {
    try {
      const response = await axios.get(`${API_URL}/api/tecnicos/sin-asignar`);
      setTickets(response.data);
      setActiveTab('tickets');
    } catch (error) {
      console.error('Error cargando tickets sin asignar:', error);
      setError(`Error: ${error.response?.data?.message || error.message}`);
    }
  };

  const obtenerTicketsPorTecnico = async (tecnico) => {
    try {
      const response = await axios.get(`${API_URL}/api/tecnicos/${tecnico}/tickets`);
      setTickets(response.data);
      setActiveTab('tickets');
    } catch (error) {
      console.error('Error cargando tickets por técnico:', error);
      setError(`Error: ${error.response?.data?.message || error.message}`);
    }
  };

  if (loading) {
    return (
      <div className="app">
        <header className="app-header">
          <h1>🎫 Sistema de Gestión de Tickets</h1>
        </header>
        <div className="loading">Cargando tickets...</div>
      </div>
    );
  }

  return (
    <div className="app">
      <header className="app-header">
        <h1>🎫 Sistema de Gestión de Tickets</h1>
        <p>Spring Boot 3 + Java 21 + React</p>
      </header>

      {/* Navegación */}
      <nav className="app-nav">
        <button 
          className={activeTab === 'tickets' ? 'nav-btn active' : 'nav-btn'}
          onClick={() => { setActiveTab('tickets'); cargarTickets(); }}
        >
          🎫 Tickets
        </button>
        <button 
          className={activeTab === 'tecnicos' ? 'nav-btn active' : 'nav-btn'}
          onClick={() => setActiveTab('tecnicos')}
        >
          👨‍💼 Gestión de Técnicos
        </button>
        <button 
          className={activeTab === 'reportes' ? 'nav-btn active' : 'nav-btn'}
          onClick={() => { setActiveTab('reportes'); cargarReportes(); }}
        >
          📊 Reportes
        </button>
      </nav>

      <div className="container">
        {error && (
          <div className="error-banner">
            ⚠️ {error}
            <button onClick={() => setError(null)}>×</button>
          </div>
        )}

        {/* Sección de Tickets */}
        {activeTab === 'tickets' && (
          <>
            <section className="form-section">
              <h2>Crear Nuevo Ticket</h2>
              <form onSubmit={crearTicket} className="ticket-form">
                <input
                  type="text"
                  placeholder="Título del ticket"
                  value={newTicket.titulo}
                  onChange={(e) => setNewTicket({...newTicket, titulo: e.target.value})}
                  required
                />
                <textarea
                  placeholder="Descripción del problema"
                  value={newTicket.descripcion}
                  onChange={(e) => setNewTicket({...newTicket, descripcion: e.target.value})}
                  required
                />
                <select
                  value={newTicket.prioridad}
                  onChange={(e) => setNewTicket({...newTicket, prioridad: e.target.value})}
                >
                  <option value="BAJA">Baja</option>
                  <option value="MEDIA">Media</option>
                  <option value="ALTA">Alta</option>
                </select>
                <button type="submit">Crear Ticket</button>
              </form>
            </section>

            <section className="tickets-section">
              <div className="section-header">
                <h2>Tickets ({tickets.length})</h2>
                <div className="filtros">
                  <button onClick={cargarTickets} className="filter-btn">Todos</button>
                  <button onClick={obtenerTicketsSinAsignar} className="filter-btn">Sin Asignar</button>
                  {tecnicos.map(tecnico => (
                    <button 
                      key={tecnico} 
                      onClick={() => obtenerTicketsPorTecnico(tecnico)}
                      className="filter-btn"
                    >
                      {tecnico}
                    </button>
                  ))}
                </div>
              </div>
              <div className="tickets-grid">
                {tickets.map(ticket => (
                  <div key={ticket.id} className={`ticket-card ${ticket.prioridad?.toLowerCase()} ${ticket.estado?.toLowerCase()}`}>
                    <div className="ticket-header">
                      <h3>{ticket.titulo}</h3>
                      <span className={`badge ${ticket.estado?.toLowerCase()}`}>
                        {ticket.estado}
                      </span>
                    </div>
                    <p className="ticket-desc">{ticket.descripcion}</p>
                    <div className="ticket-info">
                      <span className={`priority ${ticket.prioridad?.toLowerCase()}`}>
                        Prioridad: {ticket.prioridad}
                      </span>
                      <span className="date">
                        {ticket.fechaCreacion ? 
                          `Creado: ${new Date(ticket.fechaCreacion).toLocaleDateString()}` : 
                          'Fecha no disponible'}
                      </span>
                    </div>
                    {ticket.tecnicoAsignado && (
                      <div className="tecnico-asignado">
                        <strong>Técnico:</strong> {ticket.tecnicoAsignado}
                        <button 
                          onClick={() => removerTecnico(ticket.id)}
                          className="btn-remove"
                        >
                          ✕
                        </button>
                      </div>
                    )}
                    <div className="ticket-actions">
                      {ticket.estado === 'ABIERTO' && (
                        <>
                          <button 
                            onClick={() => cambiarEstado(ticket.id, 'EN_PROCESO')}
                            className="btn-progress"
                          >
                            En Proceso
                          </button>
                          <button 
                            onClick={() => cambiarEstado(ticket.id, 'CERRADO')}
                            className="btn-close"
                          >
                            Cerrar
                          </button>
                        </>
                      )}
                      {ticket.estado === 'EN_PROCESO' && (
                        <button 
                          onClick={() => cambiarEstado(ticket.id, 'CERRADO')}
                          className="btn-close"
                        >
                          Cerrar
                        </button>
                      )}
                      {ticket.estado === 'CERRADO' && (
                        <span className="closed-text">✅ Ticket Cerrado</span>
                      )}
                    </div>
                  </div>
                ))}
              </div>
            </section>
          </>
        )}

        {/* Sección de Técnicos */}
        {activeTab === 'tecnicos' && (
          <section className="tecnicos-section">
            <div className="form-section">
              <h2>Asignar Técnico a Ticket</h2>
              <form onSubmit={asignarTecnico} className="ticket-form">
                <input
                  type="number"
                  placeholder="ID del Ticket"
                  value={asignacionData.ticketId}
                  onChange={(e) => setAsignacionData({...asignacionData, ticketId: e.target.value})}
                  required
                />
                <input
                  type="text"
                  placeholder="Nombre del Técnico"
                  value={asignacionData.tecnico}
                  onChange={(e) => setAsignacionData({...asignacionData, tecnico: e.target.value})}
                  required
                />
                <button type="submit">Asignar Técnico</button>
              </form>
            </div>

            <div className="tecnicos-info">
              <div className="info-card">
                <h3>Técnicos Activos</h3>
                <div className="tecnicos-list">
                  {tecnicos.length > 0 ? (
                    tecnicos.map(tecnico => (
                      <div key={tecnico} className="tecnico-item">
                        <span>{tecnico}</span>
                        <button 
                          onClick={() => obtenerTicketsPorTecnico(tecnico)}
                          className="btn-view"
                        >
                          Ver Tickets
                        </button>
                      </div>
                    ))
                  ) : (
                    <p>No hay técnicos activos</p>
                  )}
                </div>
              </div>

              <div className="info-card">
                <h3>Tickets Sin Asignar</h3>
                <button 
                  onClick={obtenerTicketsSinAsignar}
                  className="btn-view-large"
                >
                  Ver Tickets Sin Asignar ({tickets.filter(t => !t.tecnicoAsignado).length})
                </button>
              </div>
            </div>
          </section>
        )}

        {/* Sección de Reportes */}
        {activeTab === 'reportes' && (
          <section className="reportes-section">
            <h2>Reportes y Métricas</h2>
            
            <div className="metricas-grid">
              <div className="metrica-card">
                <h3>📈 Métricas Generales</h3>
                {reportes.metricas ? (
                  <div className="metrica-content">
                    <div className="metrica-item">
                      <span>Total Tickets:</span>
                      <strong>{reportes.metricas.totalTickets}</strong>
                    </div>
                    <div className="metrica-item">
                      <span>Abiertos:</span>
                      <strong>{reportes.metricas.ticketsAbiertos}</strong>
                    </div>
                    <div className="metrica-item">
                      <span>En Proceso:</span>
                      <strong>{reportes.metricas.ticketsEnProceso}</strong>
                    </div>
                    <div className="metrica-item">
                      <span>Resueltos:</span>
                      <strong>{reportes.metricas.ticketsResueltos}</strong>
                    </div>
                    <div className="metrica-item">
                      <span>Cerrados:</span>
                      <strong>{reportes.metricas.ticketsCerrados}</strong>
                    </div>
                    <div className="metrica-item">
                      <span>Sin Asignar:</span>
                      <strong>{reportes.metricas.ticketsSinAsignar}</strong>
                    </div>
                  </div>
                ) : (
                  <p>Cargando métricas...</p>
                )}
              </div>

              <div className="reporte-card">
                <h3>📊 Por Estado</h3>
                {reportes.porEstado ? (
                  <div className="reporte-content">
                    {Object.entries(reportes.porEstado).map(([estado, count]) => (
                      <div key={estado} className="reporte-item">
                        <span>{estado}:</span>
                        <strong>{count}</strong>
                      </div>
                    ))}
                  </div>
                ) : (
                  <p>Cargando reporte...</p>
                )}
              </div>

              <div className="reporte-card">
                <h3>⚡ Por Prioridad</h3>
                {reportes.porPrioridad ? (
                  <div className="reporte-content">
                    {Object.entries(reportes.porPrioridad).map(([prioridad, count]) => (
                      <div key={prioridad} className="reporte-item">
                        <span>{prioridad}:</span>
                        <strong>{count}</strong>
                      </div>
                    ))}
                  </div>
                ) : (
                  <p>Cargando reporte...</p>
                )}
              </div>

              <div className="reporte-card">
                <h3>👨‍💼 Por Técnico</h3>
                {reportes.porTecnico ? (
                  <div className="reporte-content">
                    {Object.entries(reportes.porTecnico).map(([tecnico, count]) => (
                      <div key={tecnico} className="reporte-item">
                        <span>{tecnico}:</span>
                        <strong>{count}</strong>
                      </div>
                    ))}
                  </div>
                ) : (
                  <p>Cargando reporte...</p>
                )}
              </div>
            </div>
          </section>
        )}
      </div>
    </div>
  );
}

export default App;