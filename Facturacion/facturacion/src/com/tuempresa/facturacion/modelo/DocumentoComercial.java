package com.tuempresa.facturacion.modelo;

import java.math.*;
import java.time.*;
import java.util.*;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.openxava.annotations.*;
import org.openxava.calculators.*;
import org.openxava.jpa.*;

import com.tuempresa.facturacion.calculadores.*;

import lombok.*;

@Entity @Getter @Setter
@View(members = 
        "anyo, numero, fecha,"+
     "datos {"+    
		"cliente;"+
        "detalles;"+
		"observaciones"+
     "}"
 )
abstract public class DocumentoComercial extends Identificable{
	
	@DefaultValueCalculator(CurrentYearCalculator.class)
	@Column(length = 4)
	int anyo;
	
	@Column(length = 6)
	@ReadOnly
	int numero;
	
	@Required
	@DefaultValueCalculator(CurrentLocalDateCalculator.class)
	LocalDate fecha;
	
	@Stereotype("MEMO")
	String observaciones;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@ReferenceView("simple")
	Cliente cliente;
	  
	@ElementCollection
	@ListProperties(
			"producto.numero, producto.descripcion, cantidad, precioPorunidad, " +
			"importe+[" +
			"documentoComercial.porcentajeIVA," +
			"documentoComercial.iva," +
			"documentoComercial.importeTotal" +
		 "]"
			)
	Collection<Detalle>detalles;
	
	@DefaultValueCalculator(CalculadorPorcentajeIVA.class)
	@Digits(integer = 2, fraction=0)
	BigDecimal porcentajeIVA;
	
	
	@ReadOnly
	@Stereotype("DINERO")
	@Calculation("sum(detalles.importe)*porcentajeIVA/100")
	BigDecimal iva;
	
	
	@ReadOnly
	@Stereotype("DINERO")
	@Calculation("sum(detalles.importe)+iva")
	BigDecimal importeTotal;
	
	@PrePersist
	private void calcularNumero() {
		Query query = XPersistence.getManager().createQuery(
				"select max(f.numero) from "+
		        getClass().getSimpleName()+
		        " f where f.anyo = :anyo");
		query.setParameter("anyo", anyo);
		Integer ultimoNomero = (Integer) query.getSingleResult();
		this.numero = ultimoNomero == null ? 1 : ultimoNomero + 1;
	}
	
	@org.hibernate.annotations.Formula("IMPORTETOTAL * 0.18")
	@Setter(AccessLevel.NONE)
	@Stereotype("DINERO")
	BigDecimal beneficioEstimado;
}
