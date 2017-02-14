package battle;

import logging.battleLogger.BattleLogger;

public interface CombatTarget {
    void addBattleLog(BattleLogger logger);

    boolean isDestroyed();

    void takeDamage(Shot list);
	boolean reactBeforeAttacker(ShipInstance attacker);

	void endCurrentBattle();
}
