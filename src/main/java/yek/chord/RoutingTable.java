package yek.chord;

public class RoutingTable {

	public NodeInfo successor;
	public NodeInfo predecessor;
	public Finger []fingerTable;

	public RoutingTable() {
		this.fingerTable = new Finger[160];
	}
}