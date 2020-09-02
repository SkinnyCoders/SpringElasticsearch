package com.tanto;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.network.InetAddresses;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class config {

	@Value("${elasticsearch.host:localhost}")
	public String host;
	
	@Value("${elasticsearch.port:9300}")
	public Integer port;
	
	public String getHost() {
		return host;
	}
	
	public int getPort() {
		return port;
	}
	
	static final Settings settings = Settings.builder().put("cluster.name", "cluster-ex")
			.put("client.transport.sniff", true).put("client.transport.ignore_cluster_name", false)
			.put("client.transport.ping_timeout", "30s").put("client.transport.nodes_sampler_interval", "5s").build();
	
	@SuppressWarnings("deprecation")
	@Bean
	public Client client() {
		TransportClient client = null;
				try {
					System.out.println("Host " +getHost()+" Port" +getPort());
					client = new PreBuiltTransportClient(settings)
							.addTransportAddress(new TransportAddress(InetAddress.getByName(host), port));
				}catch (UnknownHostException e) {
					e.printStackTrace();
				}
		
		return client;
	}
	
}
