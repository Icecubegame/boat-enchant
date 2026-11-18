package com.icecubegame.boatenchant.data.loot;

import com.icecubegame.boatenchant.BoatEnchant;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;

public class BoatEnchantLootTables {
    public static final ResourceKey<LootTable> BURIED_TRESSURE = key("buried_tressure");

    private static ResourceKey<LootTable> key(String path) {
        Identifier id = BoatEnchant.id(path);
        return ResourceKey.create(Registries.LOOT_TABLE, id);
    }
}
