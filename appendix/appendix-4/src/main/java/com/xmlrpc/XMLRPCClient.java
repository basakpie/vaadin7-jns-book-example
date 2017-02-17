package com.xmlrpc;

import java.net.URL;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.util.ClientFactory;

public class XMLRPCClient {

    public static void main(String[] args) throws Exception {

        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setServerURL(new URL("http://127.0.0.1:8080/xmlrpc"));
        config.setEnabledForExtensions(true);
        config.setConnectionTimeout(60 * 1000);
        config.setReplyTimeout(60 * 1000);

        XmlRpcClient client = new XmlRpcClient();
        client.setConfig(config);
        ClientFactory factory = new ClientFactory(client);

        HelloWorld helloworld = (HelloWorld) factory.newInstance(Thread.currentThread().getContextClassLoader(), HelloWorld.class, "helloworld");
        System.out.print(helloworld.getMessage("HelloWorld!"));

    }

}

