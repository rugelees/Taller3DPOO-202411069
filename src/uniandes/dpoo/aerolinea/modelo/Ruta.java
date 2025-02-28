package uniandes.dpoo.aerolinea.modelo;

public class Ruta {
    private String codigoRuta;
    private Aeropuerto origen;
    private Aeropuerto destino;
    private String horaSalida;
    private String horaLlegada;
    
    public Ruta(Aeropuerto origen, Aeropuerto destino, String horaSalida, String horaLlegada, String codigoRuta) {
        this.origen = origen;
        this.destino = destino;
        this.horaSalida = horaSalida;
        this.horaLlegada = horaLlegada;
        this.codigoRuta = codigoRuta;
    }
    
    public String getCodigoRuta() {
        return codigoRuta;
    }
    
    public Aeropuerto getOrigen() {
        return origen;
    }
    
    public Aeropuerto getDestino() {
        return destino;
    }
    
    public String getHoraSalida() {
        return horaSalida;
    }
    
    public String getHoraLlegada() {
        return horaLlegada;
    }
    
    public static int getMinutos(String horaCompleta) {
        int minutos = Integer.parseInt(horaCompleta) % 100;
        return minutos;
    }
    
    public static int getHoras(String horaCompleta) {
        int horas = Integer.parseInt(horaCompleta) / 100;
        return horas;
    }
}
