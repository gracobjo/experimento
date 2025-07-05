package es.mityc.appfacturae.ui.utils;

public class AddressTransporter {
	
	private String direccion;
	
	private String codigoPostal;
	
	private String poblacion;
	
	private String provincia;

	public AddressTransporter(String direccion, String codigoPostal, String poblacion, String provincia) {
		super();
		this.direccion = direccion;
		this.codigoPostal = codigoPostal;
		this.poblacion = poblacion;
		this.provincia = provincia;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getCodigoPostal() {
		return codigoPostal;
	}

	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}

	public String getPoblacion() {
		return poblacion;
	}

	public void setPoblacion(String poblacion) {
		this.poblacion = poblacion;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}
}
