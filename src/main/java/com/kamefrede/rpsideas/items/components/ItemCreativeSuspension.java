package com.kamefrede.rpsideas.items.components;

import com.kamefrede.rpsideas.RPSIdeas;
import com.kamefrede.rpsideas.items.base.INoCraftingComponent;
import com.kamefrede.rpsideas.items.base.ItemComponent;
import com.kamefrede.rpsideas.util.helpers.ClientHelpers;
import com.kamefrede.rpsideas.util.libs.RPSItemNames;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.*;
import vazkii.psi.common.core.handler.PlayerDataHandler;

import javax.annotation.Nullable;
import java.util.List;

@Mod.EventBusSubscriber(modid = RPSIdeas.MODID)
public class ItemCreativeSuspension extends ItemComponent implements ICADColorizer, INoCraftingComponent {
    public ItemCreativeSuspension() {
        super(RPSItemNames.CREATIVE_COLORIZER);
    }

    @SubscribeEvent
    public static void modifyStats(CADStatEvent event) {
        ItemStack cad = event.getCad();
        ICAD cadItem = (ICAD) cad.getItem();
        ItemStack colorizer = cadItem.getComponentInSlot(cad, EnumCADComponent.DYE);
        if (!colorizer.isEmpty() && colorizer.getItem() instanceof ItemCreativeSuspension)
            event.setStatValue(getStatValue(event.getStat()));
        else {
            ItemStack assembly = cadItem.getComponentInSlot(cad, EnumCADComponent.ASSEMBLY);
            if (!assembly.isEmpty()) {
                ICADComponent componentItem = (ICADComponent) assembly.getItem();
                if (componentItem.getCADStatValue(assembly, EnumCADStat.POTENCY) == -1 &&
                        componentItem.getCADStatValue(assembly, EnumCADStat.EFFICIENCY) == -1)
                    event.setStatValue(getStatValue(event.getStat()));
            }
        }
    }

    @SubscribeEvent
    public static void updatePlayer(TickEvent.PlayerTickEvent event) {
        EntityPlayer player = event.player;
        ItemStack cad = PsiAPI.getPlayerCAD(player);
        if (!cad.isEmpty() && cad.getItem() instanceof ICAD) {
            ICAD cadItem = (ICAD) cad.getItem();
            int stat = cadItem.getStatValue(cad, EnumCADStat.OVERFLOW);
            if (stat == -1) {
                PlayerDataHandler.PlayerData data = PlayerDataHandler.get(player);
                data.availablePsi = data.getTotalPsi();
            }
        }
    }

    private static int getStatValue(EnumCADStat stat) {
        if (stat == EnumCADStat.SOCKETS)
            return 12;
        return -1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected void addTooltipTags(Minecraft minecraft, @Nullable World world, KeyBinding sneak, ItemStack stack, List<String> tooltip, ITooltipFlag advanced) {
        addTooltipTag(tooltip, true, getTranslationKey(stack) + ".desc");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColor(ItemStack itemStack) {
        return ClientHelpers.slideColor(0x9B3200, 0xFFAB00, 0.02f);
    }
}
