package bloodutils.api.classes.guide;

import java.util.HashMap;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import bloodutils.api.classes.guide.buttons.ButtonNext;
import bloodutils.api.classes.guide.buttons.ButtonPage;
import bloodutils.api.classes.guide.elements.ElementCategory;
import bloodutils.api.compact.Category;
import bloodutils.api.compact.Entry;
import bloodutils.api.registries.EntryRegistry;

public class GuiIndex extends GuiScreen{
	public GuiIndex(Category category, EntityPlayer player){
		this.category = category;
		this.player = player;
	}
	
	public GuiIndex(Category category, EntityPlayer player, int currPage){
		this.category = category;
		this.player = player;
		this.currPage = currPage;
	}
    private static final ResourceLocation gui = new ResourceLocation("bloodutils:textures/gui/guide.png");
    GuiButton prev, next, back;
    
    Category category;
    EntityPlayer player;
    
	ElementCategory[] categories = new ElementCategory[EntryRegistry.categories.size()];

	int gwidth = 192;
	int gheight = 192;
	int left, top;
	int currPage = 0;
	
	@SuppressWarnings("unchecked")
	@Override
	public void initGui(){
		super.initGui();
		left = (this.width/2) - (gwidth/2);
		top = (this.height/2) - (gheight/2);
		this.buttonList.clear();
        
		populate();
		drawCategories();
		
		int k = (this.width - this.gwidth) / 2;
        this.buttonList.add(next = new ButtonNext(500, k + 120, top + 160, true));
        this.buttonList.add(prev = new ButtonNext(501, k + 38,  top + 160, false));
	}
	
	public void drawCategories(){
		int pX = left - 1;
		int pY = top + 12;
		
		int iWidth = 20;
		int iHeight = 20;
		for(int i = 0; i < EntryRegistry.categories.size(); i++){
			Category category = EntryRegistry.categories.get(i);
			this.categories[i] = new ElementCategory(category, pX, pY + (i*iHeight) - 2, iWidth, iHeight, this.player);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void populate(){
		this.buttonList.clear();

		HashMap<String, Entry> entries = EntryRegistry.entries.get(this.category);
		int x, y;
		
		if(entries != null && !entries.isEmpty()){
			int j = 0;
			for(int i = 0; i < entries.size(); i++){
				Entry entry = (Entry)entries.values().toArray()[i];
				if(entry != null && entry.indexPage == this.currPage){
					x = this.left + gwidth / 2 - 75;
					y = (top + 15) + (10*j);
					buttonList.add(new ButtonPage(j, x, y, 110, 10, ""));
					j++;
				}
			}
		}
		
		int k = (this.width - this.gwidth) / 2;
		
        this.buttonList.add(next = new ButtonNext(500, k + 120, top + 160, true));
        this.buttonList.add(prev = new ButtonNext(501, k + 38,  top + 160, false));
	}
	
	@Override
	public void drawScreen(int mX, int mY, float f1){
		GL11.glColor4f(1F, 1F, 1F, 1F);
		this.mc.renderEngine.bindTexture(gui);
		drawTexturedModalRect(left, top, 0, 0, gwidth, gheight);
		
		/** Title */
		String str = category.name;
		this.drawCenteredString(fontRendererObj, str, this.left + gwidth / 2, top - 15, 0x336666);
		
		/** Current Page */
		if(this.category != null && EntryRegistry.maxEntries.containsKey(this.category)){
			int size = EntryRegistry.maxEntries.get(this.category);
			this.drawCenteredString(fontRendererObj, (currPage + 1) + "/" + (size + 1), this.left + gwidth / 2, top + 160, 0x336666);
			registerButtons();
		}
		
		for(int i = 0; i < EntryRegistry.categories.size(); i++){
			ElementCategory category = this.categories[i];
			category.drawElement();
			
			if(category.isMouseInElement(mX, mY)){
				category.onMouseEnter(mX, mY);
			}
		}
		super.drawScreen(mX, mY, f1);
	}
	
	public void registerButtons(){
		HashMap<String, Entry> entries = EntryRegistry.entries.get(this.category);		
		if(entries != null && !entries.isEmpty()){
			int j = 0;
			for(int i = 0; i < entries.size(); i++){
				Entry entry = (Entry)entries.values().toArray()[i];

				if(entry != null && entry.indexPage == this.currPage){
					String title = entry.name;
					if(title != null && buttonList.get(j) != null && buttonList.get(j) instanceof ButtonPage){
						ButtonPage button = (ButtonPage) buttonList.get(j);
						button.displayString = title;
						j++;
					}
				}
			}
		}
	}
	
	@Override
	public void mouseClicked(int mX, int mY, int type){
		super.mouseClicked(mX, mY, type);
		
		if(type  == 1)
			mc.displayGuiScreen(new GuiCategories(this.player));
		
		for(int i = 0; i < EntryRegistry.categories.size(); i++){
			ElementCategory category = this.categories[i];
			if(category.isMouseInElement(mX, mY)){
				category.onMouseClick(mX, mY, type);
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
		int size;
		if(EntryRegistry.maxEntries.containsKey(this.category))
			size = EntryRegistry.maxEntries.get(this.category);
		else
			size = 1;
		if(id == 500){
			if(currPage + 1 < size + 1){
				currPage++;
				populate();
				registerButtons();
			}
		}else if(id == 501){
			if(currPage > 0){
				currPage--;
				populate();
				registerButtons();
			}
		}else{
			mc.displayGuiScreen(new GuiEntry(button.displayString, player, category));
		}
	}
	
    @Override
    public void keyTyped(char c, int i){
    	super.keyTyped(c, i);
    	
    	if(Keyboard.getEventKeyState()){
    		if(i == 14){
    			mc.displayGuiScreen(new GuiCategories(this.player));
            	
    			return;
    		}
    	}
    }
    
	@Override
    public void onGuiClosed(){
		ItemStack held = player.getHeldItem();
		if(held.hasTagCompound()){
			held.getTagCompound().setString("CATEGORY", this.category.name);
			held.getTagCompound().setString("KEY", "0");
			held.getTagCompound().setInteger("PAGE", this.currPage);
		}
	}
}