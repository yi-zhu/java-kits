package space.yizhu.configuration;/* Created by xiuxi on 2018/12/24.*/

import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import space.yizhu.variable.GlobalVariable;

@Component
public class ServerConfig implements ApplicationListener<WebServerInitializedEvent> {

    @Override
    public void onApplicationEvent(WebServerInitializedEvent webServerInitializedEvent) {
        GlobalVariable.serverPort = webServerInitializedEvent.getWebServer().getPort();
        GlobalVariable.webServer = webServerInitializedEvent.getWebServer();
    }
}
