package com.tuempresa.facturacion.modelo;

import javax.persistence.*;
import javax.persistence.Entity;

import org.hibernate.annotations.*;
import org.openxava.annotations.*;

import lombok.*;

@Entity @Getter @Setter
public class Categoria extends Identificable{
	
	
	@Column(length=50)
	String descripcion;

}
