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
			Thread.sleep(30000);
			this.node.stabilize();
			start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}