package uniandes.dpoo.aerolinea.modelo.cliente;

import org.json.JSONException;
import org.json.JSONObject;

public class ClienteCorporativo extends Cliente {
    public static final String CORPORATIVO = "CORPORATIVO";
    public static final int GRANDE = 3;
    public static final int MEDIANA = 2;
    public static final int PEQUENA = 1;
    
    private String nombreEmpresa;
    private int tamanoEmpresa;
    
    public ClienteCorporativo(String nombreEmpresa, int tamanoEmpresa) {
        super();
        this.nombreEmpresa = nombreEmpresa;
        this.tamanoEmpresa = tamanoEmpresa;
    }
    
    public String getNombreEmpresa() {
        return nombreEmpresa;
    }
    
    public int getTamanoEmpresa() {
        return tamanoEmpresa;
    }
    
    public static Cliente cargarDesdeJSON(JSONObject json) throws JSONException {
        if (!json.getString("tipoCliente").equalsIgnoreCase("CORPORATIVO")) {
            throw new JSONException("El tipo de cliente no es CORPORATIVO.");
        }
        
        if (!json.has("nombreEmpresa")) {
            throw new JSONException("La clave 'nombreEmpresa' no se encontró.");
        }
        String nombreEmpresa = json.getString("nombreEmpresa");
        
        if (!json.has("tamanoEmpresa")) {
            throw new JSONException("La clave 'tamanoEmpresa' no se encontró.");
        }
        int tamanoEmpresa = json.getInt("tamanoEmpresa");
        
        return new ClienteCorporativo(nombreEmpresa, tamanoEmpresa);
    }
    
    public JSONObject salvarEnJSON() {
        JSONObject jobject = new JSONObject();
        jobject.put("nombreEmpresa", this.nombreEmpresa);
        jobject.put("tamanoEmpresa", this.tamanoEmpresa);
        jobject.put("tipo", CORPORATIVO);
        return jobject;
    }

	@Override
	public String getIdentificador() {
		// TODO Auto-generated method stub
		return nombreEmpresa;
	}

	@Override
	public String getTipoCliente() {
		// TODO Auto-generated method stub
		return CORPORATIVO;
	}
}
