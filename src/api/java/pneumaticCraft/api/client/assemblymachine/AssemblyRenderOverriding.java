package pneumaticCraft.api.client.assemblymachine;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class AssemblyRenderOverriding{
    public static final HashMap<Integer, IAssemblyRenderOverriding> renderOverrides = new HashMap<Integer, IAssemblyRenderOverriding>();

    public static void addRenderOverride(Block block, IAssemblyRenderOverriding renderOverride){
        renderOverrides.put(Block.getIdFromBlock(block), renderOverride);
    }

    public static void addRenderOverride(Item item, IAssemblyRenderOverriding renderOverride){
        renderOverrides.put(Item.getIdFromItem(item), renderOverride);
    }

    public static interface IAssemblyRenderOverriding{
        /**
         * This method will be called just before the IO Unit's held stack is being rendered. You can insert GL11 calls here to
         * rotate the model for example. push and pop matrices are not needed, this is done for you.
         *  You can also choose to do the whole rendering yourself, you'll need to return false then to indicate that
         *  PneumaticCraft shouldn't render the item.
         *  @param renderedStack itemStack that is being rendered
         *  @return true if PneumaticCraft should render the item (after your changes), or false to cancel rendering.
         */
        public boolean applyRenderChangeIOUnit(ItemStack renderedStack);

        /**
         * Same deal as with the applyRenderChangeIOUnit(), but now for the Assembly Platform.
         * @param renderedStack itemStack that is being rendered
         * @return true if PneumaticCraft should render the item (after your changes), or false to cancel rendering.
         */
        public boolean applyRenderChangePlatform(ItemStack renderedStack);

        /**
         * Should return the distance the claw travels before it is gripped to the stack.
         * By default it's 0.0875F for items and 0.00625F for blocks, 0.09375 when the claw is completely closed.
         * @param renderedStack
         * @return
         */
        public float getIOUnitClawShift(ItemStack renderedStack);

        /**
         * Should return the distance the claw travels before it is gripped to the stack.
         * By default it's 0.0875F for items and 0.00625F for blocks, 0.09375 when the claw is completely closed.
         * @param renderedStack
         * @return
         */
        public float getPlatformClawShift(ItemStack renderedStack);

    }
}
