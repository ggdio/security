package br.com.ggdio.client.zookeeper;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

/**
 * A simple client for Zookeeper
 * 
 * @author Guilherme Dio
 *
 */
public class ZookeeperClient {

	private ZooKeeper zk;

	public ZookeeperClient(String zkList) {
		final CountDownLatch connSignal = new CountDownLatch(1);

		try {
			zk = new ZooKeeper(zkList, 3000, new Watcher() {
				public void process(WatchedEvent event) {
					if (event.getState() == KeeperState.SyncConnected) {
						connSignal.countDown();
					}
				}
			});
	
			connSignal.await();
		} catch(Exception e) {
			close();
			
			throw new RuntimeException(e);
		}
	}

	public void close() {
		try {
			if(zk != null) zk.close();
		} catch(Exception e) {}
	}

	public void createNode(String path, byte[] data) {
		try {
			zk.create(path, data, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			
		} catch(Exception e) {
			throw new RuntimeException(e);
			
		}
	}

	public void updateNode(String path, byte[] data) {
		try {
			zk.setData(path, data, zk.exists(path, true).getVersion());
			
		} catch(Exception e) {
			throw new RuntimeException(e);
			
		}
	}

	public void deleteNode(String path) {
		try {
			zk.delete(path, zk.exists(path, true).getVersion());
			
		} catch(Exception e) {
			throw new RuntimeException(e);
			
		}
	}
	
	public List<String> listNodes(String path) {
		try {
			return zk.getChildren(path, true);
			
		} catch(Exception e) {
			throw new RuntimeException(e);
			
		}
	}
	
	public byte[] getNodeData(String path) {
		try {
			return zk.getData(path, true, zk.exists(path, true));
			
		} catch (Exception e) {
			throw new RuntimeException(e);
			
		}
	}

}