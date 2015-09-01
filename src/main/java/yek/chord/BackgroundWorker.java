package yek.chord;

public class BackgroundWorker implements Runnable {
	Node node;

	public BackgroundWorker(Node n)
	{
		this.node = n;
	}

	public void run()
	{
		start();
	}

	public void start()
	{
		try {
			Thread.sleep(3000);
			System.out.println("successor "+this.node.routingTable.successor.ip);
			if (this.node.routingTable.predecessor != null) {
				System.out.println("predecessor "+this.node.routingTable.predecessor.ip);
			}
			this.node.stabilize();
			start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}