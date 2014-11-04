package bloodutils.api.classes.guide;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import bloodutils.api.classes.guide.buttons.ButtonNext;
import bloodutils.api.compact.Category;
import bloodutils.api.compact.Entry;
import bloodutils.api.entries.IEntry;
import bloodutils.api.registries.EntryRegistry;

public class GuiEntry extends GuiScreen{
	public GuiEntry(String key, EntityPlayer player, Category category){
		this.key = key;
		this.player = player;
		this.category = category;
	}
	
	public GuiEntry(String key, EntityPlayer player, Category category, int currPage){
		this.key = key;
		this.player = player;
		this.category = category;
		this.currPage = currPage;
	}
	
    private static final ResourceLocation gui = new ResourceLocation("bloodutils:textures/gui/guide.png");
	int gwidth = 192;
	int gheight = 192;
	int prevPage;
	int left, top;

	String key;
	
	int currPage = 1;
	GuiButton next, prev, back;
	EntityPlayer player;
	Category category;
	
	@SuppressWarnings("unchecked")
	@Override
	public void initGui(){
		super.initGui();
		left = (this.width/2) - (gwidth/2);
		top = (this.height/2) - (gheight/2);
		this.buttonList.clear();
		int k = (this.width - this.gwidth) / 2;
		
        this.buttonList.add(next = new ButtonNext(500, k + 120, top + 160, true));
        this.buttonList.add(prev = new ButtonNext(501, k + 38,  top + 160, false));
		
        Entry e = EntryRegistry.entries.get(this.category).get(this.key);
        if(e != null){
	        IEntry entry = e.entry[this.currPage - 1];
	        if(entry != null)
				entry.initGui(gwidth, gheight, left, top, player, this.buttonList);
        }else{
			mc.displayGuiScreen(new GuiCategories(this.player));
        }
	}
	
	@Override
	public void drawScreen(int mX, int mY, float f1){
		GL11.glColor4f(1F, 1F, 1F, 1F);
		this.mc.renderEngine.bindTexture(gui);
		drawTexturedModalRect(left, top, 0, 0, gwidth, gheight);
        Entry e = EntryRegistry.entries.get(this.category).get(this.key);

		/** Title */
		String str = e.name;
		this.drawCenteredString(fontRendererObj, str, this.left + gwidth / 2, top - 15, 0x336666);
		
		/** Current Page */
		this.drawCenteredString(fontRendererObj, (currPage) + "/" + (e.entry.length), this.left + gwidth / 2, top + 160, 0x336666);

		IEntry entry = e.entry[currPage - 1];
		if(entry != null){
			entry.draw(this, gwidth, gheight, left, top, player, e.name, currPage, mX, mY);
		}
		super.drawScreen(mX, mY, f1);
	}
	
	@Override
	public void mouseClicked(int mX, int mY, int type){
		super.mouseClicked(mX, mY, type);
		
		if(type  == 1)
			mc.displayGuiScreen(new GuiIndex(this.category, this.player));
	}
	
    @Override
    public void keyTyped(char c, int i){
    	super.keyTyped(c, i);
    	
    	if(Keyboard.getEventKeyState()){
    		if(i == 14){
    			mc.displayGuiScreen(new GuiIndex(this.category, this.player));
            	
    			return;
    		}
    	}
    }
    
	@Override
    public boolean doesGuiPauseGame(){
        return false;
    }
	
	@Override
	protected void actionPerformed(GuiButton button){
		int id = button.id;
		int maxPages = EntryRegistry.entries.get(this.category).get(this.key).entry.length;

		if(id == 500){
			if(currPage < maxPages){
				currPage++;
				initGui();
			}
		}else if(id == 501){
			if(currPage > 1){
				currPage--;
				initGui();
			}
		}else{
			Entry e = EntryRegistry.entries.get(this.category).get(this.key);
			if(e != null){
				IEntry entry = e.entry[this.currPage];
		        if(entry != null)
		        	entry.actionPerformed(button);
			}else{
    			mc.displayGuiScreen(new GuiCategories(this.player));
			}
		}
	}
	
	@Override
    public void onGuiClosed(){
		ItemStack held = player.getHeldItem();
		if(held.hasTagCompound()){
			held.getTagCompound().setString("CATEGORY", this.category.name);
			held.getTagCompound().setString("KEY", this.key);
			held.getTagCompound().setInteger("PAGE", this.currPage);
		}
	}
}