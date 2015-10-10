package com.cricketcraft.chisel.api;

import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatBasic;
import net.minecraft.util.ChatComponentTranslation;

public class Statistics {

    public static StatBase blocksChiseled = (new StatBasic("stat.blockChiseled", new ChatComponentTranslation("stat.blockChiseled", new Object[0])));

    public static void init(){
        blocksChiseled.initIndependentStat();
        blocksChiseled.registerStat();
    }
}
