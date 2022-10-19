package com.tuempresa.facturacion.modelo;

import java.math.*;

import javax.persistence.*;

import org.openxava.annotations.*;

import com.tuempresa.facturacion.calculadores.*;

import lombok.*;

@Embeddable @Getter @Setter
public class Detalle {

	int cantidad;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	Producto producto;
	
	@Stereotype("DINERO")
	@Depends("precioPorunidad, cantidad")
	public BigDecimal getImporte () {
		if (precioPorunidad == null) return BigDecimal.ZERO;
		return new BigDecimal(cantidad).multiply(precioPorunidad); 
	}
	
	@DefaultValueCalculator(value = CalculadorPrecioPorUnidad.class,
			properties = @PropertyValue(name = "numeroProducto", from = "producto.numero")
	
	)
	
	@Stereotype("DINERO")
	BigDecimal precioPorunidad;
}
