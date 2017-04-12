package ships.shipHulls;

public enum WeaponSize {
	XXS("XXS"), XS("XS"), S("S"), M("M"), L("L"), XL("XL"), XXL("XXL");
	
	private final String value;
	
	WeaponSize(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
}
