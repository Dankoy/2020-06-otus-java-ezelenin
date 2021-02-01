package ru.dankoy.otus.warmvc;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.dankoy.otus.warmvc.core.model.User;
import ru.dankoy.otus.warmvc.flyway.MigrationsExecutor;
import ru.dankoy.otus.warmvc.flyway.MigrationsExecutorFlyway;
import ru.dankoy.otus.warmvc.hibernate.utils.HibernateUtils;

@Configuration
public class HibernateConfig implements WebMvcConfigurer {

    public static final String HIBERNATE_CFG_FILE = "WEB-INF/hibernate.cfg.xml";

    @Bean(initMethod = "executeMigrations")
    public MigrationsExecutor migrationsExecutor() {
        return new  MigrationsExecutorFlyway(HIBERNATE_CFG_FILE);
    }

    @Bean
    @DependsOn("migrationsExecutor")
    public SessionFactory sessionFactory() {
        return HibernateUtils.buildSessionFactory(HIBERNATE_CFG_FILE, User.class);
    }

}
