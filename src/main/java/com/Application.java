package com;

import com.redphase.framework.util.EhcacheUtil;
import com.redphase.framework.util.I18nUtil;
import com.redphase.framework.util.JwtUtil;
import com.redphase.view.CustomSplash;
import com.redphase.view.LoginView;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

//@SpringBootApplication(scanBasePackages = {"com.redphase.framework.util", "com.redphase.aspect", "com.redphase.controller", "com.redphase.dao", "com.redphase.framework.config", "com.redphase.service", "com.redphase.view", "com.redphase.httpserver"})
//@SpringBootApplication(scanBasePackages = {"com.redphase.controller", "com.redphase.dao", "com.redphase.service", "com.redphase.view", "com.redphase.aspect", "com.redphase.httpserver"})
@ComponentScan({"com.redphase.framework.config","com.redphase.controller","com.redphase.service", "com.redphase.view", "com.redphase.aspect", "com.redphase.httpserver"})
@Configuration
@EnableAutoConfiguration
@EnableCaching
@Slf4j
public class Application extends AbstractJavaFxApplicationSupport {
    public static void main(String[] args) {
        log.debug("Application ..start..");
        CustomSplash customSplash = new CustomSplash();
        customSplash.setImagePath("/loading.gif");
        customSplash.setVisible(true);
        launch(Application.class, LoginView.class, customSplash, args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        super.start(stage);
        stage.setTitle(I18nUtil.get("title"));
        stage.setOnCloseRequest((event -> {
            log.debug("exit");
            System.exit(0);
        }));
    }

    @Bean
    public EhcacheUtil getEhcacheUtil() {
        return new EhcacheUtil();
    }

    @Bean
    public JwtUtil getJwtUtil() {
        return new JwtUtil();
    }
}
