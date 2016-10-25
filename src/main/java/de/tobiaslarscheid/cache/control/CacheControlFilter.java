package de.tobiaslarscheid.cache.control;

import java.io.IOException;
import java.lang.annotation.Annotation;

import javax.ws.rs.GET;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.tobiaslarscheid.cache.control.interfaces.CacheControlDirective;
import de.tobiaslarscheid.cache.control.interfaces.CacheControlled;

@Provider
@CacheControlled({})
public class CacheControlFilter implements ContainerResponseFilter {

	private static final Logger logger = LoggerFactory.getLogger(CacheControlFilter.class);

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
		CacheControlled annotation = findCacheControlledAnnotation(responseContext);
		CacheControl cc = buildCacheControlFromAnnotation(annotation);
		responseContext.getHeaders().add(HttpHeaders.CACHE_CONTROL, cc.toString());
	}

	private CacheControlled findCacheControlledAnnotation(ContainerResponseContext responseContext) {
		for (Annotation annotation : responseContext.getEntityAnnotations()) {
			if (annotation instanceof CacheControlled) {
				return (CacheControlled) annotation;
			}
		}
		throw new IllegalStateException("@CacheControlled annotation not found");
	}

	private CacheControl buildCacheControlFromAnnotation(CacheControlled annotation) {
		CacheControl cc = new CacheControl();
		for (CacheControlDirective directive : annotation.value()) {
			switch (directive.name()) {
			case MAX_AGE:
				logger.debug("Found MAX_AGE, setting to " + directive.value());
				cc.setMaxAge(Integer.parseInt(directive.value()));
				break;
			case MUST_REVALIDATE:
				logger.debug("Found MUST_REVALIDATE, setting to " + directive.value());
				cc.setMustRevalidate(Boolean.getBoolean(directive.value()));
				break;
			case NO_CACHE:
				logger.debug("Found NO_CACHE, setting to " + directive.value());
				cc.setNoCache(Boolean.getBoolean(directive.value()));
				break;
			case NO_STORE:
				logger.debug("Found NO_STORE, setting to " + directive.value());
				cc.setNoStore(Boolean.getBoolean(directive.value()));
				break;
			case NO_TRANSFORM:
				logger.debug("Found NO_TRANSFORM, setting to " + directive.value());
				cc.setNoTransform(Boolean.getBoolean(directive.value()));
				break;
			case PRIVATE:
				logger.debug("Found PRIVATE, setting to " + directive.value());
				cc.setPrivate(Boolean.getBoolean(directive.value()));
				break;
			case PROXY_REVALIDATE:
				logger.debug("Found PROXY_REVALIDATE, setting to " + directive.value());
				cc.setProxyRevalidate(Boolean.getBoolean(directive.value()));
				break;
			case S_MAX_AGE:
				logger.debug("Found S_MAX_AGE, setting to " + directive.value());
				cc.setSMaxAge(Integer.parseInt(directive.value()));
				break;
			default:
				break;
			}
		}
		return cc;
	}

}
