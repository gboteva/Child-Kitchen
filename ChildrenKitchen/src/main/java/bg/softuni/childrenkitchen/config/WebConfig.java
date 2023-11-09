package bg.softuni.childrenkitchen.config;


import bg.softuni.childrenkitchen.service.StatisticInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final StatisticInterceptor statisticInterceptor;

    public WebConfig(StatisticInterceptor statisticInterceptor) {
        this.statisticInterceptor = statisticInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(statisticInterceptor).addPathPatterns("/menus");;
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
