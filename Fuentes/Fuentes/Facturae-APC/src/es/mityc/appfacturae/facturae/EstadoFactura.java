package es.mityc.appfacturae.facturae;

public class EstadoFactura {

	private Short estado;
	
	private Short clase;
	
	public EstadoFactura(Short estado, Short clase){
		this.estado = estado;
		this.clase = clase;
	}

	public Short getEstado() {
		return estado;
	}

	public void setEstado(Short estado) {
		this.estado = estado;
	}

	public Short getClase() {
		return clase;
	}

	public void setClase(Short clase) {
		this.clase = clase;
	}
}
