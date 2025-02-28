package uniandes.dpoo.aerolinea.modelo.tarifas;

import uniandes.dpoo.aerolinea.modelo.Aeropuerto;
import uniandes.dpoo.aerolinea.modelo.Ruta;
import uniandes.dpoo.aerolinea.modelo.Vuelo;
import uniandes.dpoo.aerolinea.modelo.cliente.Cliente;

public abstract class CalculadoraTarifas {
    public static final double IMPUESTO = 0.28;

    public abstract int calcularCostoBase(Vuelo vuelo, Cliente cliente);
    public abstract double calcularPorcentajeDescuento(Cliente cliente);

    protected int calcularDistanciaVuelo(Ruta ruta) {
        return Aeropuerto.calcularDistancia(ruta.getOrigen(), ruta.getDestino());
    }

    public int calcularTarifa(Vuelo vuelo, Cliente cliente) {
        int costoBase = calcularCostoBase(vuelo, cliente);
        double descuento = costoBase * calcularPorcentajeDescuento(cliente);
        int valorImpuestos = calcularValorImpuestos(costoBase - (int)descuento);
        return costoBase - (int)descuento + valorImpuestos;
    }

    protected int calcularValorImpuestos(int costoBaseMenosDescuento) {
        return (int)Math.round(costoBaseMenosDescuento * IMPUESTO);
    }
}
