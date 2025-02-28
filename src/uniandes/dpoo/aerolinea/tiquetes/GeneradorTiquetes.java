package uniandes.dpoo.aerolinea.tiquetes;

import java.util.HashSet;
import java.util.Set;
import uniandes.dpoo.aerolinea.modelo.Vuelo;
import uniandes.dpoo.aerolinea.modelo.cliente.Cliente;

public class GeneradorTiquetes {
    private static Set<String> codigos = new HashSet<String>();
    
    public static Tiquete generarTiquete(Vuelo vuelo, Cliente cliente, int tarifa) {
        String codigo;
        do {
            codigo = String.format("%07d", (int)(Math.random() * 10000000));
        } while(validarTiquete(codigo));
        Tiquete tiquete = new Tiquete(codigo, vuelo, cliente, tarifa);
        registrarTiquete(tiquete);
        return tiquete;
    }
    
    public static void registrarTiquete(Tiquete tiquete) {
        codigos.add(tiquete.getCodigo());
    }
    
    public static boolean validarTiquete(String codigoTiquete) {
        return codigos.contains(codigoTiquete);
    }
}
