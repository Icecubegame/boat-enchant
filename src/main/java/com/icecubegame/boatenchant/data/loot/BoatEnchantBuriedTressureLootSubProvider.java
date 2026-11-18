package com.icecubegame.boatenchant.data.loot;

import com.icecubegame.boatenchant.enchantment.BoatEnchantment;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetEnchantmentsFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;


import java.util.Optional;

public class BoatEnchantBuriedTressureLootSubProvider implements LootTableSubProvider {
    LootTableSubProvider.Context context;

    public BoatEnchantBuriedTressureLootSubProvider(LootTableSubProvider.Context context) {
        this.context = context;
    }

    @Override
    public void run() {
        LootPool.Builder pool = LootPool.lootPool();
        HolderGetter<Enchantment> enchantments = context.lookup(Registries.ENCHANTMENT);

        Optional<Holder.Reference<Enchantment>> landEntry = enchantments.get(BoatEnchantment.LAND);
        Optional<Holder.Reference<Enchantment>> stepEntry = enchantments.get(BoatEnchantment.STEP);

        landEntry.ifPresent(landEnchant -> pool
                .add(LootItem.lootTableItem(Items.BOOK)
                        .apply(new SetEnchantmentsFunction.Builder().withEnchantment(landEnchant, ContextIntProviders.exactly(1)))));

        stepEntry.ifPresent(stepEnchant -> pool
                .add(LootItem.lootTableItem(Items.BOOK)
                        .apply(new SetEnchantmentsFunction.Builder().withEnchantment(stepEnchant, ContextIntProviders.exactly(1)))));

        pool.setRolls(ContextIntProviders.exactly(1))
                .when(LootItemRandomChanceCondition.randomChance(0.5f));

        context.accept(
                BoatEnchantLootTables.BURIED_TRESSURE,
                LootTable.lootTable().withPool(pool)
        );
    }

    public static class Factory implements  LootTableSubProvider.Factory {

        @Override
        public LootTableSubProvider create(Context context) {
            return new BoatEnchantBuriedTressureLootSubProvider(context);
        }
    }
}
