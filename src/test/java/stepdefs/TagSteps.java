package stepdefs;


import components.TestExecutionData;
import components.WebDriverHolder;
import cucumber.api.Scenario;
import cucumber.api.java8.En;
import logs.Logger;
import org.apache.commons.lang.ArrayUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import pages.sample.SamplePage;
import springboot.TestApplication;

import java.net.URL;
import java.util.concurrent.TimeUnit;

@ContextConfiguration
@SpringBootTest(
        classes = TestApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class TagSteps implements En {

    @Autowired
    private WebDriverHolder webDriverHolder;

    @Autowired
    private TestExecutionData testExecutionData;

    @Autowired
    private SamplePage samplePage;

    @Autowired
    private Environment environment;

    public TagSteps() {

        Before(1, (Scenario scenario) -> {
            Logger.info("****** Starting scenario: " + scenario.getName() + " ******");
            testExecutionData.scenario = scenario;
        });

        Before(new String[]{"@web"}, 0, 2, () -> {
            Logger.info("****** Before @web ******");
            System.out.println("ActiveProfiles: ");

            String[] test = environment.getActiveProfiles();

            for (String profile : environment.getActiveProfiles()) {
                System.out.println(profile + " ");
            }


            if (ArrayUtils.contains(environment.getActiveProfiles(), "ie11")) {
                Logger.info("Starting InternetExplorerDriver");
                System.setProperty("webdriver.ie.driver", environment.getProperty("webdriver.ie.driver"));
                System.setProperty("webdriver.ie.driver.extractpath", environment.getProperty("webdriver.ie.driver.extractpath"));
                webDriverHolder.driver = new InternetExplorerDriver();
                webDriverHolder.driver.manage().window().maximize();
            }
            if (ArrayUtils.contains(environment.getActiveProfiles(), "chrome")) {
                Logger.info("Starting ChromeDriver");
                System.setProperty("webdriver.chrome.driver", environment.getProperty("webdriver.chrome.driver"));
                System.setProperty("webdriver.chrome.driver.extractpath", environment.getProperty("webdriver.chrome.driver.extractpath"));
                webDriverHolder.driver = new ChromeDriver();
                webDriverHolder.driver.manage().window().maximize();
            }
            if (ArrayUtils.contains(environment.getActiveProfiles(), "browserstack")) {
                Logger.info("Starting Browserstack WebDriver");

                String URL = "https://" + environment.getProperty("browserstack.username") + ":" + environment.getProperty("browserstack.accesskey") + "@hub-cloud.browserstack.com/wd/hub";
                Logger.info("Browserstack URL:" + URL);
                String browserstackLocalIdentifier = System.getenv("BROWSERSTACK_LOCAL_IDENTIFIER");
                DesiredCapabilities caps = new DesiredCapabilities();
                if (environment.getProperty("browserstack_isMobile").equals("false"))
                {
                    caps.setCapability("os", environment.getProperty("browserstack.os"));
                    caps.setCapability("os_version", environment.getProperty("browserstack.os_version"));
                    caps.setCapability("browser", environment.getProperty("browserstack.browser"));
                    caps.setCapability("browser_version", environment.getProperty("browserstack.browser_version"));
                    caps.setCapability("browserstack.local", environment.getProperty("browserstack.local"));
                    caps.setCapability("browserstack.localIdentifier", browserstackLocalIdentifier);
                    caps.setCapability("resolution", environment.getProperty("browserstack.resolution"));
                    caps.setCapability("browserstack.selenium_version", environment.getProperty("browserstack.selenium_version"));
                    caps.setCapability("browserstack.video", environment.getProperty("browserstack.video"));
                    caps.setCapability("name", testExecutionData.getScenarioName());
                    caps.setCapability("build", testExecutionData.getBuild());
                    caps.setCapability("project", environment.getProperty("project.name"));
                } else {
                    caps.setCapability("browserName", environment.getProperty("browserstack.browserName"));
                    caps.setCapability("device", environment.getProperty("browserstack.device"));
                    caps.setCapability("realMobile", "true");
                    caps.setCapability("os_version", environment.getProperty("browserstack.os_version"));
                    caps.setCapability("browserstack.local", environment.getProperty("browserstack.local"));
                    caps.setCapability("browserstack.selenium_version", environment.getProperty("browserstack.selenium_version"));
                    caps.setCapability("browserstack.video", environment.getProperty("browserstack.video"));
                    caps.setCapability("name", testExecutionData.getScenarioName());
                    caps.setCapability("build", testExecutionData.getBuild());
                    caps.setCapability("project", environment.getProperty("project.name"));
                    caps.setCapability("nativeWebTap", true);
                }

                URL browserStackURL = new URL(URL);
                Logger.info("Caps: " + caps.toString());
                webDriverHolder.driver = new RemoteWebDriver(browserStackURL, caps);
                Logger.info("****** End @web ******");
            }
            Dimension d = new Dimension(1200, 800);

            if(webDriverHolder.driver==null) Logger.info("WebDriver is null");
            webDriverHolder.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            webDriverHolder.driver.manage().window().setSize(d);
            webDriverHolder.driver.manage().deleteAllCookies();
        });

        After(new String[]{"@web"}, 0, 9, (Scenario scenario) -> {
            Logger.info("******  After @web ******");
            samplePage.takeScreenShot(scenario);

            //close the webdriver and attempt to quit - found that the using only quit causes an error 1 out of 10 times
            webDriverHolder.driver.close();

            try {
                webDriverHolder.driver.quit();
            } catch (UnsatisfiedLinkError | NoClassDefFoundError e) {
                Logger.debug(e.toString());
            }
        });

    }
}
