package com.icecubegame.boatenchant;

public interface BoatMixinAccessor {
    void setHasLandEnchant(boolean hasLand);
    boolean getHasLandEnchant();

    void setHasCurseOfLandEnchant(boolean hasCurseOfLand);
    boolean getHasCurseOfLandEnchant();

    void setHasStepEnchant(boolean hasStep);
    boolean getHasStepEnchant();
}
