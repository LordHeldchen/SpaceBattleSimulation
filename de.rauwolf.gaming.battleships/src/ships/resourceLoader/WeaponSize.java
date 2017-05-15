package ships.resourceLoader;

public enum WeaponSize {
	XXS("XXS"), XS("XS"), S("S"), M("M"), L("L"), XL("XL"), XXL("XXL"), D("D");
	
	private final String value;
	
	WeaponSize(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
}
