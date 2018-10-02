package pages.base;



import components.WebDriverHolder;
import cucumber.api.DataTable;
import cucumber.api.Scenario;
import logs.Logger;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.WebElement;


public abstract class BasePage {

    @Autowired
    public WebDriverHolder webDriverHolder;

    @Autowired
    public Environment environment;


    public BasePage() {
    }

    public Boolean isEnabled(String xpath) {
        WebElement element = webDriverHolder.driver.findElement(By.xpath(xpath));
        return element.isEnabled();
    }

    public DataTable createTable(List<String> columns, By byTable, By byRow, By byCell) {
        WebElement myTable = webDriverHolder.driver.findElement(byTable);
        //To locate rows of table.
        List<WebElement> rows_table = myTable.findElements(byRow);

        //To calculate no of rows In table.
        int rows_count = rows_table.size();

        List<List<String>> tierRaw = new ArrayList<List<String>>();

        tierRaw.add(columns);

        //Loop will execute for all the rows of the table
        for (WebElement aRows_table : rows_table) {
            List<String> newRow = new ArrayList<String>();
            //To locate columns(cells) of that specific row.
            List<WebElement> Columns_row = aRows_table.findElements(byCell);

            //To calculate no of columns(cells) In that specific row.
            int columns_count = Columns_row.size();

            //Loop will execute till the last cell of that specific row.
            for (WebElement aColumns_row : Columns_row) {
                //To retrieve text from the cells.
                newRow.add(aColumns_row.getText().trim());
            }
            tierRaw.add(newRow);
        }

        return DataTable.create(tierRaw);
    }

