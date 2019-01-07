package com.kamefrede.rpsideas.compat.jei;

import com.kamefrede.rpsideas.compat.jei.craftingTricks.TrickCraftingCategory;
import com.kamefrede.rpsideas.compat.jei.craftingTricks.TrickCraftingRecipeJEI;
import com.kamefrede.rpsideas.crafting.RPSRecipes;
import com.kamefrede.rpsideas.crafting.trick.TrickRecipe;
import com.kamefrede.rpsideas.items.RPSItems;
import mezz.jei.api.*;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import vazkii.psi.common.item.base.ModItems;

/**
 * @author WireSegal
 * Created at 1:30 PM on 12/21/18.
 */
@JEIPlugin
public class JEICompat implements IModPlugin {

    public static IJeiHelpers helpers;

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        helpers = registry.getJeiHelpers();
        registry.addRecipeCategories(TrickCraftingCategory.INSTANCE);
    }

    @Override
    public void register(IModRegistry registry) {
        helpers = registry.getJeiHelpers();

        registry.handleRecipes(TrickRecipe.class, TrickCraftingRecipeJEI::new, TrickCraftingCategory.INSTANCE.getUid());
        registry.addRecipes(RPSRecipes.trickRecipes, TrickCraftingCategory.INSTANCE.getUid());

        NonNullList<ItemStack> stacks = NonNullList.create();
        ModItems.cad.getSubItems(CreativeTabs.SEARCH, stacks);
        for (ItemStack stack : stacks)
            registry.addRecipeCatalyst(stack, TrickCraftingCategory.INSTANCE.getUid());
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {
        subtypeRegistry.useNbtForSubtypes(ModItems.cad, RPSItems.cadMagazine);
    }
}
