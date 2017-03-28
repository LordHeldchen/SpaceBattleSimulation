package battle;

public class ShieldInstance extends CombatActor {
	private final int maxShield;
	private final int regenerationAmount;

	private ShipInstance owningShipInstance;
	private int currentShield;

	public ShieldInstance(int maxStrength, int regenerationAmount, int startInitiative, int initiativeDecay) {
		super(startInitiative, initiativeDecay);

		this.maxShield = maxStrength;
		this.regenerationAmount = regenerationAmount;
	}

	@Override
	public CombatTarget takeAction() {
		loseInitiative();

		if (maxShield > 0) {
			int regeneration = currentShield + regenerationAmount > maxShield ? maxShield - currentShield
					: regenerationAmount;
			currentShield += regeneration;
			logger.regeneratesShield(this, regeneration, currentShield);
		}

		return null;
	}

	public ShipInstance getOwner() {
		return owningShipInstance;
	}

	public int getCurrentShield() {
		return currentShield;
	}

	public int takeShieldDamage(int damage, int strength) {
		final int shieldBefore = currentShield;
		currentShield = currentShield - damage;

		int remainingDamage = 0;
		if (currentShield <= 0) {
			remainingDamage = -currentShield;
			if (shieldBefore > 0) {
				logger.shieldBreaks(this);
			}
		} else {
			logger.takesShieldDamage(this, (shieldBefore - currentShield));
		}

		return remainingDamage;
	}
}
