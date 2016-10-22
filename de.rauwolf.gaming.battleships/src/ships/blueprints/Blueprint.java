package ships.blueprints;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ships.layouts.ArmorLayout;
import ships.layouts.ElectronicsLayout;
import ships.layouts.PropulsionLayout;
import ships.layouts.ShieldLayout;
import ships.layouts.WeaponLayout;

public class Blueprint {
    // For all following elements: May be reduced from Lists to single elements if Ships may not contain more than one.
    private final List<WeaponLayout>                   weapons;
    private final List<ArmorLayout>                    armor;
    private final List<ShieldLayout>                   shields;
    private final List<PropulsionLayout>               propulsion;
    private final List<ElectronicsLayout>              electronics;

    private final int                                  maxSupply;

    private final String                               name;
    private final String                               description;

    protected Map<Class<? extends Blueprint>, Integer> preferredTargets = new HashMap<Class<? extends Blueprint>, Integer>();

    public Blueprint(
                    String name,
                    String description,
                    List<WeaponLayout> weaponLayouts,
                    List<ArmorLayout> armorLayouts,
                    List<ShieldLayout> shieldLayouts,
                    List<PropulsionLayout> propulsionLayouts,
                    List<ElectronicsLayout> electronicsLayouts) {
        this.name = name;
        this.description = description;
        this.weapons = weaponLayouts != null ? weaponLayouts : new LinkedList<WeaponLayout>();
        this.armor = armorLayouts != null ? armorLayouts : new LinkedList<ArmorLayout>();
        this.shields = shieldLayouts != null ? shieldLayouts : new LinkedList<ShieldLayout>();
        this.propulsion = propulsionLayouts != null ? propulsionLayouts : new LinkedList<PropulsionLayout>();
        this.electronics = electronicsLayouts != null ? electronicsLayouts : new LinkedList<ElectronicsLayout>();

        //XXX Make dependent on specific blueprint
        maxSupply = 0;
    }
    public List<PropulsionLayout> getPropulsion() {
        return propulsion;
    }

    public List<ElectronicsLayout> getElectronics() {
        return electronics;
    }

    public List<ArmorLayout> getArmor() {
        return armor;
    }

    public List<ShieldLayout> getShields() {
        return shields;
    }

    public List<WeaponLayout> getWeapons() {
        return weapons;
    }

    public int getBaseBattleSpeed() {
        int sum = 0;
        for (PropulsionLayout prop : propulsion) {
            sum += prop.getBattleSpeed();
        }
        return sum;
    }

    public int getBattleSpeedDecay() {
        int sum = 0;
        for (PropulsionLayout prop : propulsion) {
            sum += prop.getBattleSpeedDecay();
        }
        return sum / propulsion.size();
    }

    public int getBaseEvasion() {
        int sum = 0;
        for (PropulsionLayout prop : propulsion) {
            sum += prop.getEvasion();
        }
        return sum;
    }

    public int getMaxShieldStrength() {
        int sum = 0;
        for (ShieldLayout shield : shields) {
            sum += shield.getStrength();
        }
        return sum == 0 ? 0 : sum / shields.size();
    }

    public int getShieldRegeneration() {
        int sum = 0;
        for (ShieldLayout shield : shields) {
            sum += shield.getRegeneration();
        }
        return sum;
    }

    public int getArmorHardeningByShields() {
        int sum = 0;
        for (ShieldLayout shield : shields) {
            sum += shield.getArmorHardening();
        }
        return sum == 0 ? 0 : sum / shields.size();
    }

    public int getArmorStrength() {
        int sum = 0;
        for (ArmorLayout arm : armor) {
            sum += arm.getStrength();
        }
        return sum / armor.size();
    }

    public int getBaseStealth() {
        int sum = 0;
        for (ElectronicsLayout el : electronics) {
            sum += el.getStealth();
        }
        return sum;
    }

    public int getMaxPayloadForWeapon(WeaponLayout weapon) {
        return weapon.getMaxPayload();
    }

    public int getMaxSupply() {
        return maxSupply;
    }

    public Map<Class<? extends Blueprint>, Integer> getPreferredTargets() {
        return preferredTargets;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Blueprint> getFighterTypesInBay() {
        //TODO: Define, allow configuration, ...
        return new LinkedList<Blueprint>();
    }
}
