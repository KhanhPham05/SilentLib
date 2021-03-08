package net.silentchaos512.lib.crafting.ingredient;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import net.minecraftforge.registries.ForgeRegistries;
import net.silentchaos512.lib.SilentLib;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Stream;

/**
 * An ingredient which matches another ingredient, but excludes specific items
 */
public final class ExclusionIngredient extends Ingredient {
    private final Ingredient parent;
    private final Collection<Ingredient> exclusions = new ArrayList<>();

    private ExclusionIngredient(Ingredient parent, Collection<Ingredient> exclusions) {
        super(Stream.of());
        this.parent = parent;
        this.exclusions.addAll(exclusions);
    }

    public static ExclusionIngredient of(ITag<Item> tag, Ingredient... exclusions) {
        return of(Ingredient.fromTag(tag), exclusions);
    }

    public static ExclusionIngredient of(Ingredient parent, Ingredient... exclusions) {
        List<Ingredient> list = new ArrayList<>();
        Collections.addAll(list, exclusions);
        return new ExclusionIngredient(parent, list);
    }

    public static ExclusionIngredient of(ITag<Item> tag, IItemProvider... exclusions) {
        return of(Ingredient.fromTag(tag), exclusions);
    }

    public static ExclusionIngredient of(Ingredient parent, IItemProvider... exclusions) {
        List<Ingredient> list = new ArrayList<>();
        for (IItemProvider item : exclusions) {
            list.add(Ingredient.fromItems(item));
        }
        return new ExclusionIngredient(parent, list);
    }

    @Override
    public ItemStack[] getMatchingStacks() {
        List<ItemStack> ret = new ArrayList<>(Arrays.asList(parent.getMatchingStacks()));
        exclusions.forEach(ret::removeIf);
        return ret.toArray(new ItemStack[0]);
    }

    /**
     * Gets all items that would match the parent ingredient, including ones in the exclusions
     * list.
     *
     * @return All matching stacks of the parent ingredient
     */
    public ItemStack[] getMatchingStacksWithExclusions() {
        return parent.getMatchingStacks();
    }

    @Override
    public boolean test(@Nullable ItemStack stack) {
        if (stack == null || !parent.test(stack)) {
            return false;
        }

        for (Ingredient ingredient : exclusions) {
            if (ingredient.test(stack)) {
                return false;
            }
        }

        return true;
    }

    @Override
    protected void invalidate() {
    }

    @Override
    public boolean isSimple() {
        return parent.isSimple();
    }

    @Override
    public IIngredientSerializer<? extends Ingredient> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public JsonElement serialize() {
        JsonObject json = new JsonObject();
        json.addProperty("type", Serializer.NAME.toString());
        json.add("value", this.parent.serialize());
        JsonArray array = new JsonArray();
        this.exclusions.forEach(ingredient -> array.add(ingredient.serialize()));
        json.add("exclusions", array);
        return json;
    }

    public static class Serializer implements IIngredientSerializer<ExclusionIngredient> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation NAME = SilentLib.getId("exclusion");

        @Nonnull
        @Override
        public ExclusionIngredient parse(@Nonnull JsonObject json) {
            Ingredient value = Ingredient.deserialize(json.get("value"));

            List<Ingredient> list = new ArrayList<>();
            for (JsonElement e : json.get("exclusions").getAsJsonArray()) {
                if (e.isJsonPrimitive()) {
                    ResourceLocation id = new ResourceLocation(e.getAsString());
                    Item item = ForgeRegistries.ITEMS.getValue(id);
                    if (item != null) {
                        list.add(Ingredient.fromItems(item));
                    } else {
                        throw new JsonParseException("Unknown item: " + id);
                    }
                } else {
                    list.add(Ingredient.deserialize(e));
                }
            }

            return new ExclusionIngredient(value, list);
        }

        @Nonnull
        @Override
        public ExclusionIngredient parse(@Nonnull PacketBuffer buffer) {
            List<Ingredient> list = new ArrayList<>();
            int count = buffer.readByte();
            for (int i = 0; i < count; ++i) {
                list.add(Ingredient.read(buffer));
            }
            return new ExclusionIngredient(Ingredient.read(buffer), list);
        }

        @Override
        public void write(@Nonnull PacketBuffer buffer, @Nonnull ExclusionIngredient ingredient) {
            buffer.writeByte(ingredient.exclusions.size());
            for (Ingredient ing : ingredient.exclusions) {
                ing.write(buffer);
            }
            ingredient.parent.write(buffer);
        }

    }
}
