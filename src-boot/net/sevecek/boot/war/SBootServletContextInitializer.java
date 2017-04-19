package net.sevecek.boot.war;

import java.util.*;
import javax.servlet.*;
import org.slf4j.*;
import org.springframework.web.context.support.*;
import org.springframework.web.filter.*;
import org.springframework.web.servlet.*;

public class SBootServletContextInitializer implements ServletContainerInitializer {

    public static final String MVC_CONFIG_LOCATION = "mvcConfigLocation";

    private static Logger logger = LoggerFactory.getLogger(SBootServletContextInitializer.class);

    @Override
    public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
        logger.info("Deploying web application /ROOT");

        String contextConfigLocation = ctx.getInitParameter(MVC_CONFIG_LOCATION);
        if (contextConfigLocation != null) {
            ServletRegistration.Dynamic dispatcherServletRegistration = ctx.addServlet("Spring MVC Entry Point", DispatcherServlet.class);
            dispatcherServletRegistration.setInitParameter("contextConfigLocation", contextConfigLocation);
            dispatcherServletRegistration.setInitParameter("contextClass", AnnotationConfigWebApplicationContext.class.getName());
            dispatcherServletRegistration.setLoadOnStartup(100);
            dispatcherServletRegistration.addMapping("/");
        }

        FilterRegistration.Dynamic characterEncodingFilterRegistration =
                ctx.addFilter("Spring Character Encoding Filter", CharacterEncodingFilter.class);
        characterEncodingFilterRegistration.setInitParameter("encoding", "UTF-8");
        characterEncodingFilterRegistration.setInitParameter("forceEncoding", "true");
        characterEncodingFilterRegistration.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, "/*");
    }
}
