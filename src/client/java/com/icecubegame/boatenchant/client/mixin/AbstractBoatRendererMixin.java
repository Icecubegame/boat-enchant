package com.icecubegame.boatenchant.client.mixin;

import com.icecubegame.boatenchant.BoatMixinAccessor;
import com.icecubegame.boatenchant.client.BoatRenderStateMixinAccessor;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.AbstractBoatRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.BoatRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractBoatRenderer.class)
public abstract class AbstractBoatRendererMixin extends EntityRenderer<AbstractBoat, BoatRenderState> {
    protected AbstractBoatRendererMixin(EntityRendererProvider.Context context)
    {
        super(context);
    }

    @Redirect(method = "submit",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/resources/Identifier;III)V"
            )
    )
    private void boatenchant$submit(
            SubmitNodeCollector instance,
            Model model,
            Object state,
            PoseStack poseStack,
            Identifier texture,
            int lightCoords,
            int overlayCoords,
            int outlineColor
    )
    {
        BoatRenderStateMixinAccessor stateAccess = (BoatRenderStateMixinAccessor)state;

        if (stateAccess.getIsEnchanted()) {
            instance.submitModel(model, state, poseStack, RenderTypes.entitySolidGlint(texture), lightCoords, overlayCoords, outlineColor);
        } else {
            instance.submitModel(model, state, poseStack, texture, lightCoords, overlayCoords, outlineColor);
        }
    }

    @Inject(method = "extractRenderState", at = @At("TAIL"))
    private void boatenchant$extractRenderState(AbstractBoat entity, BoatRenderState state, float partialTicks, CallbackInfo ci) {
        BoatRenderStateMixinAccessor stateAccess = (BoatRenderStateMixinAccessor)state;
        BoatMixinAccessor boatAccessor = (BoatMixinAccessor)entity;

        stateAccess.setIsEnchanted(boatAccessor.getHasLandEnchant() || boatAccessor.getHasStepEnchant());
    }
}
