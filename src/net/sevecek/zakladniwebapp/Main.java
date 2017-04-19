package net.sevecek.zakladniwebapp;

import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.*;
import net.sevecek.boot.standalone.*;
import net.sevecek.boot.war.*;

@Configuration
@ComponentScan
@EnableWebMvc
public class Main extends DefaultWebMvcConfigurer {

    public static void main(String[] args) {
        TomcatApplication.run();
    }

}
