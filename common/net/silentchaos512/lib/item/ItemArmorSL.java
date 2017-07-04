package net.silentchaos512.lib.item;

import java.util.List;
import java.util.Map;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.silentchaos512.lib.SilentLib;
import net.silentchaos512.lib.registry.IRegistryObject;
import net.silentchaos512.lib.registry.RecipeMaker;
import net.silentchaos512.lib.util.LocalizationHelper;

public class ItemArmorSL extends ItemArmor implements IRegistryObject, IItemSL {

  protected String itemName;
  protected String modId;

  public ItemArmorSL(String modId, String itemName, ArmorMaterial materialIn, int renderIndexIn,
      EntityEquipmentSlot equipmentSlotIn) {

    super(materialIn, renderIndexIn, equipmentSlotIn);
    this.modId = modId;
    this.itemName = itemName;
    setUnlocalizedName(itemName);
  }

  // =======================
  // IRegistryObject methods
  // =======================

  @Override
  public void addRecipes(RecipeMaker recipes) {

  }

  @Override
  public void addOreDict() {

  }

  @Override
  public String getName() {

    return itemName;
  }

  @Override
  public String getFullName() {

    return modId + ":" + getName();
  }

  @Override
  public String getModId() {

    return modId;
  }

  @Override
  public void getModels(Map<Integer, ModelResourceLocation> models) {

    models.put(0, new ModelResourceLocation(getFullName().toLowerCase(), "inventory"));
  }

  @Override
  public boolean registerModels() {

    // Let SRegistry handle model registration by default. Override if necessary.
    return false;
  }

  // ==============================
  // Cross Compatibility (MC 10/11)
  // inspired by CompatLayer
  // ==============================

  @Override
  public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World worldIn,
      EntityPlayer playerIn, EnumHand hand) {

    return clOnItemRightClick(worldIn, playerIn, hand);
  }

  protected ActionResult<ItemStack> clOnItemRightClick(World worldIn, EntityPlayer playerIn,
      EnumHand hand) {

    return super.onItemRightClick(playerIn.getHeldItem(hand), worldIn, playerIn, hand);
  }

  @Override
  public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos,
      EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

    return clOnItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
  }

  protected EnumActionResult clOnItemUse(EntityPlayer player, World world, BlockPos pos,
      EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

    return super.onItemUse(player.getHeldItem(hand), player, world, pos, hand, facing, hitX, hitY,
        hitZ);
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {

    clGetSubItems(itemIn, tab, subItems);
  }

  @SideOnly(Side.CLIENT)
  protected void clGetSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {

    super.getSubItems(itemIn, tab, subItems);
  }


  // ===========================
  // Cross Compatibility (MC 12)
  // ===========================

  public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean advanced) {

    clAddInformation(stack, player.world, list, advanced);
  }

  public void clAddInformation(ItemStack stack, World world, List<String> list, boolean advanced) {

    LocalizationHelper loc = SilentLib.instance.getLocalizationHelperForMod(modId);
    if (loc != null) {
      String name = getFullName();
      list.addAll(loc.getItemDescriptionLines(name));
    }
  }
}
