    package main.java.ships.shipHulls;

public enum DamageType {
	HEAT("Heat"), EM("EM"), KINETIC("Kinetic"), EXPLOSIVE("Explosive"), ANTIMATTER("Antimatter"), PARTICLE("Particle");
	
	private final String value;
	
	DamageType(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
}
