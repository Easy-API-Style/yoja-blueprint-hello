package blueprint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.easygoingapi.yoja.core.http.HttpMethod;
import com.easygoingapi.yoja.http.server.HttpRouter;
import com.easygoingapi.yoja.http.server.HttpServer;
import com.easygoingapi.yoja.http.server.WebApp;

public class Hello {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(Hello.class);
    
    public static void main(String[] args) {
        HttpRouter router = 
            HttpRouter.builder()
                      .contentType("js", "application/javascript")
                      .contentType("html", "text/html")
                      .webResource(WebApp.of(WebApp.Type.jar, 
                                             "com.easygoingapi.yoja.web", 
                                             "/yoja"), 
                                   "/*")
                      .webResource(WebApp.jar("blueprint"), "/*")
                      .webService(HttpMethod.GET, "/hello", r -> r.response().send("hello, yoja"))
                      .build();
        
        HttpServer.builder(router, 9090)
                  .start() 
                  .onSuccess(server -> {
                      LOGGER.info("blueprint Hello — http://localhost:{}/index.html", server.port());
                  })
                  .onFailure(Throwable::printStackTrace);
    }
    
}