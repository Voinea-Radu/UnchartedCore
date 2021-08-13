package dev.lightdream.unchartedcore.files.config;

import dev.lightdream.unchartedcore.Main;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class Messages {

    public String prefix = "[" + Main.PROJECT_NAME + "] ";

    public String mustBeAPlayer = "You must be a player to use this command.";
    public String mustBeConsole = "You must be console to use this command.";
    public String noPermission = "You do not have the permission to use this command.";
    public String unknownCommand = "This is not a valid command.";
    public String invalidUser = "This is an invalid user";

    //Leave empty for auto-generated
    public List<String> helpCommand = new ArrayList<>();

    //Errors
    public String invalidNumber = "This is not a valid number";

    //SignShop
    public String signBuy = "BUY";
    public String signSell = "SELL";
    public String signItem = "%item%";
    public String signPrice = "$%price%";
    public String signMoneyEarned = "Earned: $%amount%";
    public String signMoneySpent = "Spent: $%amount%";
    public String signBuyInfo = "Buy Sign Info line";
    public String signSellInfo = "Sell Sign Info line";
    public String signShopCreated = "The sign shop have been successfully created";
    public String signShopDeleted = "The sign shop have been successfully deleted";
    public String signShopDeletedSessionStarted = "You have entered the sign shop delete session";
    public String signShopDeletedSessionStopped = "You have exited the sign shop delete session";
    public String signShopNotEnoughMoney = "Not enough money";
    public String signShopNotEnoughItems = "Not enough items";
    public String sold = "You have sold %amount% items for $%price%";
    public String invalidItem = "This item is invalid";

    //Anvils
    public String typeName = "Please type your new name.";

    //PlayerHeads
    public String invalidHead = "This is not a valid player head";
    public String cannotSellOwnHead = "You can not sell your own head";
    public String soldHead = "Sold the head for";
    public String soldTimes = "This head has been sold %count% times";

    //Stats
    public String statsSignInfoLine = "TOP";
    public String statsSignStatLine = "%stat%";
    public String statsSignPlayerLine = "%player%";
    public String statsSignValueLine = "%value%";
    public String statSignCreated = "Stat Sign created";
    public String statSignRemoved = "Stat Sign removed";

    //Homes
    public String homeSet = "You have set your home";
    public String homeDeleted = "You have deleted your home";
    public String homeDoesNotExist = "This home does not exist";
    public String homeAlreadyExists = "This home already exists";
    public String notAllowedToAllowHomes = "You are not allowed to allow other factions to have home on you factions claims";
    public String homesAllowed = "This faction can now set homes on your faction's claims";
    public String homesDenied = "This faction can no longer set homes on your faction's claims";
    public String homes = "Homes: %homes%";
    public String homeAllows = "Allows: %allows%";
    public String invalidFaction = "This is an invalid faction";

    //Kits
    public String kitDoesNotExist = "This kit does not exist";
    public String kitCreated = "This kit has been created";
    public String kitDeleted = "This kit has been deleted";
    public String kits = "Kits: ";

    //Pearls
    public String enderPearlCoolDown= "You can only use a pearl at 5s";


}
