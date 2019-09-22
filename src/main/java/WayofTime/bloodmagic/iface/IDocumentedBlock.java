package WayofTime.bloodmagic.iface;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Marks blocks as one that is documented.
 * <p>
 * This documentation can be read by an
 * {@link WayofTime.bloodmagic.item.ItemSanguineBook} (or child)
 */
public interface IDocumentedBlock {
    /**
     * Provides the documentation to provide to the player. Usually a
     * short'n'sweet description about basic usage.
     *
     * @param player - The EntityPlayer attempting to view the Documentation.
     * @param world  - The World interaction is happening in.
     * @param pos    - The BlockPos being interacted at.
     * @param state  - The IBlockState of the interacted Block.
     * @return - A list of formatted ITextComponent to provide to the player.
     * Provide an empty list if there is no available documentation.
     */
    @Nonnull
    List<ITextComponent> getDocumentation(PlayerEntity player, World world, BlockPos pos, BlockState state);
}
