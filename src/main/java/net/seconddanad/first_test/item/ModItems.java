package net.seconddanad.first_test.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.seconddanad.first_test.FirstTest;
import net.seconddanad.first_test.item.custom.TestItem;

public class ModItems {

    public static final Item TEST_ITEM = registerItem(
            "test_item",
            new Identifier("ingredients"),
            new TestItem(new FabricItemSettings()
                    .fireproof()
                    .maxCount(16)
    ));

    public static void registerModItems() {

    }

    private static Item registerItem(String name, Identifier itemGroupIdentifier, Item item) {
        registerItemGroup(item, itemGroupIdentifier);
        return Registry.register(Registries.ITEM, new Identifier(FirstTest.MOD_ID, name), item);

    }

    private static void registerItemGroup(Item item, Identifier itemGroupIdentifier) {
        ItemGroupEvents.modifyEntriesEvent(RegistryKey.of(RegistryKeys.ITEM_GROUP, itemGroupIdentifier)).register(content -> {
            content.add(item);
        });
     }
}