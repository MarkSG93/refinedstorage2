package com.refinedmods.refinedstorage.neoforge.storage.diskdrive;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;

public class DiskDriveGeometryLoader implements IGeometryLoader<DiskDriveUnbakedGeometry> {
    @Override
    public DiskDriveUnbakedGeometry read(final JsonObject jsonObject,
                                         final JsonDeserializationContext deserializationContext) {
        return new DiskDriveUnbakedGeometry();
    }
}
