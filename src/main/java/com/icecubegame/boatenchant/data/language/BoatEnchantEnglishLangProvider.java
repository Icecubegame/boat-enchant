package com.icecubegame.boatenchant.data.language;

import com.icecubegame.boatenchant.BoatEnchant;
import com.icecubegame.boatenchant.enchantment.BoatEnchantment;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class BoatEnchantEnglishLangProvider extends FabricLanguageProvider {
    public BoatEnchantEnglishLangProvider(FabricPackOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, "en_us", registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider holderLookup, TranslationBuilder translationBuilder) {
        translationBuilder.addEnchantment(BoatEnchantment.LAND, "Land");
        translationBuilder.addEnchantment(BoatEnchantment.STEP, "Step");
    }
}