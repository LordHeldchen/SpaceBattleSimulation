package logging.battleLogger;

import java.util.HashSet;
import java.util.Map;

import battle.CombatActor;
import battle.ShieldInstance;
import battle.ShipInstance;
import ships.Fleet;

public interface BattleLogger {
    public void showFormup(HashSet<Fleet> allFleets,
                           HashSet<ShipInstance> allShips,
                           Map<Integer, Fleet> enemiesOfEmpireX,
                           HashSet<ShipInstance> participatingFighters);

    public void shipDestroyed(ShipInstance target);

    public void endOfBattle(HashSet<Fleet> allFleets);

    public void endOfSingleAttack(ShipInstance attacker);

    public void beginSingleAttack(final ShipInstance attacker, final ShipInstance target);

    public void nextRound();

    public void shipReacts(ShipInstance attacker);

    public void evades(ShipInstance ship, boolean doesEvade);

    public void armorDeflectsAllDamage(ShipInstance ship);

    public void takesArmorDamage(ShipInstance ship, int damage);

    public void shieldBreaks(ShipInstance ship);

    public void takesShieldDamage(ShipInstance ship, int i);

    public void regeneratesShield(ShieldInstance ship, int regeneration, int shieldNow);

    public void preysOnPreferredTargetType(CombatActor ship);

    public void hasResupplied(ShipInstance ship, ShipInstance mothership);
}
