package battle;

import ships.stats.MutableStat;

public abstract class CombatActor implements Comparable<CombatActor> {
    private final MutableStat timeCost;
    private int                   currentInitiative;
    private int                   lostTicks;

    protected CombatActor(int startInitiative, int timeCost) {
        this.currentInitiative = startInitiative;
        this.timeCost = new MutableStat(timeCost);
    }

    @Override
    public int compareTo(CombatActor other) {
        // Inverse ordering since PriorityQueues require natural ordering.
        // 'Natural' being in this case that having the most is best, i.e. makes you first.
        final int res = other.currentInitiative - this.currentInitiative;
        return res;
    }

    protected final int loseTicks() {
        int before = currentInitiative;
        currentInitiative -= timeCost.getCalculatedValue();
        return before;
    }

    public final int loseTicks(int val) {
        currentInitiative -= val;
        return currentInitiative;
    }

    protected abstract CombatTarget takeAction(SingleBattle currentBattle);

	public int getCurrentInitiative() {
		return currentInitiative;
	}

	public void setCurrentInitiative(int currentInitiative) {
		this.currentInitiative = currentInitiative;
	}

    public void rememberLostTicks(int lostTicks) {
        this.lostTicks += lostTicks;
    }
    
    public boolean hasRememberedLostTicks() {
        return lostTicks > 0;
    }

    public void applyRememberedLostTicks() {
        currentInitiative -= lostTicks;
        lostTicks = 0;
    }
}
