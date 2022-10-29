package top.meyok.user.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author meyok@mbulletin.meyok.top
 * @date 2022/9/2 13:19
 * applicationContext工具类，包含通过beanName获得bean的方法
 */
@Component
public class ApplicationContextUtils implements ApplicationContextAware {
    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    public static Object getBean(String beanName){
        return context.getBean(beanName);
    }

}
