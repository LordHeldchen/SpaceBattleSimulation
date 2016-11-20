package ships;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import logging.battleLogger.BattleLogger;
import ships.blueprints.Blueprint;
import ships.layouts.WeaponLayout;
import battle.BattleConstants;
import battle.Shot;

public class Ship implements Comparable<Ship> {
    protected final Blueprint          blueprint;

    protected int                      currentArmor;
    protected int                      currentShield;

    // To supply other Ships with ammo, repairs, ...
    protected int                      currentSupply;

    protected int                      currentBattleSpeed;
    protected int                      currentStealthBonus;

    // Current payload per weapon. Maximum payload depends on weapon layout.
    private Map<WeaponLayout, Integer> currentPayload;

    private Ship                       mothership;

    protected final int                idOfOwningEmpire;
    protected BattleLogger             logger;

    public Ship(int idOfOwningEmpire, Blueprint blueprint) {
        this.idOfOwningEmpire = idOfOwningEmpire;
        this.blueprint = blueprint;
    }

    public final int getIdOfOwningEmpire() {
        return idOfOwningEmpire;
    }

    public void prepareForBattle() {
        currentBattleSpeed = blueprint.getBaseBattleSpeed()
                        + BattleConstants.randomizer.nextInt(BattleConstants.battleSpeedRandomizerMaximum);
        currentStealthBonus = blueprint.getBaseStealth();
        currentPayload = new HashMap<WeaponLayout, Integer>();
        for (WeaponLayout weapon : blueprint.getWeapons()) {
            if (currentPayload.containsKey(weapon)) {
                currentPayload.put(weapon, currentPayload.get(weapon) + weapon.getMaxPayload());
            } else {
                currentPayload.put(weapon, weapon.getMaxPayload());
            }
        }
    }

    public void setMothership(Ship mothership) {
        this.mothership = mothership;
    }

    public LinkedList<Ship> getFightersInBay() {
        List<? extends Blueprint> fighterTypesInBay = blueprint.getFighterTypesInBay();
        LinkedList<Ship> fighters = new LinkedList<Ship>();
        for (Blueprint fighterType : fighterTypesInBay) {
            Ship fighter = new Ship(idOfOwningEmpire, fighterType);
            fighter.setMothership(this);
            fighters.add(fighter);
        }
        return fighters;
    }

    public boolean hasPayloadRemaining() {
        for (Integer payloadOfWeapon : this.currentPayload.values()) {
            if (payloadOfWeapon == 0) {
                return false;
            }
        }
        return true;
    }

    public final boolean isDestroyed() {
        return currentArmor <= 0;
    }

    public final void takeTurn() {
        currentBattleSpeed -= blueprint.getBattleSpeedDecay();
        currentStealthBonus = 0;

        if (!hasPayloadRemaining() && mothership != null && mothership.hasSupplyLeft()) {
            for (WeaponLayout weapon : currentPayload.keySet()) {
                currentPayload.put(weapon,
                                   mothership.getSupply(weapon.getMaxPayload() - currentPayload.get(weapon).intValue()));
                logger.hasResupplied(this, mothership);
            }
        }

        if (blueprint.getMaxShieldStrength() > 0) {
            int regeneration = currentShield + blueprint.getShieldRegeneration() > blueprint.getMaxShieldStrength() ?
                            blueprint.getMaxShieldStrength() - currentShield : blueprint.getShieldRegeneration();
            currentShield += regeneration;
            logger.regeneratesShield(this, regeneration, currentShield);
        }
    }

    private boolean hasSupplyLeft() {
        return currentSupply > 0;
    }

