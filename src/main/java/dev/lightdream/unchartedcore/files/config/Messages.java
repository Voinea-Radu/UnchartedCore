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

    //Anvils
    public String typeName= "Please type your new name.";


}
