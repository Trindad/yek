class RoutingTable {
	String successorID;
	String successorIP;

	public RoutingTable(String id, String ip)
	{
		this.successorIP = ip;
		this.successorID = id;
	}
}