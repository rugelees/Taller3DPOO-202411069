package uniandes.dpoo.aerolinea.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import uniandes.dpoo.aerolinea.tiquetes.Tiquete;

public class Vuelo {
    private Ruta ruta;
    private String fecha;
    private Avion avion;
    private List<Tiquete> tiquetes;
    private int capacidad;
    
    public Vuelo(Ruta ruta, String fecha, Avion avion, int capacidad) {
        this.ruta = ruta;
        this.fecha = fecha;
        this.avion = avion;
        this.capacidad = capacidad;
        this.tiquetes = new ArrayList<Tiquete>();
    }
    
    public Ruta getRuta() {
        return ruta;
    }
    
    public String getFecha() {
        return fecha;
    }
    
    public Avion getAvion() {
        return avion;
    }
    
    public List<Tiquete> getTiquetes() {
        return tiquetes;
    }
    
    public void agregarTiquete(Tiquete tiquete) {
        tiquetes.add(tiquete);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Vuelo vuelo = (Vuelo) obj;
        return Objects.equals(ruta, vuelo.ruta) && Objects.equals(fecha, vuelo.fecha);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ruta, fecha);}
    
    public boolean venderTiquete(Tiquete tiquete) {
        if (tiquetes.size() < capacidad) {
            tiquetes.add(tiquete);
            return true;
        } else {
            System.out.println("No hay más capacidad en este vuelo.");
            return false;
        }}}
  
