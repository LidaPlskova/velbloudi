package net.sevecek.boot.war;

import java.nio.charset.*;
import java.util.*;
import org.springframework.context.annotation.*;
import org.springframework.http.*;
import org.springframework.http.converter.*;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.mvc.*;

public class DefaultWebMvcConfigurer extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        WebContentInterceptor interceptor = new WebContentInterceptor();
        interceptor.addCacheMapping(CacheControl.noStore(), "/**");
        registry.addInterceptor(interceptor);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("/")
                .setCacheControl(CacheControl.noStore());
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        for (int i = 0; i < converters.size(); i++) {
            HttpMessageConverter<?> converter = converters.get(i);
            if (converter instanceof StringHttpMessageConverter) {
                StringHttpMessageConverter newConverter =
                        new StringHttpMessageConverter(StandardCharsets.UTF_8);
                newConverter.setWriteAcceptCharset(false);
                converters.set(i, newConverter);
            }
        }
    }

}
