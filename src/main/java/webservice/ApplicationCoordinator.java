package webservice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApplicationCoordinator implements CommandLineRunner{

    public static void main(String[] args){
        SpringApplication.run(ApplicationCoordinator.class, args);
    }

    @Override
    public void run(String... arg0) throws Exception {
    }
}
