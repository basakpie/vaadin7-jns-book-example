package com.xmlrpc;

import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;
import org.apache.xmlrpc.webserver.WebServer;

public class XMLRPCServer {

    public static void main(String[] args) throws Exception {

        WebServer webServer = new WebServer(8080);
// XmlRpcServer
        // XmlRpcServer xmlRpcServer = new XmlRpcServer ();
// XmlRpcWebServer (Built-in HTTP Server)
        XmlRpcServer xmlRpcServer = webServer.getXmlRpcServer();
        PropertyHandlerMapping phm = new PropertyHandlerMapping();
        phm.setVoidMethodEnabled(true);

        phm.addHandler("helloworld", HelloWorldHandler.class);

        xmlRpcServer.setHandlerMapping(phm);
        XmlRpcServerConfigImpl serverConfig = (XmlRpcServerConfigImpl)
                xmlRpcServer.getConfig();
        serverConfig.setEnabledForExtensions(true);
        serverConfig.setContentLengthOptional(false);
        webServer.start();

    }
}


