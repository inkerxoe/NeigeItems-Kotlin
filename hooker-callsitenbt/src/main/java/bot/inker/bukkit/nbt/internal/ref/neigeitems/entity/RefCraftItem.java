package bot.inker.bukkit.nbt.internal.ref.neigeitems.entity;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;

@HandleBy(version = CbVersion.v1_12_R1, reference = "org/bukkit/craftbukkit/v1_12_R1/entity/CraftItem")
public final class RefCraftItem extends RefCraftEntity {
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lorg/bukkit/craftbukkit/v1_12_R1/entity/CraftItem;item:Lnet/minecraft/server/v1_12_R1/EntityItem;", accessor = true)
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lorg/bukkit/craftbukkit/v1_17_R1/entity/CraftItem;item:Lnet/minecraft/world/entity/item/ItemEntity;", accessor = true)
    @HandleBy(version = CbVersion.v1_20_R2, reference = "")
    public RefEntityItem item;

    @HandleBy(version = CbVersion.v1_20_R2, reference = "Lorg/bukkit/craftbukkit/v1_20_R2/entity/CraftItem;getHandle()Lnet/minecraft/world/entity/item/ItemEntity;")
    public native RefEntityItem getHandle();
}