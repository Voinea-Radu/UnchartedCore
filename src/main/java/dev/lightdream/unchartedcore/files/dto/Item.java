package dev.lightdream.unchartedcore.files.dto;

import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"MethodDoesntCallSuperMethod", "unused"})
@NoArgsConstructor
public class Item {

    public XMaterial material;
    public int amount;
    public String displayName;
    public String headData;
    public String headOwner;
    public List<String> lore;
    public Integer slot;

    public Item(XMaterial material, int amount) {
        this.material = material;
        this.amount = amount;
        this.lore = new ArrayList<>();
        if (material.parseMaterial() == null) {
            this.displayName = "Item";
        } else {
            this.displayName = material.parseMaterial().name();
        }
    }

    public Item(XMaterial material, int amount, String displayName, List<String> lore) {
        this.material = material;
        this.amount = amount;
        this.lore = lore;
        this.displayName = displayName;
    }

    public Item(XMaterial material, int slot, int amount, String displayName, List<String> lore) {
        this.material = material;
        this.amount = amount;
        this.lore = lore;
        this.displayName = displayName;
        this.slot = slot;
    }

    public Item(XMaterial material, int slot, String headData, int amount, String displayName, List<String> lore) {
        this.material = material;
        this.amount = amount;
        this.lore = lore;
        this.displayName = displayName;
        this.slot = slot;
        this.headData = headData;
    }

    public Item(XMaterial material, int slot, int amount, String displayName, String headOwner, List<String> lore) {
        this.material = material;
        this.amount = amount;
        this.lore = lore;
        this.displayName = displayName;
        this.headOwner = headOwner;
        this.slot = slot;
    }

    public Item(XMaterial material, int amount, String displayName, String headOwner, List<String> lore) {
        this.material = material;
        this.amount = amount;
        this.lore = lore;
        this.displayName = displayName;
        this.headOwner = headOwner;
    }

    public Item clone() {
        Item item = new Item(this.material, this.amount, this.displayName, this.lore);

        if (slot != null) item.slot = this.slot;
        if (headOwner != null) item.headOwner = this.headOwner;
        if (headData != null) item.headData = this.headData;

        return item;
    }


    @Override
    public String toString() {
        return "Item{" +
                "material=" + material +
                ", amount=" + amount +
                ", displayName='" + displayName + '\'' +
                ", headData='" + headData + '\'' +
                ", headOwner='" + headOwner + '\'' +
                ", lore=" + lore +
                ", slot=" + slot +
                '}';
    }
}
