package com.deyo.hypixel.type.generic.utility;

import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;

public final class WebLinks {
    public static final String STORE = "https://store.hypixel.net";
    public static final String FORUMS = "https://hypixel.net/forums/";
    public static final String GUILDS = "https://hypixel.net/guilds/";
    public static final String DAILY_REWARDS = "https://rewards.hypixel.net/claim-reward/";
    public static final String SOCIAL_MEDIA_REWARDS = "https://hypixel.net/social-media-rewards/";

    private WebLinks() {
    }

    public static Text storeLink() {
        return link(STORE, "<e>Click here to visit the Hypixel Store!");
    }

    public static Text forumsLink() {
        return link(FORUMS, "<e>Click here to visit the Hypixel Forums!");
    }

    public static Text guildsLink() {
        return link(GUILDS, "<e>Click here to browse Hypixel guilds!");
    }

    public static Text dailyRewardLink() {
        return link(DAILY_REWARDS, "<e>Click here to claim your Daily Reward!");
    }

    public static Text socialMediaRewardsLink() {
        return link(SOCIAL_MEDIA_REWARDS, "<e>Click here to view Social Media Rewards!");
    }

    public static Text link(String url, String labelMarkup) {
        return Text.read("<click:url:'" + url + "'>" + labelMarkup);
    }

    public static void sendStoreLink(HypixelPlayer player) {
        player.sendMessage("<6><l>HYPIXEL STORE</l>");
        player.sendMessage(storeLink());
    }

    public static void sendForumsLink(HypixelPlayer player) {
        player.sendMessage("<6><l>HYPIXEL FORUMS</l>");
        player.sendMessage(forumsLink());
    }

    public static void sendGuildsLink(HypixelPlayer player) {
        player.sendMessage("<6><l>GUILD FINDER</l>");
        player.sendMessage(guildsLink());
    }

    public static void sendDailyRewardLink(HypixelPlayer player) {
        player.sendMessage("<6><l>DAILY REWARD</l>");
        player.sendMessage(dailyRewardLink());
    }

    public static void sendSocialMediaRewardsLink(HypixelPlayer player) {
        player.sendMessage("<6><l>SOCIAL MEDIA REWARDS</l>");
        player.sendMessage(socialMediaRewardsLink());
    }
}
