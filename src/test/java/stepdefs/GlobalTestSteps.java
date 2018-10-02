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
        classes = TestApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class GlobalTestSteps implements En {

    @Autowired
    private SamplePage samplePage;

    @Autowired
    private Environment environment;

    public GlobalTestSteps() {

        Given("^User is on the google search page$", () -> samplePage.navigateToGoogle());
        Given("^a web browser is on the aem publisher page$", () -> samplePage.navigateToPublisher());
        Given("^User is on the aem \"([^\"]*)\" page$", (String pubUrl) ->
        {
            samplePage.navigateToProbPublisher(pubUrl);

        });
        When("^author enters userid and password$", () -> {
          });




    }
}
