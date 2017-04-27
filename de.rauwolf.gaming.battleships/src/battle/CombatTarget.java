package battle;

import java.util.List;

import logging.battleLogger.BattleLogger;

public interface CombatTarget {
    void addBattleLog(BattleLogger logger);

    boolean isDestroyed();

    void receiveAttack(Shot list);
	boolean reactBeforeAttacker(ShipInstance attacker);

	void endCurrentBattle();

	List<CombatActor> getCombatActors();
}
