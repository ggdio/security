package br.com.ggdio.client.zookeeper;

import java.util.List;

import br.com.ggdio.json.JSONUtils;

/**
 * Utilities for zookeeper
 * 
 * @author Guilherme Dio
 *
 */
public class ZookeeperUtils {

	/**
	 * Discover kafka broker list from zk
	 * @param zkList
	 * @return {@link String}
	 */
	public static String discoverKafkaBrokers(String zkList) {
		ZookeeperClient zk = null;
		try {
			zk = new ZookeeperClient(zkList);
			
			String brokerList = null;
			String path = "/brokers/ids";
			
			List<String> ids = zk.listNodes(path);
			for (String id : ids) {
				byte[] data = zk.getNodeData(path + "/" + id);
				
				Object json = JSONUtils.fromJSON(new String(data));
				String host = String.valueOf(JSONUtils.resolveValue("host", json));
				String port = String.valueOf(JSONUtils.resolveValue("port", json));
				
				if(brokerList == null) {
					brokerList = host.concat(":").concat(port);
				} else {
					brokerList = brokerList.concat(",").concat(host).concat(":").concat(port);
				}
			}
			
			return brokerList;
			
		} catch(Exception e) {
			throw new IllegalStateException("Failed to connect to zookeeper at '" + zkList + "'", e);
			
		}
		finally {
			if(zk != null) {
				try {
					zk.close();
				} catch(Exception e) {};
			}
		}
	}
	
}
