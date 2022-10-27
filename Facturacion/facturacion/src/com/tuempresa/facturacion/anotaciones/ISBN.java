package com.tuempresa.facturacion.anotaciones;

import java.lang.annotation.*;

import javax.validation.*;

@Constraint(validatedBy = com.tuempresa.facturacion.validadores.ValidadorISBN.class)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface ISBN {
	
	Class<?>[]groups() default{};
	Class<? extends Payload>[] payload() default{};
	String message() default "isbn_invalido";

}
