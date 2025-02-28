package uniandes.dpoo.aerolinea.modelo.cliente;

import java.util.ArrayList;
import java.util.List;
import uniandes.dpoo.aerolinea.modelo.Vuelo;
import uniandes.dpoo.aerolinea.tiquetes.Tiquete;

public abstract class Cliente {
    protected List<Tiquete> tiquetesSinUsar;
    protected List<Tiquete> tiquetesUsados;
    
    public Cliente() {
        tiquetesSinUsar = new ArrayList<Tiquete>();
        tiquetesUsados = new ArrayList<Tiquete>();
    }
    
    public void agregarTiquete(Tiquete tiquete) {
        tiquetesSinUsar.add(tiquete);
    }
    
    public int calcularValorTotalTiquetes() {
        int total = 0;
        for(Tiquete t : tiquetesSinUsar)
            total += t.getTarifa();
        return total;
    }
    
    public void usarTiquetes(Vuelo vuelo) {
        List<Tiquete> usados = new ArrayList<Tiquete>();
        for(Tiquete t : tiquetesSinUsar) {
            if(t.getVuelo().equals(vuelo)) {
                t.marcarComoUsado();
                usados.add(t);
            }
        }
        tiquetesSinUsar.removeAll(usados);
        tiquetesUsados.addAll(usados);
    }
    
    public List<Tiquete> getTiquetesSinUsar() {
        return tiquetesSinUsar;
    }
    
    public List<Tiquete> getTiquetesUsados() {
        return tiquetesUsados;
    }
    
    public abstract String getIdentificador();
    
    public abstract String getTipoCliente();
}
