package WayofTime.bloodmagic.util.handler;

import net.minecraftforge.fml.common.event.FMLInterModComms;

public class IMCHandler {

    public static void handleIMC(FMLInterModComms.IMCEvent event) {
        // TODO
//        for (FMLInterModComms.IMCMessage message : event.getMessages())
//        {
//            if (message.key.equals("teleposerBlacklist") && message.isItemStackMessage())
//            {
//                ItemStack stack = message.getItemStackValue();
//                if (stack.getItem() instanceof ItemBlock)
//                {
//                    Block block = Block.getBlockFromItem(stack.getItem());
//                    BloodMagicAPI.addToTeleposerBlacklist(block, stack.getItemDamage());
//                }
//            }
//
//            if (message.key.equals("transpositionBlacklist") && message.isItemStackMessage())
//            {
//                ItemStack stack = message.getItemStackValue();
//                if (stack.getItem() instanceof ItemBlock)
//                {
//                    Block block = Block.getBlockFromItem(stack.getItem());
//                    BloodMagicAPI.addToTranspositionBlacklist(block, stack.getItemDamage());
//                }
//            }
//
//            if (message.key.equals("sacrificeValue") && message.isStringMessage())
//            {
//                String[] splitInfo = message.getStringValue().split(";");
//                if (splitInfo.length == 2 && Utils.isInteger(splitInfo[1]))
//                    BloodMagicAPI.setEntitySacrificeValue(splitInfo[0], Integer.parseInt(splitInfo[1]));
//            }
//
//            if (message.key.equals("greenGroveBlacklist") && message.isStringMessage())
//            {
//                String[] splitInfo = message.getStringValue().split(":");
//                if (splitInfo.length == 2)
//                {
//                    Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(splitInfo[0], splitInfo[1]));
//                    if (block != null)
//                        BloodMagicAPI.blacklistFromGreenGrove(block);
//                }
//            }
//
//            if (message.key.equals("altarComponent") && message.isStringMessage())
//            {
//                String[] splitInfo = message.getStringValue().split(":");
//                if (splitInfo.length == 4)
//                {
//                    Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(splitInfo[0], splitInfo[1]));
//                    if (block != null)
//                        BloodMagicAPI.addAltarComponent(block.getStateFromMeta(Integer.parseInt(splitInfo[2])), EnumAltarComponent.valueOf(splitInfo[3]));
//                }
//            }
//        }
    }
}
