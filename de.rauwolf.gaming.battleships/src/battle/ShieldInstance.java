package battle;

import ships.shipHulls.ValueDurationPair;

public class ShieldInstance extends CombatActor {
	private final int maxShield;
	private final double regenerationAmount;
	private final ShipInstance owningShipInstance;
	private final int breakDuration;

	private double currentShield;

	public ShieldInstance(ShipInstance owningShipInstance, int maxShield, int regenerationAmount, int shieldBreakDuration, int startInitiative) {
		super(startInitiative, 1);
		
		this.owningShipInstance = owningShipInstance;
		this.maxShield = maxShield;
		this.currentShield = maxShield;
		this.regenerationAmount = ((double) regenerationAmount / 10d);
		this.breakDuration = shieldBreakDuration;
	}

	@Override
	public CombatTarget takeAction() {
		loseInitiative();

		if (maxShield > 0 && currentShield < maxShield) {
			final double regeneration = currentShield + regenerationAmount > maxShield ? maxShield - currentShield
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
		return (int) currentShield;
	}

	public double takeShieldDamage(Shot shot) {
	    int damage = shot.amount;
	    int penetratingDamage = 0;

        ValueDurationPair valueDurationPair = shot.secondaryEffects.get(WeaponSecondaryEffect.SHIELD_PIERCE_PERCENT);
        if (valueDurationPair != null) {
            Integer shieldPiercePercent = valueDurationPair.getValue();
            penetratingDamage += (damage * shieldPiercePercent / 100);
            damage -= penetratingDamage;
        }

	    valueDurationPair = shot.secondaryEffects.get(WeaponSecondaryEffect.SHIELD_PIERCE_FLAT);
	    if (valueDurationPair != null) {
	        Integer shieldPierceFlat = valueDurationPair.getValue();
	        penetratingDamage += shieldPierceFlat;
	        damage -= penetratingDamage;
	    }
        penetratingDamage += (damage < 0 ? damage : 0);
        damage = damage < 0 ? 0 : damage;
	    
		final double shieldBefore = currentShield;
		currentShield = currentShield - damage;

		double remainingDamage = 0;
		if (currentShield <= 0) {
			remainingDamage = -currentShield;
			if (shieldBefore > 0) {
			    this.loseInitiative(breakDuration);
				logger.shieldBreaks(this, breakDuration);
			}
			currentShield = 0;
		} else {
			logger.takesShieldDamage(this, (shieldBefore - currentShield));
		}

		return remainingDamage + penetratingDamage;
	}
}
