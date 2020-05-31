package com.ivietech.demo;

import com.ivietech.demo.service.websocketclient.SimpleWsHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

@SpringBootApplication
public class DemoApplication {
    @Autowired
    private SimpleWsHandler simpleWsHandler;

    private final String webSocketUri = "wss://stream.pushbullet.com/websocket/o.HRV88QcVMA7pEwfIYJFgtXfdLeAjRjux";

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
    
    @Bean
    public WebSocketConnectionManager wsConnectionManager() {
    	
    	//Generates a web socket connection
    	WebSocketConnectionManager manager = new WebSocketConnectionManager(
    			new StandardWebSocketClient(),
    			simpleWsHandler,
    			this.webSocketUri);
    	
    	//Will connect as soon as possible
    	//manager.setAutoStartup(true);
    	return manager;
    }
   
}
