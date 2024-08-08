package com.refinedmods.refinedstorage.common.util;

import com.refinedmods.refinedstorage.common.api.support.AmountFormatting;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

public final class IdentifierUtil {
    public static final String MOD_ID = "refinedstorage";

    public static final MutableComponent YES = Component.translatable("gui.yes");
    public static final MutableComponent NO = Component.translatable("gui.no");

    private IdentifierUtil() {
    }

    public static ResourceLocation createIdentifier(final String value) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, value);
    }

    public static String createTranslationKey(final String category, final String value) {
        return String.format("%s.%s.%s", category, MOD_ID, value);
    }

    public static MutableComponent createTranslation(final String category, final String value) {
        return Component.translatable(createTranslationKey(category, value));
    }

    public static MutableComponent createTranslation(final String category, final String value, final Object... args) {
        return Component.translatable(createTranslationKey(category, value), args);
    }

    public static MutableComponent createStoredWithCapacityTranslation(
        final long stored,
        final long capacity,
        final double pct
    ) {
        return createTranslation(
            "misc",
            "stored_with_capacity",
            Component.literal(stored == Long.MAX_VALUE ? "∞" : AmountFormatting.format(stored))
                .withStyle(ChatFormatting.WHITE),
            Component.literal(capacity == Long.MAX_VALUE ? "∞" : AmountFormatting.format(capacity))
                .withStyle(ChatFormatting.WHITE),
            Component.literal(String.valueOf((int) (pct * 100D)))
        ).withStyle(ChatFormatting.GRAY);
    }

    public static MutableComponent createTranslationAsHeading(final String category, final String value) {
        return Component.literal("<")
            .append(createTranslation(category, value))
            .append(">")
            .withStyle(ChatFormatting.DARK_GRAY);
    }

    // https://github.com/emilyploszaj/emi/blob/ee35c78b0f5b0b1e91cc0ba1571df8dcd88cbef3/xplat/src/main/java/dev/emi/emi/registry/EmiTags.java#L174
    public static String getTagTranslationKey(final TagKey<?> key) {
        final ResourceLocation registry = key.registry().location();
        final String fixedPath = registry.getPath().replace('/', '.');
        if (registry.getNamespace().equals(ResourceLocation.DEFAULT_NAMESPACE)) {
            return getTagTranslationKey("tag.%s.".formatted(fixedPath), key.location());
        }
        return getTagTranslationKey(
            "tag.%s.%s.".formatted(registry.getNamespace(), fixedPath),
            key.location()
        );
    }

    private static String getTagTranslationKey(final String prefix, final ResourceLocation id) {
        final String fixedPath = id.getPath().replace('/', '.');
        return prefix + id.getNamespace() + "." + fixedPath;
    }
}
