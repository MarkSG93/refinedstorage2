package com.refinedmods.refinedstorage.platform.neoforge.support.render;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import javax.annotation.Nullable;

import com.mojang.math.Transformation;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.neoforged.neoforge.client.model.SimpleModelState;

public class RotationTranslationModelBaker {
    private final ModelState state;
    private final ModelBaker baker;
    private final Function<Material, TextureAtlasSprite> spriteGetter;
    private final ResourceLocation model;

    public RotationTranslationModelBaker(final ModelState state,
                                         final ModelBaker baker,
                                         final Function<Material, TextureAtlasSprite> spriteGetter,
                                         final ResourceLocation model) {
        this.state = state;
        this.baker = baker;
        this.spriteGetter = spriteGetter;
        this.model = model;
    }

    @SuppressWarnings("deprecation")
    public List<BakedQuad> bake(final Transformation transformation,
                                @Nullable final Direction side,
                                final RandomSource rand) {
        final ModelState wrappedState = new SimpleModelState(transformation, state.isUvLocked());
        final BakedModel bakedModel = baker.bake(model, wrappedState, spriteGetter);
        if (bakedModel == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(bakedModel.getQuads(null, side, rand));
    }
}
