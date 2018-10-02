package stepdefs;


import cucumber.api.java8.En;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import pages.sample.SamplePage;
import springboot.TestApplication;

@ContextConfiguration
@SpringBootTest(
        classes= TestApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class SearchTestSteps implements En {

    @Autowired
    public SamplePage samplePage;

    @Autowired
    public Environment environment;


    public SearchTestSteps()
    {
        When("^User search for Selenium$", () -> {
            // Write code here that turns the phrase above into concrete actions

        });
    }
}
