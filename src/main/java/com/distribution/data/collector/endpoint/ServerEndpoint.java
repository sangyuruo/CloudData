package com.distribution.data.collector.endpoint;

import org.springframework.boot.actuate.endpoint.Endpoint;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

@Component
public class ServerEndpoint implements Endpoint<List<String>> {

	public String getId() {
		return "server";
	}

	public List<String> invoke() {
		List<String> serverDetails = new ArrayList<String>();
		try {
			serverDetails.add("Server IP Address : " + InetAddress.getLocalHost().getHostAddress());
			serverDetails.add("Server OS : " + System.getProperty("os.name").toLowerCase());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return serverDetails;
	}

	public boolean isEnabled() {
		return true;
	}

	public boolean isSensitive() {
		return false;
	}

}
