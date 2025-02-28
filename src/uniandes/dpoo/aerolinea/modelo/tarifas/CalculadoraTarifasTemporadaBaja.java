package uniandes.dpoo.aerolinea.modelo.tarifas;

import uniandes.dpoo.aerolinea.modelo.Ruta;
import uniandes.dpoo.aerolinea.modelo.Vuelo;
import uniandes.dpoo.aerolinea.modelo.cliente.Cliente;
import uniandes.dpoo.aerolinea.modelo.cliente.ClienteNatural;
import uniandes.dpoo.aerolinea.modelo.cliente.ClienteCorporativo;

public class CalculadoraTarifasTemporadaBaja extends CalculadoraTarifas {
    protected final int COSTO_POR_KM_NATURAL = 8;
    protected final int COSTO_POR_KM_CORPORATIVO = 12;
    protected final double DESCUENTO_GRANDES = 0.20;
    protected final double DESCUENTO_MEDIANAS = 0.10;
    protected final double DESCUENTO_PEQ = 0.05;

    @Override
    public int calcularCostoBase(Vuelo vuelo, Cliente cliente) {
        Ruta ruta = vuelo.getRuta();
        int distancia = calcularDistanciaVuelo(ruta);
        if(cliente.getTipoCliente().equals(ClienteNatural.NATURAL))
            return COSTO_POR_KM_NATURAL * distancia;
        else
            return COSTO_POR_KM_CORPORATIVO * distancia;
    }

    @Override
    public double calcularPorcentajeDescuento(Cliente cliente) {
        if(cliente.getTipoCliente().equals(ClienteNatural.NATURAL))
            return 0.0;
        else {
            int tam = ((ClienteCorporativo)cliente).getTamanoEmpresa();
            if(tam >= 3)
                return DESCUENTO_GRANDES;
            else if(tam == 2)
                return DESCUENTO_MEDIANAS;
            else
                return DESCUENTO_PEQ;
        }
    }
}
