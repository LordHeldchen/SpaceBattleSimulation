package ships.configurators;

import java.util.List;
import java.util.Set;

import ships.blueprints.Blueprint;
import ships.blueprints.WeaponBlueprint;

public abstract class ShipTypeConfigurator {
    protected WeaponBlueprint weaponLayout;

    abstract Blueprint getConfiguredBlueprint();
    abstract Set<WeaponBlueprint> getAvailableWeaponLayouts();
    abstract List<WeaponBlueprint> getAvailableShieldModules();
    abstract List<WeaponBlueprint> getAvailableShieldModifications();
    abstract int getAllowedNumOfShieldModifications();

    //TODO: Activate assertions!
    void setWeaponLayout(WeaponBlueprint weaponLayout) {
        assert(getAvailableWeaponLayouts().contains(weaponLayout));
        this.weaponLayout = weaponLayout;
    }
    
    void setShieldLayout() {
        
    }
    
    void addShieldModification() {
        
    }
}
