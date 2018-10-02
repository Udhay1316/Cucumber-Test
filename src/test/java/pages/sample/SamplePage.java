package pages.sample;

import org.springframework.beans.factory.annotation.Autowired;
import pages.base.BasePage;
import pages.base.PageObject;

@PageObject
public class SamplePage extends BasePage {

    @Autowired
    public SearchPage searchPage;

    public SamplePage()
    {

    }
}
