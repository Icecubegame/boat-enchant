package com.icecubegame.boatenchant.enchantment;

import com.icecubegame.boatenchant.BoatEnchant;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentTarget;
import net.minecraft.world.item.enchantment.LevelBasedValue;

public class BoatEnchantment {
    public static final ResourceKey<Enchantment> LAND = key("land");
    public static final ResourceKey<Enchantment> STEP = key("step");

    private static ResourceKey<Enchantment> key(String path) {
        Identifier id = BoatEnchant.id(path);
        return ResourceKey.create(Registries.ENCHANTMENT, id);
    }

    public static Enchantment.Builder createLandEnchantment(BootstrapContext<Enchantment> context) {
        return Enchantment.enchantment(
                        Enchantment.definition(
                                context.lookup(Registries.ITEM).getOrThrow(ItemTags.BOATS),
                                1,
                                1,
                                Enchantment.constantCost(0),
                                Enchantment.constantCost(0),
                                0,
                                EquipmentSlotGroup.ANY
                        )
                );
    }

    public static Enchantment.Builder createStepEnchantment(BootstrapContext<Enchantment> context) {
        return Enchantment.enchantment(
                Enchantment.definition(
                        context.lookup(Registries.ITEM).getOrThrow(ItemTags.BOATS),
                        1,
                        1,
                        Enchantment.constantCost(0),
                        Enchantment.constantCost(0),
                        0,
                        EquipmentSlotGroup.ANY
                )
        );
    }
}
