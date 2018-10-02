package springboot;

import components.WebDriverHolder;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

@SpringBootApplication()
@EnableAutoConfiguration()
@ComponentScan({"components","pages"})
public class TestApplication {

    @Autowired
    private Environment environment;

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }


}
