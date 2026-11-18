package com.icecubegame.boatenchant.data;

import com.icecubegame.boatenchant.BoatEnchant;
import com.icecubegame.boatenchant.data.enchantment.BoatEnchantEnchantmentBootstrap;
import com.icecubegame.boatenchant.data.language.BoatEnchantEnglishLangProvider;
import com.icecubegame.boatenchant.data.loot.BoatEnchantBuriedTressureLootSubProvider;
import com.icecubegame.boatenchant.data.loot.BoatEnchantGlobalLootModifierProvider;
import com.icecubegame.boatenchant.data.tags.BoatEnchantEnchantmentTagsProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.data.event.GatherDataRegistryEntriesEvent;

@EventBusSubscriber(modid = BoatEnchant.MODID)
public class BoatEnchantDataGenerator {

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent.Client event) {
        event.createProvider(BoatEnchantEnglishLangProvider::new);
        event.createProvider(BoatEnchantEnchantmentTagsProvider::new);
        event.createProvider(BoatEnchantGlobalLootModifierProvider::new);
    }

    @SubscribeEvent
    public static void onGatherRegistries(GatherDataRegistryEntriesEvent event) {
        event.add(Registries.ENCHANTMENT, new BoatEnchantEnchantmentBootstrap());
        LootTableProvider.SubProviderEntry subProvider = new LootTableProvider.SubProviderEntry(new BoatEnchantBuriedTressureLootSubProvider.Factory(), LootContextParamSets.CHEST);
        event.lootTable(subProvider);
    }
}