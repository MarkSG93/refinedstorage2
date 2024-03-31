package com.refinedmods.refinedstorage2.platform.common.support.packet;

import net.minecraft.resources.ResourceLocation;

import static com.refinedmods.refinedstorage2.platform.common.util.IdentifierUtil.createIdentifier;

public final class PacketIds {
    public static final ResourceLocation STORAGE_INFO_RESPONSE = createIdentifier("storage_info_response");
    public static final ResourceLocation STORAGE_INFO_REQUEST = createIdentifier("storage_info_request");
    public static final ResourceLocation ENERGY_INFO = createIdentifier("controller_energy");
    public static final ResourceLocation WIRELESS_TRANSMITTER_RANGE = createIdentifier("wireless_transmitter_range");
    public static final ResourceLocation GRID_ACTIVE = createIdentifier("grid_active");
    public static final ResourceLocation GRID_UPDATE = createIdentifier("grid_update");
    public static final ResourceLocation GRID_CLEAR = createIdentifier("grid_clear");
    public static final ResourceLocation PROPERTY_CHANGE = createIdentifier("property_change");
    public static final ResourceLocation GRID_INSERT = createIdentifier("grid_insert");
    public static final ResourceLocation GRID_EXTRACT = createIdentifier("grid_extract");
    public static final ResourceLocation GRID_SCROLL = createIdentifier("grid_scroll");
    public static final ResourceLocation CRAFTING_GRID_CLEAR = createIdentifier("crafting_grid_clear");
    public static final ResourceLocation CRAFTING_GRID_RECIPE_TRANSFER =
        createIdentifier("crafting_grid_recipe_transfer");
    public static final ResourceLocation RESOURCE_SLOT_UPDATE = createIdentifier("resource_slot_update");
    public static final ResourceLocation RESOURCE_SLOT_CHANGE = createIdentifier("resource_slot_change");
    public static final ResourceLocation RESOURCE_FILTER_SLOT_CHANGE = createIdentifier("resource_filter_slot_change");
    public static final ResourceLocation RESOURCE_SLOT_AMOUNT_CHANGE = createIdentifier("resource_slot_amount_change");
    public static final ResourceLocation SINGLE_AMOUNT_CHANGE = createIdentifier("detector_amount_change");
    public static final ResourceLocation USE_NETWORK_BOUND_ITEM = createIdentifier("use_network_bound_item");
    public static final ResourceLocation NETWORK_TRANSMITTER_STATUS = createIdentifier("network_transmitter_status");
    public static final ResourceLocation SECURITY_CARD_PERMISSION = createIdentifier("security_card_permission");
    public static final ResourceLocation SECURITY_CARD_RESET_PERMISSION = createIdentifier(
        "security_card_reset_permission"
    );
    public static final ResourceLocation SECURITY_CARD_BOUND_PLAYER = createIdentifier("security_card_bound_player");

    private PacketIds() {
    }
}