    public void wpsWait(long timeSeconds) {

        try {
            Thread.sleep(timeSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public List<String> getList(String xpath) {
        List<WebElement> elements = webDriverHolder.driver.findElements(By.xpath(xpath));
        List<String> list = new ArrayList<String>();

        for (WebElement element : elements) {
            list.add(element.getText().trim());
        }

        return list;
    }

    public String getH1() {
        return getText("//h1");
    }

    public String getTitle() {
        return webDriverHolder.driver.getTitle();
    }

    public String getURL() {
        return webDriverHolder.driver.getCurrentUrl();
    }

    public WebElement element(String xpath) {
        WebElement element = webDriverHolder.driver.findElement(By.xpath(xpath));
        return driverWait(10).until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
    }

    public String getPage() {
        return webDriverHolder.driver.getCurrentUrl();
    }

    public String getText(By by) {
        return driverWait(10).until(ExpectedConditions.elementToBeClickable(by)).getText().trim();
    }

    public String getText(String xpath) {
        WebElement element = webDriverHolder.driver.findElement(By.xpath(xpath));
        return driverWait(10).until(ExpectedConditions.elementToBeClickable(element)).getText().trim();
    }

    public String getText(String xpath, String param, String paramValue) {
        return getText(xpath.replace(param, paramValue));
    }

    public String getAttribute(String xpath, String attributeName) {
        WebElement element = webDriverHolder.driver.findElement(By.xpath(xpath));
        return driverWait(10).until(ExpectedConditions.elementToBeClickable(element)).getAttribute(attributeName).trim();
    }

    public WebDriverWait driverWait(long timeoutSeconds) {
        return new WebDriverWait(webDriverHolder.driver, timeoutSeconds);
    }

    public WebElement clickWebElement(By by) {
        WebElement element = driverWait(10).until(ExpectedConditions.elementToBeClickable(by));
        element.click();
        return element;
    }

    public WebElement clickWebElement(String xpath) {
        WebElement element = null;
        for (int i = 0; i < 3; i++) {
            try {
                Logger.info("Entered into for loop to Click Webelement..!!!" + i);
                element = webDriverHolder.driver.findElement(By.xpath(xpath));
                scrollInViewByWebElement(element);
                element = driverWait(45).ignoring(StaleElementReferenceException.class)
                        .until(ExpectedConditions.refreshed(ExpectedConditions.elementToBeClickable(element)));

                element.click();
                break;
            } catch (Exception e) {
            }

        }
        return element;
    }


    public void maximizeWindow() {
        webDriverHolder.driver.manage().window().maximize();
    }

    public WebElement clickWebElement(String xpath, String param, String paramValue) {
        return clickWebElement(xpath.replace(param, paramValue));
    }

    public WebElement clickWebElementbyTitle(String title) {

        return clickWebElement(".//*[@title='" + title + "']");
    }

    public WebElement enterValue(WebElement element, String value) {
        scrollInViewByWebElement(element);
        WebElement field = driverWait(10).until(ExpectedConditions.elementToBeClickable(element));

        element.clear();
        element.sendKeys(value);
        return element;
    }


    public WebElement enterValue(By by, String value) {
        WebElement element = driverWait(5).until(ExpectedConditions.elementToBeClickable(by));
        return enterValue(element, value);
    }

    public WebElement enterValue(String xpath, String value) {

        Logger.info("The value about to enter is..." + value);
        WebElement element = driverWait(5).ignoring(TimeoutException.class).
                until(ExpectedConditions.refreshed(ExpectedConditions.elementToBeClickable(By.xpath(xpath))));
        Logger.info("Value of element is ::" + element);
        return enterValue(element, value);
    }

    public WebElement enterValue(String xpath, String value, String param, String paramValue) {
        return enterValue(xpath.replace(param, paramValue), value);
    }

    public boolean pageTitleContains(String phrase) {
        try {
            return driverWait(5).until(ExpectedConditions.titleContains(phrase));
        } catch (TimeoutException ex) {
            return false;
        }
    }


    /* FUNCTION NAME: navigateToAuthor
     * PARAMETERS USED: NA
     * WORKING: This function navigates the user from current page to the Author AEM page
     * */
    public void navigateToGoogle() {
        webDriverHolder.driver.navigate().to(environment.getProperty("web.google.url"));
        Logger.info(webDriverHolder.driver.getCurrentUrl());
    }

    public void navigateToPublishPage() {

        String url = environment.getProperty("workplace_web.author.to.pub.url");
        String childPage = url + environment.getProperty("test.page.name") + ".html";
        Logger.info("Child page is...!!" + childPage);
        webDriverHolder.driver.navigate().to(childPage);
        Logger.info(webDriverHolder.driver.getCurrentUrl());
    }

    /* FUNCTION NAME: navigateToPublisher
     * PARAMETERS USED: NA
     * WORKING: This function navigates the user from current page to the Publisher homepage
     * */
    public void navigateToPublisher() {
        //validatePage();
        webDriverHolder.driver.navigate().to(environment.getProperty("workplace_web.publisher.url"));
        Logger.info(webDriverHolder.driver.getCurrentUrl());

    }

    public void navigateToProbPublisher(String prodPubUrl) {
        //validatePage();
        webDriverHolder.driver.navigate().to(prodPubUrl);
        Logger.info(webDriverHolder.driver.getCurrentUrl());

    }

    public void navigateToCrxDe() {
        webDriverHolder.driver.navigate().to(environment.getProperty("workplace_web.crxde.url"));
        Logger.info(webDriverHolder.driver.getCurrentUrl());
    }

    public void scrollInViewByWebElement(WebElement element) {
        for (int i = 0; i <= 3; i++) {
            try {
                ((JavascriptExecutor) webDriverHolder.driver).executeScript("arguments[0].scrollIntoView(true);", element);
                Logger.info("Scrolled to the element" + element);
                break;
            } catch (Exception e) {
            }
        }
    }

    public WebElement clickToggleElement(String xpath_toggle, String xpath_assets) {
        WebElement element1 = webDriverHolder.driver.findElement(By.xpath(xpath_toggle));
        element1 = driverWait(25).ignoring(StaleElementReferenceException.class).until(ExpectedConditions.elementToBeClickable(element1));
        WebElement element2 = webDriverHolder.driver.findElement(By.xpath(xpath_assets));
        if (element2.isDisplayed()) {
            scrollInViewByWebElement(element2);
            element2 = driverWait(25).ignoring(StaleElementReferenceException.class).until(ExpectedConditions.elementToBeClickable(element2));
            element2.click();
        } else {
            element1.click();
            element2.click();
        }
        return element2;
    }


    public void takeScreenShot(Scenario scenario) {
        //After a web test case take a screenshot of the full screen and save it to build\reports\cucumber\screenshots
        String featureAndScneanrioNames=scenario.getId();
        String[] featureAndScenarioSplit=featureAndScneanrioNames.split(";");
        String featureName=featureAndScenarioSplit[0];
        String fileName = featureName + "//" + scenario.getStatus() + "_" + scenario.getName();
        TakesScreenshot scrShot = ((TakesScreenshot) webDriverHolder.driver);
        File rawScreenshot = scrShot.getScreenshotAs(OutputType.FILE);

        String projectPath = System.getProperty("user.dir");
        final String root = projectPath + "/build/reports/cucumber/screenshots/";

        final File targetDir = new File(root);

        if (!targetDir.exists()) targetDir.mkdir();

        String finalScreenshotLocation = root + fileName.toLowerCase().replace("//", "/").replace(",", "").replace("]", "_").replace(" ", "_").replace("[", "_") + "_"
                + System.currentTimeMillis() + ".png";
        Logger.info("Saving screenshot to " + finalScreenshotLocation);

        try {
            FileUtils.copyFile(rawScreenshot, new File(finalScreenshotLocation));
        } catch (IOException e) {
            e.printStackTrace();
        }

        final String[] files = finalScreenshotLocation.split("/build");

        byte[] bytes = ("../artifact/build" + files[1]).getBytes();
        scenario.embed(bytes, "image/url");
    }

    public void switchToAuthorWindow() {
        Set<String> handles = webDriverHolder.driver.getWindowHandles();
        Logger.info("No of Windows are..." + handles.size());
        for (String handle : handles) {
            Logger.info("Child Window Handle is...!!" + handle);
            webDriverHolder.driver.switchTo().window(handle);
        }

    }

    public String switchToWindow() {

        String parentHandle = webDriverHolder.driver.getWindowHandle();
        Logger.info("Parent Window Handle is..!!!" + parentHandle);


        Set<String> handles = webDriverHolder.driver.getWindowHandles();
        Logger.info("No of Windows are..." + handles.size());

        Iterator<String> itr = handles.iterator();
        while (itr.hasNext()) {
            String childWindow = itr.next();
            if (!parentHandle.equals(childWindow)) {
                Logger.info("Child Window Handle is...!!" + childWindow);
                webDriverHolder.driver.switchTo().window(childWindow);
                break;
            }
        }
        return parentHandle;
    }

    public void switchToParentWindow(String windowName) {
        webDriverHolder.driver.switchTo().window(windowName);
    }

    public void addCookies() {
        Cookie ck = new Cookie("login-token",
                "c61218c9-86a8-48de-b3af-f8a314c91292%3a8a120a85-d49d-49b0-b812-3efba942b260_83265c3906c582ed%3acrx.default");
        webDriverHolder.driver.manage().addCookie(ck);
    }

    public void switchToFrameByNameOrID(String var) {
        webDriverHolder.driver.switchTo().frame(var);
    }

    public void pageRefresh() {
        Logger.info("Page got refreshed");
        webDriverHolder.driver.navigate().refresh();
    }

    public void assetSearch(String xpath, String assetName) {

        Logger.info("Started finding an asset..!!!");
        Logger.info("Asset Search XPATH is..." + xpath);
        element(xpath).clear();
        Logger.info("Asset Serach field is cleared...");
        Logger.info("Entering asset name to find an asset..!!!");
        enterValue(xpath, assetName);
        Logger.info("Value is entered to be searched");
        element(xpath).sendKeys(Keys.ENTER);
        Logger.info("Enter key is pressed");
        Logger.info("Asset found..!!!");
    }

    public void dragAndDropByAsset(String assetXpath, String assetName, String srcXpath, String trgtXpath) {

        assetSearch(assetXpath, assetName);
        wpsWait(3000);
        driverWait(90).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(assetXpath)));
        WebElement srcElement = driverWait(30).ignoring(StaleElementReferenceException.class)
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath(srcXpath)));
        WebElement trgtElement = driverWait(30).ignoring(StaleElementReferenceException.class)
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath(trgtXpath)));
        Actions actions = new Actions(webDriverHolder.driver);
        try {
            actions.dragAndDrop(srcElement, trgtElement).build().perform();
        } catch (StaleElementReferenceException e) {
            e.getMessage();
        }
    }


    public void dragAndDrop(String srcXpath, String trgtXpath) {
        for (int i = 1; i <= 10; i++) {
            if (getList(srcXpath).size() > 0) {
                Logger.info("Src Image Path is found..!!!");
                WebElement srcElement = driverWait(10).ignoring(StaleElementReferenceException.class)
                        .until(ExpectedConditions.presenceOfElementLocated(By.xpath(srcXpath)));
                WebElement trgtElement = driverWait(10).ignoring(StaleElementReferenceException.class)
                        .until(ExpectedConditions.presenceOfElementLocated(By.xpath(trgtXpath)));
                Actions actions = new Actions(webDriverHolder.driver);
                actions.dragAndDrop(srcElement, trgtElement).build().perform();
                Logger.info("Component is dragged and dropped.");
                break;
            } else {
                int imgSize = getList("//coral-card-asset/img").size();
                Logger.info("Src Image Page is not found so we are scrolling down");
                scrollInViewByWebElement(element("(//coral-card-asset/img)[" + imgSize + "]"));
                Logger.info("Scrolling started");
            }
        }
    }


    public void clickByJs(String loc) {

        WebElement webElement = driverWait(15).ignoring(StaleElementReferenceException.class)
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath(loc)));
        driverWait(15).until(ExpectedConditions.elementToBeClickable(webElement));
        try {
            if (webElement.isEnabled() && webElement.isDisplayed()) {
                JavascriptExecutor executor = (JavascriptExecutor) webDriverHolder.driver;
                executor.executeScript("arguments[0].click();", webElement);
            }

        } catch (Exception e) {
            Logger.info("Exception occurs ");
        }
    }

    public void verifyBrokenImage(String xpath, String imageName) {
        try {
            String imageSrc = getAttribute(xpath, "src");
            Logger.info("ImgSrc is...." + imageSrc);
            boolean imgFlag = imageSrc.endsWith(imageName);
            Logger.info("Flag is..!!!" + imgFlag);
            Logger.info("Image Src is..!!!!" + imageSrc);
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(imageSrc);
            HttpResponse response = null;
            try {
                response = client.execute(request);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (response.getStatusLine().getStatusCode() == 200) {
                Logger.info("Status Code is...!!!" + response.getStatusLine().getStatusCode());
                boolean imgStatusFlag = true;
//                assertTrue(imgStatusFlag);
                Logger.info("Image is active and not broken");
            } else Logger.info("Either image is missing Or broken");
        } catch (NullPointerException e) {
            e.printStackTrace();
            Logger.info("Image src is missing and hence no image is present");
        }
    }

    public void validatePage() {
        Logger.info("We are into Validating Page");
        String url = environment.getProperty("workplace_web.webserver.url");
        Logger.info("Url is going to naviage..." + url);
        webDriverHolder.driver.navigate().to(environment.getProperty("workplace_web.webserver.url"));
        String currentUrl = webDriverHolder.driver.getCurrentUrl();
        Logger.info("Current URL is..." + currentUrl);

        if (currentUrl.equals("http://h0002637.associatesys.local/content/workplace/index.html")) {
            Logger.info("Status Code is...!!!" + currentUrl);
            Logger.info("The request has succeeded");
            webDriverHolder.driver.navigate().to(environment.getProperty("workplace_web.publisher.url"));
            Logger.info(webDriverHolder.driver.getCurrentUrl());
        } else if (currentUrl.equals("https://www.tdameritrade.com/web-page-not-found.html")) {
            Logger.info("Status Code is...!!!" + currentUrl);
            Logger.info("404 Error Page..Resource Not Found");
        } else if (currentUrl.equals("https://www.tdameritrade.com/error/server-error.html")) {
            Logger.info("Status Code is...!!!" + currentUrl);
            Logger.info("500 Internal Server Error");
        } else if (currentUrl.equals("https://www.tdameritrade.com/error/service-unavailable.html")) {
            Logger.info("Status Code is...!!!" + currentUrl);
            Logger.info("503 Service Unavailable");
        }
    }

    public void enterValueByJquery(String htmlTag, String text) {
        try {
            JavascriptExecutor executor = (JavascriptExecutor) webDriverHolder.driver;
            executor.executeScript("$('" + htmlTag + "').text('" + text + "');");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clickByJquery(String htmlTag) {
        try {
            JavascriptExecutor executor = (JavascriptExecutor) webDriverHolder.driver;
            executor.executeScript("$('" + htmlTag + "').click();");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean pubVerifyHeaderAltTextFlag = false;


    public void waitForPageLoad() {
        //wait for pageload
        JavascriptExecutor js = (JavascriptExecutor) webDriverHolder.driver;
        for (int i = 0; i < 25; i++) {
            wpsWait(1);
            //To check page ready state.
            if (js.executeScript("return document.readyState").toString().equals("complete")) {
                break;
            }
        }
    }

    public void scrollToElement(WebElement loc) {
        try {
            JavascriptExecutor executor = (JavascriptExecutor) webDriverHolder.driver;
            executor.executeScript("arguments[0].scrollIntoView();", loc);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public String getUniqueId(String cardXpath) {
        String XPATH_INSIDE_CARD_NEW = cardXpath + "[" + 1 + "]";
        Logger.info("Value of XPATH_INSIDE_CARD_NEW is :: " + XPATH_INSIDE_CARD_NEW);
        String dataPath = getAttribute(XPATH_INSIDE_CARD_NEW, "data-path");
        String[] dataPaths = dataPath.split("/");
        Logger.info("Data Path is....!!!!" + dataPath);
        String val = dataPaths[dataPaths.length - 1].replaceAll("\\D+", "");
        Logger.info("Length of the Val is...!!!!!" + val.length() + " Val is...!!!" + val);
        return val;
    }




    public void checkMegaNavigation(String xpath)
    {
        if(getList(xpath).size()>0) {
            String cardTitle = getText(xpath);
            Logger.info("Title of the card is..!!!" + cardTitle);
            clickWebElement(xpath);
        } else
            Logger.info("Card container is not appearing in the sticky banner");
    }


}




