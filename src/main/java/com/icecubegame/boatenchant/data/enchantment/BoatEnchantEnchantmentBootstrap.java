package com.icecubegame.boatenchant.data.enchantment;

import com.icecubegame.boatenchant.enchantment.BoatEnchantment;
import net.minecraft.core.registries.SingleRegistryBootstrap;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;

public class BoatEnchantEnchantmentBootstrap implements SingleRegistryBootstrap<Enchantment> {

    @Override
    public void run(BootstrapContext<Enchantment> context) {
        register(context, BoatEnchantment.LAND, BoatEnchantment.createLandEnchantment(context));
        register(context, BoatEnchantment.CURSE_LAND, BoatEnchantment.createCurseOfLandEnchantment(context));
        register(context, BoatEnchantment.STEP, BoatEnchantment.createStepEnchantment(context));
    }

    private static void register(BootstrapContext<Enchantment> context, ResourceKey<Enchantment> key, Enchantment.Builder builder) {
        context.register(key, builder.build(key.identifier()));
    }
}