package net.silentchaos512.lib.registry;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ItemRegistryObject<T extends Item> extends RegistryObjectWrapper<T> implements ItemLike {
    public ItemRegistryObject(DeferredRegister<Item> deferredRegister, String name, Supplier<T> itemSupplier) {
        this(deferredRegister.register(name, itemSupplier));
    }

    public ItemRegistryObject(RegistryObject<T> item) {
        super(item);
    }

    @Override
    public Item asItem() {
        return registryObject.get();
    }
}
