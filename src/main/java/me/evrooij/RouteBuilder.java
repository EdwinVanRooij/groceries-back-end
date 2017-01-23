package me.evrooij;

import io.swagger.annotations.Api;
import org.reflections.Reflections;
import spark.Route;

import javax.ws.rs.*;
import java.lang.reflect.Method;
import java.util.Set;

import static spark.Spark.*;

public class RouteBuilder {

	public static void setupRoutes(String packageName) throws InstantiationException, IllegalAccessException {

		Reflections reflections = new Reflections(packageName);
		Set<Class<?>> apiRoutes = reflections.getTypesAnnotatedWith(Api.class);

		for (Class<?> clazz : apiRoutes) {
			Route sparkRoute = (Route) clazz.newInstance();
			Path path = clazz.getAnnotation(Path.class);
			Method[] methods = clazz.getMethods();
			for (Method method : methods) {
				POST post = method.getAnnotation(POST.class);
				String friendlyRoute = path.value().replaceAll("\\{(.*?)\\}", ":$1");
				if (post != null) {
					post(friendlyRoute, sparkRoute);
					break;
				}

				GET get = method.getAnnotation(GET.class);
				if (get != null) {
					get(friendlyRoute, sparkRoute);
					break;
				}

				DELETE delete = method.getAnnotation(DELETE.class);
				if (delete != null) {
					delete(friendlyRoute, sparkRoute);
					break;
				}

				PUT put = method.getAnnotation(PUT.class);
				if (put != null) {
					put(friendlyRoute, sparkRoute);
					break;
				}
			}

		}
	}

}
