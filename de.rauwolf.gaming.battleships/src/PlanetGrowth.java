public class PlanetGrowth {
    static class PlanetDummy {
        public static int getCurrentPopulationInTenths() {
            return 0;
        }

        public static int getTargetPopulationInTenths() {
            return 0;
        }
    }

    int nextTurnPopGrowth() {
        int target_pop = PlanetDummy.getTargetPopulationInTenths() * 10;
        int current_pop = PlanetDummy.getCurrentPopulationInTenths() * 10;
        int pop_change = 0;

        if (target_pop > current_pop) {
            // Using target population slightly above actual population avoids
            // excessively slow asymptotic growth towards target.
            pop_change = current_pop * (target_pop + 10 - current_pop) / 100;
            pop_change = Math.min(pop_change, target_pop - current_pop);
        } else {
            pop_change = -(current_pop - target_pop) / 100;
            pop_change = Math.max(pop_change, target_pop - current_pop);
        }

        return pop_change;
    }
}
