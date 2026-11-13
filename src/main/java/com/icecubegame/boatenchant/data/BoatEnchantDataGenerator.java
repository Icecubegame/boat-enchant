package com.icecubegame.boatenchant.data;

import com.icecubegame.boatenchant.data.language.BoatEnchantEnglishLangProvider;
import com.icecubegame.boatenchant.data.tags.BoatEnchantItemTagProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;

public class BoatEnchantDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(BoatEnchantEnglishLangProvider::new);
        pack.addProvider(BoatEnchantItemTagProvider::new);
        pack.addProvider(BoatEnchantEnchantmentGenerator::new);
    }

    @Override
    public void buildRegistry(RegistrySetBuilder registryBuilder) {
        registryBuilder.add(Registries.ENCHANTMENT, BoatEnchantEnchantmentGenerator::bootstrap);
    }
}