package de.tobiaslarscheid.cache.expires;

import java.io.IOException;
import java.lang.annotation.Annotation;

import javax.ws.rs.GET;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.tobiaslarscheid.cache.control.interfaces.CacheControlled;
import de.tobiaslarscheid.cache.expires.interfaces.Expires;

@Provider
@CacheControlled({})
public class ExpiresFilter implements ContainerResponseFilter {

	private static final Logger logger = LoggerFactory.getLogger(ExpiresFilter.class);

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
			throws IOException {
		if (!HttpMethod.GET.equals(requestContext.getMethod())) {
			logger.error("Request's http method was " + requestContext.getMethod() + ", but expected " + HttpMethod.GET
					+ "! Please make sure you only add @" + CacheControlled.class.getName() + " together with @"
					+ GET.class.getName() + ". No cache-control header is added!");
			return;
		}
		if (responseContext.getStatus() != Response.Status.OK.getStatusCode()) {
			logger.debug("Response status code is not 200, ignoring.");
			return;
		}
		Expires annotation = findExpiresAnnotation(responseContext);
		responseContext.getHeaders().add(HttpHeaders.EXPIRES, annotation.value());
	}

	private Expires findExpiresAnnotation(ContainerResponseContext responseContext) {
		for (Annotation annotation : responseContext.getEntityAnnotations()) {
			if (annotation instanceof Expires) {
				return (Expires) annotation;
			}
		}
		throw new IllegalStateException("@Expires annotation not found");
	}
}
