package de.tobiaslarscheid.cache.control.interfaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.ws.rs.NameBinding;

/**
 * Annotate this to your JAX-RS Resource Method to add cache-control headers.
 * Specify which cache-control directives to use in the value.
 *
 */
@NameBinding
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheControlled {
	/**
	 * Array of
	 * {@link de.tobiaslarscheid.cache.control.interfaces.CacheControlDirective}
	 * Annotations specifying which cache-control directives to include in the
	 * set header.
	 */
	CacheControlDirective[] value();
}
