package com.target;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

public class ElasticUtil {
	
	public static Client getClient()
	{
		Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", "localtestsearch").build();
		TransportClient transportClient = new TransportClient(settings);
		transportClient = transportClient.addTransportAddress(new InetSocketTransportAddress("localhost", 9300));//Can read from properties file
		return (Client) transportClient;
	}

    public static void close(Client client) { 
        client.close(); 
    } 

}
