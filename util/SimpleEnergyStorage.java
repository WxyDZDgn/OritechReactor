package util;

public class SimpleEnergyStorage {
	private long amount;
	public SimpleEnergyStorage() {
		amount = 0L;
	}
	public void insertIgnoringLimit(long amount, boolean ignored) {
		this.amount += amount;
	}
	public long getAmount() {
		return amount;
	}
}
