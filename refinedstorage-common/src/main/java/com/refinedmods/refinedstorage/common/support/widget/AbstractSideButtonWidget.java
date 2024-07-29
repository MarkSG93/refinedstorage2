package com.refinedmods.refinedstorage.common.support.widget;

import com.refinedmods.refinedstorage.common.support.AbstractBaseScreen;
import com.refinedmods.refinedstorage.common.support.TextureIds;
import com.refinedmods.refinedstorage.common.support.tooltip.HelpClientTooltipComponent;
import com.refinedmods.refinedstorage.common.support.tooltip.SmallTextClientTooltipComponent;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.opengl.GL11;

public abstract class AbstractSideButtonWidget extends Button {
    private static final int WIDTH = 18;
    private static final int HEIGHT = 18;
    private static final int WARNING_SIZE = 10;

    @Nullable
    private ClientTooltipComponent warning;

    protected AbstractSideButtonWidget(final OnPress pressAction) {
        super(-1, -1, WIDTH, HEIGHT, Component.empty(), pressAction, DEFAULT_NARRATION);
    }

    protected abstract int getXTexture();

    protected abstract int getYTexture();

    protected ResourceLocation getTextureIdentifier() {
        return TextureIds.ICONS;
    }

    public void setWarning(@Nullable final Component text) {
        if (text == null) {
            this.warning = null;
            return;
        }
        this.warning = new SmallTextClientTooltipComponent(text.copy().withStyle(ChatFormatting.RED));
    }

    @Override
    public void renderWidget(final GuiGraphics graphics, final int mouseX, final int mouseY, final float partialTicks) {
        graphics.blit(TextureIds.ICONS, getX(), getY(), 238, isHovered ? 35 : 16, WIDTH, HEIGHT);
        graphics.blit(
            getTextureIdentifier(),
            getX() + 1,
            getY() + 1,
            getXTexture(),
            getYTexture(),
            WIDTH - 2,
            HEIGHT - 2
        );
        if (isHovered) {
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 0.5f);
            graphics.blit(TextureIds.ICONS, getX(), getY(), 238, 54, WIDTH, HEIGHT);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.disableBlend();
            final Screen screen = Minecraft.getInstance().screen;
            if (screen instanceof AbstractBaseScreen<?> baseScreen) {
                baseScreen.setDeferredTooltip(buildTooltip());
            }
        }
        if (warning != null) {
            renderWarning(graphics);
        }
    }

    private void renderWarning(final GuiGraphics graphics) {
        graphics.pose().pushPose();
        graphics.pose().translate(0, 0, 200);
        graphics.blit(
            TextureIds.ICONS,
            getX() + WIDTH - WARNING_SIZE + 2,
            getY() + HEIGHT - WARNING_SIZE + 2,
            246,
            148,
            WARNING_SIZE,
            WARNING_SIZE
        );
        graphics.pose().popPose();
    }

    protected List<ClientTooltipComponent> buildTooltip() {
        final List<ClientTooltipComponent> lines = new ArrayList<>();
        final ClientTooltipComponent title = ClientTooltipComponent.create(
            getTitle().getVisualOrderText()
        );
        lines.add(title);
        final ClientTooltipComponent subText = ClientTooltipComponent.create(
            getSubText().withStyle(ChatFormatting.GRAY).getVisualOrderText()
        );
        lines.add(subText);
        if (warning != null) {
            lines.add(warning);
        }
        final Component helpText = getHelpText();
        if (helpText != null) {
            lines.add(HelpClientTooltipComponent.create(helpText));
        }
        return lines;
    }

    protected abstract MutableComponent getTitle();

    protected abstract MutableComponent getSubText();

    @Nullable
    protected Component getHelpText() {
        return null;
    }
}
