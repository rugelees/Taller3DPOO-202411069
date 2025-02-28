package uniandes.dpoo.aerolinea.persistencia;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import uniandes.dpoo.aerolinea.exceptions.ClienteRepetidoException;
import uniandes.dpoo.aerolinea.exceptions.InformacionInconsistenteException;
import uniandes.dpoo.aerolinea.exceptions.InformacionInconsistenteTiqueteException;
import uniandes.dpoo.aerolinea.modelo.Aerolinea;
import uniandes.dpoo.aerolinea.modelo.Ruta;
import uniandes.dpoo.aerolinea.modelo.Vuelo;
import uniandes.dpoo.aerolinea.modelo.cliente.Cliente;
import uniandes.dpoo.aerolinea.modelo.cliente.ClienteCorporativo;
import uniandes.dpoo.aerolinea.modelo.cliente.ClienteNatural;
import uniandes.dpoo.aerolinea.tiquetes.GeneradorTiquetes;
import uniandes.dpoo.aerolinea.tiquetes.Tiquete;

public class PersistenciaTiquetesJson implements IPersistenciaTiquetes {
    private static final String NOMBRE_CLIENTE = "nombre";
    private static final String TIPO_CLIENTE = "tipoCliente";
    private static final String CLIENTE = "cliente";
    private static final String USADO = "usado";
    private static final String TARIFA = "tarifa";
    private static final String CODIGO_TIQUETE = "codigoTiquete";
    private static final String FECHA = "fecha";
    private static final String CODIGO_RUTA = "codigoRuta";

    // Método para leer el contenido de un archivo
    private String leerArchivo(String archivo) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(archivo));
        String linea;
        while ((linea = br.readLine()) != null) {
            sb.append(linea);
        }
        br.close();
        return sb.toString();
    }
    
    @Override
    public void cargarTiquetes(String archivo, Aerolinea aerolinea) throws IOException {
        String contenido = leerArchivo(archivo);
        JSONObject json = new JSONObject(contenido);

        if (json.has("clientes")) {
            JSONArray clientesArray = json.getJSONArray("clientes");
            for (int i = 0; i < clientesArray.length(); i++) {
                JSONObject clienteJson = clientesArray.getJSONObject(i);
                String tipo = clienteJson.getString("tipoCliente").trim();
                Cliente cliente;
                if (tipo.equalsIgnoreCase("CORPORATIVO")) {
                    String nombreEmpresa = clienteJson.getString("nombreEmpresa").trim();
                    int tamanoEmpresa = clienteJson.getInt("tamanoEmpresa");
                    cliente = new ClienteCorporativo(nombreEmpresa, tamanoEmpresa);
                } else if (tipo.equalsIgnoreCase("NATURAL")) {
                    String nombre = clienteJson.getString("nombre").trim();
                    cliente = new ClienteNatural(nombre);
                } else {
                    throw new JSONException("Tipo de cliente desconocido: " + tipo);
                }
                aerolinea.agregarCliente(cliente);
            }
        }
    }

    @Override
    public void salvarTiquetes(String archivo, Aerolinea aerolinea) throws IOException, JSONException {
        JSONObject jobject = new JSONObject();
        salvarClientesJSON(aerolinea, jobject);
        salvarTiquetesJSON(aerolinea, jobject);
        PrintWriter pw = new PrintWriter(new FileWriter(archivo));
        jobject.write(pw, 2, 0);
        pw.close();
    }

    private void salvarClientesJSON(Aerolinea aerolinea, JSONObject jobject) throws JSONException {
        JSONArray clientesArray = new JSONArray();
        Collection<Cliente> clientes = aerolinea.getClientes();
        for (Cliente cliente : clientes) {
            JSONObject clienteJson = new JSONObject();
            clienteJson.put("tipoCliente", cliente.getTipoCliente());
            if (cliente.getTipoCliente().equalsIgnoreCase("CORPORATIVO")) {
                ClienteCorporativo corporativo = (ClienteCorporativo) cliente;
                clienteJson.put("nombreEmpresa", corporativo.getNombreEmpresa());
                clienteJson.put("tamanoEmpresa", corporativo.getTamanoEmpresa());
            } else if (cliente.getTipoCliente().equalsIgnoreCase("NATURAL")) {
                ClienteNatural natural = (ClienteNatural) cliente;
                clienteJson.put("nombre", natural.getNombre());
            }
            clientesArray.put(clienteJson);
        }
        jobject.put("clientes", clientesArray);
    }
    
    private void salvarTiquetesJSON(Aerolinea aerolinea, JSONObject jobject) throws JSONException {
        JSONArray tiquetesArray = new JSONArray();
        Collection<Cliente> clientes = aerolinea.getClientes();
        for (Cliente cliente : clientes) {
            for (Tiquete tiquete : cliente.getTiquetesSinUsar()) {
                tiquetesArray.put(tiquete.salvarEnJSON());
            }
            for (Tiquete tiquete : cliente.getTiquetesUsados()) {
                tiquetesArray.put(tiquete.salvarEnJSON());
            }
        }
        jobject.put("tiquetes", tiquetesArray);
    }
}
