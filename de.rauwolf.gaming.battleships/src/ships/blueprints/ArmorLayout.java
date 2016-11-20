package ships.blueprints;

public class ArmorLayout {
    /*
    "toHit" as before, determined before armor and damage rolls.
    Armor/Damage modeled as three thresholds, calculated vs. weapons'
    AP+rand(100).
    
    Example:
    
    * Armor-Thresholds 20/80/120 (values probably suitable for Cruiser or BS)
    * AP+rand(100) <= 20 --> Fully deflected by armor.
    * AP+rand(100) > 20 --> Glancing blow, not quite deflected, but fully
    absorbed by armor. Deals 25-40% (to be determined) of base damage.
    * AP+rand(100) > 80 --> Definite Hit, largely absorbed by armor. Deals
    100% of base damage.
    * AP+rand(100) > 120 --> Critical hit. Penetrates armor and Ravages
    critical ship sections. Possibly damages/disables some systems.
    Deals >=150% (to be determined) of base damage.
    
    --> Allows for combat computers that support targeting of weak spots
    (increased AP), allows differentiation of reinforced hull (increases
    crit threshold) Vs. reinforced armor (increases glancing and/or hit
    threshold), etc.
    Also might allow for "externally mounted" vs. "internally mounted":
    External modules may be disabled by simple hits, while internal modules
    can only be disabled by critical hits.
    */

    private final String name;

    private final int    glanceThreshold;
    private final int    hitThreshold;
    private final int    critThreshold;

    public ArmorLayout(String name, int glanceThreshold, int hitThreshold, int critThreshold) {
        this.name = name;
        this.glanceThreshold = glanceThreshold;
        this.hitThreshold = hitThreshold;
        this.critThreshold = critThreshold;
    }

    public String getName() {
        return name;
    }

    public int getGlanceThreshold() {
        return glanceThreshold;
    }

    public int getHitThreshold() {
        return hitThreshold;
    }

    public int getCritThreshold() {
        return critThreshold;
    }
}
