package xyz.goatcode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import xyz.goatcode.apis.Youtube;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        Youtube.setSubscribersCount();
        SpringApplication.run(Application.class, args);
   }
}
