package uniandes.dpoo.aerolinea.modelo;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import uniandes.dpoo.aerolinea.exceptions.InformacionInconsistenteException;
import uniandes.dpoo.aerolinea.exceptions.VueloSobrevendidoException;
import uniandes.dpoo.aerolinea.modelo.cliente.Cliente;
import uniandes.dpoo.aerolinea.modelo.tarifas.CalculadoraTarifas;
import uniandes.dpoo.aerolinea.modelo.tarifas.CalculadoraTarifasTemporadaAlta;
import uniandes.dpoo.aerolinea.modelo.tarifas.CalculadoraTarifasTemporadaBaja;
import uniandes.dpoo.aerolinea.persistencia.CentralPersistencia;
import uniandes.dpoo.aerolinea.persistencia.IPersistenciaAerolinea;
import uniandes.dpoo.aerolinea.persistencia.IPersistenciaTiquetes;
import uniandes.dpoo.aerolinea.persistencia.TipoInvalidoException;
import uniandes.dpoo.aerolinea.tiquetes.GeneradorTiquetes;
import uniandes.dpoo.aerolinea.tiquetes.Tiquete;

public class Aerolinea {
    private List<Avion> aviones;
    private Map<String, Ruta> rutas;
    private List<Vuelo> vuelos;
    private Map<String, Cliente> clientes;
    private Map<String, Aeropuerto> aeropuertos;

    public Aerolinea() {
        aviones = new LinkedList<Avion>();
        rutas = new HashMap<String, Ruta>();
        vuelos = new LinkedList<Vuelo>();
        clientes = new HashMap<String, Cliente>();
        aeropuertos = new HashMap<String, Aeropuerto>();
    }

    public void agregarRuta(Ruta ruta) {
        this.rutas.put(ruta.getCodigoRuta(), ruta);
    }

    public void agregarAvion(Avion avion) {
        this.aviones.add(avion);
    }

    public void agregarCliente(Cliente cliente) {
        this.clientes.put(cliente.getIdentificador(), cliente);
    }

    public boolean existeCliente(String identificadorCliente) {
        return this.clientes.containsKey(identificadorCliente);
    }

    public Cliente getCliente(String identificadorCliente) {
        return this.clientes.get(identificadorCliente);
    }

    public Collection<Avion> getAviones() {
        return aviones;
    }

    public HashMap<String, Ruta> getRutas() {
        return (HashMap<String, Ruta>) rutas;
    }

    public Ruta getRuta(String codigoRuta) {
        return rutas.get(codigoRuta);
    }

    public Collection<Vuelo> getVuelos() {
        return vuelos;
    }
    
    public Aeropuerto getAeropuerto(String codigo) {
        return aeropuertos.get(codigo); 
    }
    
    public Vuelo getVuelo(String codigoRuta, String fechaVuelo) {
        for(Vuelo v : vuelos) {
            if(v.getRuta().getCodigoRuta().equals(codigoRuta) && v.getFecha().equals(fechaVuelo))
                return v;
        }
        return null;
    }
    
    public Collection<Cliente> getClientes() {
        return clientes.values();
    }

    public Collection<Tiquete> getTiquetes() {
        List<Tiquete> todos = new LinkedList<Tiquete>();
        for(Vuelo v : vuelos) {
            todos.addAll(v.getTiquetes());
        }
        return todos;
    }

    public void cargarAerolinea(String archivo, String tipoArchivo) throws TipoInvalidoException, IOException, InformacionInconsistenteException {
        IPersistenciaAerolinea cargador = CentralPersistencia.getPersistenciaAerolinea(tipoArchivo);
        cargador.cargarAerolinea(archivo, this);
    }

    public void salvarAerolinea(String archivo, String tipoArchivo) throws TipoInvalidoException, IOException {
        IPersistenciaAerolinea cargador = CentralPersistencia.getPersistenciaAerolinea(tipoArchivo);
        cargador.salvarAerolinea(archivo, this);
    }

    public void cargarTiquetes(String archivo, String tipoArchivo) throws TipoInvalidoException, IOException, InformacionInconsistenteException {
        IPersistenciaTiquetes cargador = CentralPersistencia.getPersistenciaTiquetes(tipoArchivo);
        cargador.cargarTiquetes(archivo, this);
    }

    public void salvarTiquetes(String archivo, String tipoArchivo) throws TipoInvalidoException, IOException {
        IPersistenciaTiquetes cargador = CentralPersistencia.getPersistenciaTiquetes(tipoArchivo);
        cargador.salvarTiquetes(archivo, this);
    }

    public void programarVuelo(String fecha, String codigoRuta, String nombreAvion, int capacidad) throws Exception {
        Ruta r = getRuta(codigoRuta);
        if(r == null)
            throw new Exception("Ruta no encontrada");
        Avion a = null;
        for(Avion avion : aviones) {
            if(avion.getNombre().equals(nombreAvion)) {
                a = avion;
                break;
            }
        }
        if(a == null)
            throw new Exception("Avion no encontrado");
        for(Vuelo v : vuelos) {
            if(v.getFecha().equals(fecha) && v.getAvion().getNombre().equals(nombreAvion))
                throw new Exception("Avion ocupado en esa fecha");
        }
        Vuelo nuevoVuelo = new Vuelo(r, fecha, a, capacidad);
        vuelos.add(nuevoVuelo);
    }

    public int venderTiquetes(String identificadorCliente, String fecha, String codigoRuta, int cantidad) throws VueloSobrevendidoException, Exception {
        Vuelo vuelo = getVuelo(codigoRuta, fecha);
        if(vuelo == null)
            throw new Exception("Vuelo no encontrado");
        Cliente cliente = getCliente(identificadorCliente);
        if(cliente == null)
            throw new Exception("Cliente no encontrado");
        int mes = Integer.parseInt(fecha.substring(5,7));
        CalculadoraTarifas calculadora;
        if((mes >= 1 && mes <= 5) || (mes >= 9 && mes <= 11))
            calculadora = new CalculadoraTarifasTemporadaBaja();
        else
            calculadora = new CalculadoraTarifasTemporadaAlta();
        int tarifa = calculadora.calcularTarifa(vuelo, cliente);
        int tiquetesVendidos = vuelo.getTiquetes().size();
        if(tiquetesVendidos + cantidad > vuelo.getAvion().getCapacidad())
            throw new VueloSobrevendidoException(vuelo);
        int total = 0;
        for(int i = 0; i < cantidad; i++) {
            Tiquete t = GeneradorTiquetes.generarTiquete(vuelo, cliente, tarifa);
            vuelo.agregarTiquete(t);
            cliente.agregarTiquete(t);
            GeneradorTiquetes.registrarTiquete(t);
            total += tarifa;
        }
        return total;
    }

    public void registrarVueloRealizado(String fecha, String codigoRuta) {
        Iterator<Vuelo> it = vuelos.iterator();
        while(it.hasNext()) {
            Vuelo v = it.next();
            if(v.getFecha().equals(fecha) && v.getRuta().getCodigoRuta().equals(codigoRuta)) {
                it.remove();
                break;
            }
        }
    }

    public String consultarSaldoPendienteCliente(String identificadorCliente) {
        Cliente cliente = getCliente(identificadorCliente);
        if(cliente == null)
            return "0";
        int suma = 0;
        for(Tiquete t : cliente.getTiquetesSinUsar()) {
            suma += t.getTarifa();
        }
        return String.valueOf(suma);
    }
}
