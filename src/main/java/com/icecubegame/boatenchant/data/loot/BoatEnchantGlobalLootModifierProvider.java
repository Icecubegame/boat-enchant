package com.icecubegame.boatenchant.data.loot;

import com.icecubegame.boatenchant.BoatEnchant;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.AddTableLootModifier;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;



public class BoatEnchantGlobalLootModifierProvider extends GlobalLootModifierProvider {

    public BoatEnchantGlobalLootModifierProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BoatEnchant.MODID);
    }

    @Override
    protected void start() {
        this.add(
                "boat_enchant_buried_treasure_modifier",
                new AddTableLootModifier(
                        Optional.of(
                                Holder.direct(LootTableIdCondition.builder(BuiltInLootTables.BURIED_TREASURE.identifier()).build())
                        ),
                        1000,
                        BoatEnchantLootTables.BURIED_TRESSURE
                )
        );
    }
}
