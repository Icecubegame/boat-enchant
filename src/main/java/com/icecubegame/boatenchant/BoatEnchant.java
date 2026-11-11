package com.icecubegame.boatenchant;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetEnchantmentsFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;


public class BoatEnchant implements ModInitializer {
	public static final String MOD_ID = "boat-enchant";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final ResourceKey<Enchantment> LAND_ENCHANT_ID = ResourceKey.create(Registries.ENCHANTMENT, BoatEnchant.id("land"));
	public static final ResourceKey<Enchantment> STEP_ENCHANT_ID = ResourceKey.create(Registries.ENCHANTMENT, BoatEnchant.id("step"));

	@Override
	public void onInitialize() {
		LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
			if (BuiltInLootTables.BURIED_TREASURE.equals(key)) {
				Optional<Holder.Reference<Enchantment>> landEntry = registries.get(LAND_ENCHANT_ID);
				Optional<Holder.Reference<Enchantment>> stepEntry = registries.get(STEP_ENCHANT_ID);

				LootPool.Builder pool = LootPool.lootPool();

				if (landEntry.isPresent()) {
					Holder.Reference<Enchantment> landEnchant = landEntry.get();
					pool
							.add(LootItem.lootTableItem(Items.BOOK)
									.apply(new SetEnchantmentsFunction.Builder().withEnchantment(landEnchant, ContextIntProviders.exactly(1))));
				}

				if (stepEntry.isPresent()) {
					Holder.Reference<Enchantment> stepEnchant = stepEntry.get();
					pool
							.add(LootItem.lootTableItem(Items.BOOK)
									.apply(new SetEnchantmentsFunction.Builder().withEnchantment(stepEnchant, ContextIntProviders.exactly(1))));
				}

				pool.setRolls(ContextIntProviders.exactly(1))
						.when(LootItemRandomChanceCondition.randomChance(0.5f));
				tableBuilder.withPool(pool);
			}
		});
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
