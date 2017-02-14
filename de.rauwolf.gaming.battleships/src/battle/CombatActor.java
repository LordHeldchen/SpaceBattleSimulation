package battle;

import logging.battleLogger.BattleLogger;

public abstract class CombatActor implements Comparable<CombatActor> {
    private final int      initiativeDecay;

    private int            currentInitiative;

    protected SingleBattle currentBattle;
    protected BattleLogger logger;

    protected CombatActor(int startInitiative, int initiativeDecay, BattleLogger logger) {
    	this.logger = logger;
        this.currentInitiative = startInitiative;
        this.initiativeDecay = initiativeDecay;
    }

    protected final void loseInitiative() {
        currentInitiative -= initiativeDecay;
    }

    public final void setCurrentBattle(SingleBattle singleBattle) {
        this.currentBattle = singleBattle;
    }

    public final void addBattleLog(BattleLogger battleLogger) {
        this.logger = battleLogger;
    }

    abstract CombatTarget takeAction();

    @Override
    public int compareTo(CombatActor other) {
        return this.currentInitiative - other.currentInitiative;
    }
}
