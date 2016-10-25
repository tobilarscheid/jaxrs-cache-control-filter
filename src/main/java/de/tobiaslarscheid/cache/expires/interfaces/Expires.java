package de.tobiaslarscheid.cache.expires.interfaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.ws.rs.NameBinding;

/**
 * Annotate this to your JAX-RS Resource Method to add Expires headers.
 *
 */
@NameBinding
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Expires {
	/**
	 * RFC 1123 Datetime when this resource will Expire. Hint: Use
	 * {@link java.time.format.DateTimeFormatter.RFC_1123_DATE_TIME} for easy
	 * formatting
	 */
	String value();
}
