package com.tuempresa.facturacion.modelo;

import java.time.*;
import java.util.*;

import javax.persistence.*;
import javax.persistence.Entity;

import org.hibernate.annotations.*;
import org.openxava.annotations.*;
import org.openxava.calculators.*;

import com.tuempresa.facturacion.calculadores.*;

import lombok.*;

@Entity @Getter @Setter
@View(members = 
      "anyo, numero, fecha;" +
      "cliente;" +
      "detalle;" +
      "observaciones"
      )
public class Factura {
	
	@Id
	@GeneratedValue(generator="System-uuid")
	@Hidden
	@GenericGenerator(name="System-uuid", strategy="uuid")
	@Column(length=32)
	String oid;
	
	@Column(length=4)
	@DefaultValueCalculator(CurrentYearCalculator.class)
	int anyo;
	
	@Column(length=6)
	@DefaultValueCalculator(value = CalculadorSiguienteNumeroParaAnyo.class,
	properties = @PropertyValue(name = "anyo"))
	int numero;
	
	@Required
	@DefaultValueCalculator(CurrentLocalDateCalculator.class)
	LocalDate fecha;
	
	@ManyToOne (fetch = FetchType.LAZY, optional = false)
	@ReferenceView("Simple")
	Cliente cliente;
	
	@ElementCollection
	@ListProperties("producto.numero, producto.descripcion, cantidad")
	Collection <Detalle> detalles;
	
	@Stereotype("MEMO")
	String observaciones;

}