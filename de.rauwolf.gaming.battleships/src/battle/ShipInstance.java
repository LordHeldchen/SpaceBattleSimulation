package battle;

import java.util.LinkedList;
import java.util.List;

import logging.battleLogger.BattleLogger;
import logging.battleLogger.HullDamageType;
import ships.Fleet;
import ships.blueprints.Blueprint;
import ships.blueprints.MutableBaseStat;
import ships.blueprints.WeaponBlueprint;

public class ShipInstance implements Comparable<ShipInstance>, CombatActor, CombatTarget {
    protected final Blueprint       blueprint;

    private ShieldInstance          shieldInstance;

    protected int                   currentHullStrength;

    // Holding the threshold as MutableBaseStats again allows e.g. for temporary armor lowering effects and the like. 
    private final MutableBaseStat   glanceThreshold;
    private final MutableBaseStat   hitThreshold;
    private final MutableBaseStat   critThreshold;

    private ShipInstance            mothership;

    protected final int             idOfOwningEmpire;
    protected BattleLogger          logger;

    private SingleBattle            battle;

    private LinkedList<CombatActor> combatActorsOfShip;

    public ShipInstance(int idOfOwningEmpire, Blueprint blueprint) {
        this.idOfOwningEmpire = idOfOwningEmpire;
        this.blueprint = blueprint;

        this.glanceThreshold = new MutableBaseStat(blueprint.getArmorGlanceThreshold());
        this.hitThreshold = new MutableBaseStat(blueprint.getArmorHitThreshold());
        this.critThreshold = new MutableBaseStat(blueprint.getArmorCritThreshold());
    }

    public final int getIdOfOwningEmpire() {
        return idOfOwningEmpire;
    }

    public void setCurrentBattle(SingleBattle battle) {
        this.battle = battle;
    }

    public List<CombatActor> getCombatActorsOfShip() {
        combatActorsOfShip = new LinkedList<CombatActor>();
        for (WeaponBlueprint weapon : blueprint.getWeapons()) {
            WeaponInstance weaponInstance = new WeaponInstance(weapon, this, logger);
            combatActorsOfShip.add(weaponInstance);
        }
        shieldInstance = new ShieldInstance(this, logger, blueprint.getMaxShieldStrength(),
                        blueprint.getShieldRegenerationAmount(), blueprint.getShieldRegenerationSpeed());
        return combatActorsOfShip;
    }

    public void setMothership(ShipInstance mothership) {
        this.mothership = mothership;
    }

    public ShipInstance getMothership() {
        return mothership;
    }

    public LinkedList<ShipInstance> getFightersInBay() {
        List<? extends Blueprint> fighterTypesInBay = blueprint.getFighterTypesInBay();
        LinkedList<ShipInstance> fighters = new LinkedList<ShipInstance>();
        for (Blueprint fighterType : fighterTypesInBay) {
            ShipInstance fighter = new ShipInstance(idOfOwningEmpire, fighterType);
            fighter.setMothership(this);
            fighters.add(fighter);
        }
        return fighters;
    }

    public final boolean isDestroyed() {
        return currentHullStrength <= 0;
    }

    public final void takeDamage(Shot shot) {
        final int evasion = blueprint.getEvasion() - BattleConstants.evasionEqualizer
                        + BattleConstants.randomizer.nextInt(BattleConstants.evasionRandomizerMaximum);

        //TODO Add a minimum/maximum hit chance (<0,1 and >0,9 perhaps?)
        if (shot.accuracy > evasion) {
            logger.evades(this, false);
            int remainingDamage = shieldInstance.takeShieldDamage(shot.amount, shot.armorPenetration);
            takeHullDamage(remainingDamage, shot.armorPenetration);
        } else {
            logger.evades(this, true);
        }
    }

    private final int takeHullDamage(int damage, int armorPenetration) {
        int hitStrength = (int) (BattleConstants.randomizer.nextFloat() * BattleConstants.penetrationRandomizerMaximum);

        if (hitStrength + armorPenetration > glanceThreshold.getCalculatedValue()) {
            if (hitStrength + armorPenetration > hitThreshold.getCalculatedValue()) {
                if (hitStrength + armorPenetration > critThreshold.getCalculatedValue()) {
                    damage *= BattleConstants.critMultiplier;
                    logger.takesHullDamage(this, damage, HullDamageType.CRIT);
                } else {
                    damage *= BattleConstants.hitMultiplier;
                    logger.takesHullDamage(this, damage, HullDamageType.HIT);
                }
            } else {
                damage *= BattleConstants.glanceMultiplier;
                logger.takesHullDamage(this, damage, HullDamageType.GLANCING);
            }
            currentHullStrength -= damage;
            return damage;

        } else {
            logger.armorDeflectsAllDamage(this);
        }
        return 0;
    }

    @Override
    public void takeAction() {
        final Fleet listOfPotentialTargets = enemiesOfEmpireX.get(attacker.getIdOfOwningEmpire());
        CombatTarget target = attacker.chooseTarget(listOfPotentialTargets);

        logger.beginSingleAttack(attacker, target);

        if (target.reactBeforeAttacker(attacker)) {
            logger.shipReacts(target);
            if (checkDestructionOf(attacker)) {
                logger.shipDestroyed(attacker);
                return;
            }
        }

        target.takeDamage(attacker.getShots());
        checkDestructionOf(target);
    }

    private boolean checkDestructionOf(ShipInstance target) {
        if (target.isDestroyed()) {
            logger.shipDestroyed(target);
            for (Fleet ships : enemiesOfEmpireX.values()) {
                ships.remove(target);
                if (ships.isEmpty()) {
                    //Some empire has no more enemies in this battle
                    continueCombat = false;
                }
            }
            allShips.remove(target);
            for (Fleet fleet : allFleets) {
                fleet.remove(target);
            }
            return true;
        }
        return false;
    }

    @Override
    public int compareTo(ShipInstance other) {
        return (other.currentBattleSpeed + other.currentStealthBonus) -
                        (this.currentBattleSpeed + this.currentStealthBonus);
    }

    public boolean reactBeforeAttacker(ShipInstance attacker) {
        //TODO: Common solution dependent on setups. E.g. for ECM or point defense. Other active defense mechanisms need to be added elsewhere. 
        int rand = BattleConstants.randomizer.nextInt(BattleConstants.cloakingRandomizerMaximum);
        return false;
    }

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

    @Override
    final public String toString() {
        return this.getClass().getSimpleName() + "(" + idOfOwningEmpire + ")" + "[" + currentBattleSpeed + "]";
    }

    public void endCurrentBattle() {
        this.battle = null;
    }
}
