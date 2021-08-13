package dev.lightdream.unchartedcore.files.config;

import dev.lightdream.unchartedcore.modules.sotw.SOTWConfig;
import lombok.NoArgsConstructor;

@SuppressWarnings("unused")
@NoArgsConstructor
public class Config {
    public boolean signShopModule = true;
    public boolean enchantingModule = true;
    public boolean customEnchantsModule = true;
    public boolean anvilModule = true;
    public boolean playerHeadsModule = true;
    public boolean statsModule = true;
    public boolean silkSpawnersModule = true;
    public boolean homesModule = true;
    public boolean warZoneClaimsModule = true;
    public boolean mountsModule = true;
    public boolean kitsModule = true;
    public boolean trailsModule = true;
    public boolean netherPortalModule = true;
    public boolean enderPearlModule = true;
    public boolean SOTWModule = true;
    public boolean combatLogModule = true;

    public void set(SOTWConfig config) {
        this.signShopModule = config.signShopModule;
        this.enchantingModule = config.enchantingModule;
        this.customEnchantsModule = config.customEnchantsModule;
        this.anvilModule = config.anvilModule;
        this.playerHeadsModule = config.playerHeadsModule;
        this.statsModule = config.statsModule;
        this.silkSpawnersModule = config.silkSpawnersModule;
        this.homesModule = config.homesModule;
        this.warZoneClaimsModule = config.warZoneClaimsModule;
        this.mountsModule = config.mountsModule;
        this.kitsModule = config.kitsModule;
        this.trailsModule = config.trailsModule;
        this.netherPortalModule = config.netherPortalModule;
        this.enderPearlModule = config.enderPearlModule;
        this.SOTWModule = config.SOTWModule;
        this.combatLogModule = config.combatLogModule;
    }
}
