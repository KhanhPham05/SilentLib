package net.silentchaos512.lib.registry;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * A block/item intended to be registered through an SRegistry instance. SRegistry will then handle many steps of the
 * process, including registering models and adding recipes.
 * <p>
 * TODO: 1.13 - Re-evaluate <strong>everything</strong> in this interface!
 *
 * @author SilentChaos512
 *
 *
 */
@Deprecated
public interface IRegistryObject {

  // Formerly IAddRecipe

  /**
   * Add recipes here with the provided RecipeMaker, or just leave the method body empty.
   *
   * @param recipes
   *          A RecipeMaker object provided by your SRegistry.
   */
  default void addRecipes(@Nonnull RecipeMaker recipes) {}

  /**
   * Register ore dictionary entries for the block/item, or just leave the method body empty.
   */
  @Deprecated
  default void addOreDict() {}

  // Formerly IHasVariants

  /**
   * Return your mod ID here.
   *
   * @deprecated Should use getRegistryName?
   */
  @Deprecated
  @Nonnull
  String getModId();

  /**
   * Return the name of the block/item, excluding the resource prefix.
   *
   * @deprecated Should use getRegistryName?
   */
  @Deprecated
  @Nonnull
  String getName();

  /**
   * Returns the full registry name of the object (mod_id:object_name)
   *
   * @deprecated Should use getRegistryName?
   */
  @Deprecated
  @Nonnull
  default String getFullName() {

    return getModId() + ":" + getName();
  }

  /**
   * Gets a set of models (paired with metadata for each model). Replaces getVariants. The inclusion of metadata means
   * you no longer need to fill unused metadata values with nulls and the model map should NEVER contain null values.
   *
   * @param models
   *          An empty map to be filled with metadata/model pairs.
   */
  void getModels(@Nonnull Map<Integer, ModelResourceLocation> models);

  /**
   * Return true and handle model registration here if you do not want Silent Lib to handle model registration. If false
   * is returned, Silent Lib will register the models obtained via getModels.
   */
  default boolean registerModels() {

    return false;
  }
}
