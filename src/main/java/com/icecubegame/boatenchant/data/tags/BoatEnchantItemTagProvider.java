package com.icecubegame.boatenchant.data.tags;

import com.icecubegame.boatenchant.BoatEnchant;
import com.icecubegame.boatenchant.enchantment.BoatEnchantment;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public class BoatEnchantItemTagProvider extends FabricTagsProvider<Enchantment> {

    public BoatEnchantItemTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, Registries.ENCHANTMENT, registryLookupFuture);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void addTags(HolderLookup.@NonNull Provider wrapperLookup) {
        builder(EnchantmentTags.TREASURE)
                .add(
                        BoatEnchantment.LAND,
                        BoatEnchantment.STEP
                );
    }
}