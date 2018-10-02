package pages.base;


import components.WebDriverHolder;
import org.openqa.selenium.support.PageFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class PageObjectBeanPostProcessor implements BeanPostProcessor {

    @Autowired
    private WebDriverHolder webDriverHolder;


    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException
    {
        if (bean.getClass().isAnnotationPresent(PageObject.class)) {
            PageFactory.initElements(webDriverHolder.driver, bean);
        }
        return bean;
    }


    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException
    {
        return bean;
    }
}
