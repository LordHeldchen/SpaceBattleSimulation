package ships.basicShips;

public enum SizeEnum {
	XXS("XXS"), XS("XS"), S("S"), M("M"), L("L"), XL("XL"), XXL("XXL");
	
	private final String value;
	SizeEnum(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
}
