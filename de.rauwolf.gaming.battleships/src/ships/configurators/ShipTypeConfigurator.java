package ships.configurators;

import java.util.List;
import java.util.Set;

import ships.blueprints.Blueprint;
import ships.layouts.WeaponLayout;

public abstract class ShipTypeConfigurator {
    protected WeaponLayout weaponLayout;

    abstract Blueprint getConfiguredBlueprint();
    abstract Set<WeaponLayout> getAvailableWeaponLayouts();
    abstract List<WeaponLayout> getAvailableShieldModules();
    abstract List<WeaponLayout> getAvailableShieldModifications();
    abstract int getAllowedNumOfShieldModifications();

    //TODO: Activate assertions!
    void setWeaponLayout(WeaponLayout weaponLayout) {
        assert(getAvailableWeaponLayouts().contains(weaponLayout));
        this.weaponLayout = weaponLayout;
    }
    
    void setShieldLayout() {
        
    }
    
    void addShieldModification() {
        
    }
}
