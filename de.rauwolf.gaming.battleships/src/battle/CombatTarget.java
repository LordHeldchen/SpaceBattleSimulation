package battle;

import logging.battleLogger.BattleLogger;

public interface CombatTarget {
    void addBattleLog(BattleLogger logger);

    void takeDamage(Shot list);
    boolean isDestroyed();
}