    public Ship chooseTarget(final Fleet listOfPotentialTargets) {
        Map<Class<? extends Blueprint>, Integer> preferredTargets = blueprint.getPreferredTargets();
        for (Class<? extends Blueprint> preferredType : preferredTargets.keySet()) {
            if (listOfPotentialTargets.containsType(preferredType)
                            && BattleConstants.randomizer.nextInt() < preferredTargets.get(preferredType)) {
                final ArrayList<Ship> targets = listOfPotentialTargets.getAllOfType(preferredType);
                logger.preysOnPreferredTargetType(this);
                return targets.get((int) (targets.size() * Math.random()));
            }
        }
        return listOfPotentialTargets
                        .get((int) (listOfPotentialTargets.size() * BattleConstants.randomizer.nextFloat()));
    }

    public final int takeDamage(List<Shot> list) {
        final int evasion = blueprint.getBaseEvasion() - BattleConstants.evasionEqualizer
                        + BattleConstants.randomizer.nextInt(BattleConstants.evasionRandomizerMaximum);
        for (Shot shot : list) {
            if (shot.accuracy > evasion) {
                logger.evades(this, false);
                int shieldDamage = takeShieldDamage(shot.amount, shot.strength);
                if (currentShield < 0) {
                    currentShield = 0;
                    int armorDamage = takeArmorDamage(shieldDamage, shot.strength);
                    return shieldDamage + armorDamage;
                }
            } else {
                logger.evades(this, true);
            }
        }
        return 0;
    }

    //TODO Add a minimum/maximum hit chance (<0,1 and >0,9 perhaps?)
    private final int takeShieldDamage(int amount, int strength) {
        int damage = (int) (BattleConstants.armorMatrix[blueprint.getMaxShieldStrength()][strength] * amount);

        final int shieldBefore = currentShield;
        currentShield -= damage;
        damage -= shieldBefore;

        if (shieldBefore > 0) {
            if (currentShield <= 0) {
                logger.shieldBreaks(this);
            } else {
                logger.takesShieldDamage(this, (shieldBefore - currentShield));
            }
        }

        return damage;
    }

    private final int takeArmorDamage(int amount, int strength) {
        final float damageFactor = BattleConstants.armorMatrix[blueprint.getArmorStrength()
                        + blueprint.getArmorHardeningByShields()][strength];
        int damage = 0;
        if (BattleConstants.randomizer.nextFloat() <= damageFactor) {
            damage = (int) (damageFactor * amount);
            logger.takesArmorDamage(this, damage);
        } else {
            logger.armorDeflectsAllDamage(this);
        }
        currentArmor -= damage;
        return damage;
    }

    public List<Shot> getShots() {
        List<Shot> shots = new LinkedList<Shot>();
        for (WeaponLayout weapon : currentPayload.keySet()) {
            if (currentPayload.get(weapon).intValue() > 0) {
                currentPayload.put(weapon, currentPayload.get(weapon) - 1);
                shots.add(new Shot(weapon.getDamage(), weapon.getStrength(), weapon.getAccuracy()));
            }
        }
        return shots;
    }

    @Override
    public int compareTo(Ship other) {
        return (other.currentBattleSpeed + other.currentStealthBonus) -
                        (this.currentBattleSpeed + this.currentStealthBonus);
    }

    public boolean reactBeforeAttacker(Ship attacker) {
        //TODO: Common solution dependent on setups. E.g. for ECM or point defense. Other active defense mechanisms need to be added elsewhere. 
        int rand = BattleConstants.randomizer.nextInt(BattleConstants.cloakingRandomizerMaximum);
        return false;
    }

    public void endBattle() {}

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public void addBattleLog(BattleLogger logger) {
        this.logger = logger;
    }

    public void removeBattleLogger() {
        this.logger = null;
    }

    public String getName() {
        return blueprint.getName();
    }

    public String getDescription() {
        return blueprint.getDescription();
    }

    public Blueprint getBlueprint() {
        return blueprint;
    }

    public final int getSupply(int maxAmountNeeded) {
        int possible = currentSupply > maxAmountNeeded ? maxAmountNeeded : currentSupply;
        currentSupply -= possible;
        return possible;
    }

    @Override
    final public String toString() {
        return this.getClass().getSimpleName() + "(" + idOfOwningEmpire + ")" + "[" + currentBattleSpeed + "]";
    }
}
