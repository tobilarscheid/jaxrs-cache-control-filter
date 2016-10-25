package de.tobiaslarscheid.cache.control.interfaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.ws.rs.NameBinding;

import de.tobiaslarscheid.cache.control.CacheControlDirectiveName;

/**
 * Pass to {@link de.tobiaslarscheid.cache.control.interfaces.CacheControlled} to
 * specify what directives to use.
 *
 */
@NameBinding
@Target({ ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheControlDirective {
	CacheControlDirectiveName name();

	/**
	 * This is what you would pass to the {@link javax.ws.rs.core.CacheControl}
	 * 's setter method - Interger.parseInt and Boolean.getBoolean are used to
	 * transform your String into the appropriate type.
	 */
	String value();
}
