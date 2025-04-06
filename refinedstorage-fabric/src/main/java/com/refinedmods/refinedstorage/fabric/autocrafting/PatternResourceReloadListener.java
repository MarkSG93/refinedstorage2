package com.refinedmods.refinedstorage.fabric.autocrafting;

import com.refinedmods.refinedstorage.common.autocrafting.PatternBlockEntityWithoutLevelRenderer;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;

import static com.refinedmods.refinedstorage.common.util.IdentifierUtil.createIdentifier;

public class PatternResourceReloadListener implements IdentifiableResourceReloadListener {
    private static final ResourceLocation ID = createIdentifier("pattern");

    @Override
    public ResourceLocation getFabricId() {
        return ID;
    }

    @Override
    public CompletableFuture<Void> reload(final PreparationBarrier preparationBarrier,
                                          final ResourceManager resourceManager,
                                          final ProfilerFiller preparationsProfiler,
                                          final ProfilerFiller reloadProfiler,
                                          final Executor backgroundExecutor,
                                          final Executor gameExecutor) {
        return PatternBlockEntityWithoutLevelRenderer.getInstance().reload(
            preparationBarrier,
            resourceManager,
            preparationsProfiler,
            reloadProfiler,
            backgroundExecutor,
            gameExecutor
        );
    }
}
