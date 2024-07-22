package com.refinedmods.refinedstorage.fabric.storage.diskinterface;

import com.refinedmods.refinedstorage.common.storage.Disk;
import com.refinedmods.refinedstorage.common.storage.diskinterface.AbstractDiskInterfaceBlockEntity;
import com.refinedmods.refinedstorage.common.storage.diskinterface.AbstractDiskInterfaceBlockEntityRenderer;
import com.refinedmods.refinedstorage.fabric.support.render.RenderTypes;

public class DiskInterfaceBlockEntityRendererImpl<T extends AbstractDiskInterfaceBlockEntity>
    extends AbstractDiskInterfaceBlockEntityRenderer<T> {
    public DiskInterfaceBlockEntityRendererImpl() {
        super(RenderTypes.DISK_LED);
    }

    @Override
    protected Disk[] getDisks(final AbstractDiskInterfaceBlockEntity blockEntity) {
        if (!(blockEntity instanceof FabricDiskInterfaceBlockEntity fabricBlockEntity)) {
            return null;
        }
        if (fabricBlockEntity.getRenderData() instanceof Disk[] disks) {
            return disks;
        }
        return null;
    }
}
