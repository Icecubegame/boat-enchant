package com.icecubegame.boatenchant.data.tags;

import com.icecubegame.boatenchant.enchantment.BoatEnchantment;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EnchantmentTagsProvider;
import net.minecraft.tags.EnchantmentTags;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public class BoatEnchantEnchantmentTagsProvider extends EnchantmentTagsProvider {
    public BoatEnchantEnchantmentTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }


    @SuppressWarnings("unchecked")
    @Override
    protected void addTags(HolderLookup.@NonNull Provider wrapperLookup) {
        this.tag(EnchantmentTags.TREASURE)
                .add(
                        BoatEnchantment.LAND,
                        BoatEnchantment.STEP,
                        BoatEnchantment.CURSE_LAND
                );

        this.tag(EnchantmentTags.CURSE)
                .add(
                        BoatEnchantment.CURSE_LAND
                );
    }
}