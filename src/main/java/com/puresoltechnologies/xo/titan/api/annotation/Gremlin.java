package com.puresoltechnologies.xo.titan.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.buschmais.cdo.spi.annotation.QueryDefinition;

/**
 * Marks an interface or method as a Gremlin query.
 */
@QueryDefinition
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Gremlin {

	/**
	 * @return The CYPHER expression.
	 */
	String value();

}
