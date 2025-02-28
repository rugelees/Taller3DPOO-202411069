package uniandes.dpoo.aerolinea.tiquetes;

import org.json.JSONException;
import org.json.JSONObject;

import uniandes.dpoo.aerolinea.modelo.Vuelo;
import uniandes.dpoo.aerolinea.modelo.cliente.Cliente;

public class Tiquete {
    private String codigo;
    private Vuelo vuelo;
    private Cliente cliente;
    private int tarifa;
    private boolean usado;
    
    public Tiquete(String codigo, Vuelo vuelo, Cliente cliente, int tarifa) {
        this.codigo = codigo;
        this.vuelo = vuelo;
        this.cliente = cliente;
        this.tarifa = tarifa;
        this.usado = false;
    }
    
    public String getCodigo() {
        return codigo;
    }
    
    public Vuelo getVuelo() {
        return vuelo;
    }
    
    public Cliente getCliente() {
        return cliente;
    }
    
    public int getTarifa() {
        return tarifa;
    }
    
    public boolean esUsado() {
        return usado;
    }
    
    public void marcarComoUsado() {
        this.usado = true;
    }
    
    public JSONObject salvarEnJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("codigoTiquete", codigo);
        json.put("tarifa", tarifa);
        json.put("usado", usado);
        json.put("vuelo", vuelo.getRuta());
        json.put("cliente", cliente.getIdentificador());
        return json;
    }}	