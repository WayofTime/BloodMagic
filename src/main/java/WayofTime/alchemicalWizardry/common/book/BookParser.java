package WayofTime.alchemicalWizardry.common.book;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.spell.APISpellHelper;
import WayofTime.alchemicalWizardry.book.compact.Entry;
import WayofTime.alchemicalWizardry.book.entries.EntryCraftingRecipeCustomText;
import WayofTime.alchemicalWizardry.book.entries.EntryImageCustomText;
import WayofTime.alchemicalWizardry.book.entries.EntryItemCustomText;
import WayofTime.alchemicalWizardry.book.entries.EntryTextCustomText;
import WayofTime.alchemicalWizardry.book.entries.IEntryCustomText;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BookParser 
{
	@SideOnly(Side.CLIENT)
    public static List<Entry> parseTextFile(String location)
    {
//    	File textFiles = new File("config/BloodMagic/bookDocs");
    	ArrayList<Entry> entryList = new ArrayList();
    	//if(textFiles.exists())
    	{
    		try {
    			System.out.println("I am in an island of files!");
    			
                InputStream input = AlchemicalWizardry.class.getResourceAsStream(location);

        		Minecraft.getMinecraft().fontRenderer.setUnicodeFlag(true);
                
                if(input != null)
                {
                	DataInputStream in = new DataInputStream(input);
                	BufferedReader br = new BufferedReader(new InputStreamReader(in));
        			String strLine;
        			//Read File Line By Line
        			
        			int defMaxLines = 16;
        			int maxLines = defMaxLines;
        			
        			int currentPage = 0;
        			
        			int pageIndex = 1;
        			
        			String currentTitle = "aw.entry.Magnus";
        			
        			String[] strings = new String[1];
        			strings[0] = "";
        			
        			//New entry stuff
        			ArrayList<IEntryCustomText> iEntryList = new ArrayList();
        			IEntryCustomText currentIEntry = new EntryTextCustomText();
        			
        			boolean lastPageWasSpecial = true;
        			
        			int entriesPerPage = 14;
        			        			
        			while ((strLine = br.readLine()) != null)   
        			{
        				if(strLine.trim().isEmpty())
        				{
        					continue;
        				}
        				
        				if(strLine.startsWith("//TITLE ")) //New entry~
        				{
        					lastPageWasSpecial = false;
        					String[] newStrings = new String[currentPage + 1 + 1]; //Just to show that it is increasing
    						for(int i=0; i<strings.length; i++)
    						{
    							newStrings[i] = strings[i];
    						}
    						
    						if(currentPage != 0) /* New stuff */
    						{
    							currentIEntry.setText(strings[currentPage]);
        						iEntryList.add(currentIEntry);
        						
        						Entry entry = new Entry(BookParser.getArrayForList(iEntryList), currentTitle, entryList.size() / entriesPerPage + 1);
        						entryList.add(entry);
        						iEntryList.clear();
        						
        						currentIEntry = new EntryTextCustomText();
    						}
    						
    						currentPage++;
    						newStrings[currentPage - 1] = currentTitle + "." + pageIndex + "=" + newStrings[currentPage - 1];
    						newStrings[currentPage] = "";
    						strings = newStrings;
    						
    						pageIndex = 1;
    						
    						String title = strLine.replaceFirst("//TITLE ", " ").trim();
    						currentTitle = title;
    						
        					continue;
        				}else if(BookParser.containsSpecialInfo(strLine))
        				{
        					if(!strings[currentPage].isEmpty() || lastPageWasSpecial)
        					{
        						String[] newStrings = new String[currentPage + 1 + 1]; //Just to show that it is increasing
        						for(int i=0; i<strings.length; i++)
        						{
        							newStrings[i] = strings[i];
        						}
        						
        						currentIEntry.setText(strings[currentPage]);
        						iEntryList.add(currentIEntry);
        						
        						currentPage++;
        						newStrings[currentPage - 1] = currentTitle + "." + pageIndex + "=" + newStrings[currentPage - 1];
        						newStrings[currentPage] = "";
        						strings = newStrings;
        					}
        					
    						currentIEntry = BookParser.getEntryForStringTitle(strLine);
    						maxLines = BookParser.getlineLimitForStringTitle(strLine, defMaxLines);
    						
    						lastPageWasSpecial = true;
        					
        					continue;
        				}
        				
        				strLine = strLine.replace('”', '"').replace('“','"').replace("…", "...").replace('’', '\'').replace('–', '-');
        				
        				if(Minecraft.getMinecraft() != null && Minecraft.getMinecraft().fontRenderer != null)
        				{
        					List list = Minecraft.getMinecraft().fontRenderer.listFormattedStringToWidth(strLine, 110);
//            				if(list != null)
            				{
                				//System.out.println("Number of lines: " + list.size());
            				}
        				}
	        				
        				String[] cutStrings = strLine.split(" ");

        				for(String word : cutStrings)
        				{
        					lastPageWasSpecial = true;
        					boolean changePage = false;
//        					int length = word.length();
        					word = word.replace('\t', ' ');
        					List list = Minecraft.getMinecraft().fontRenderer.listFormattedStringToWidth(strings[currentPage] + " " + word, 110);

        					if(list.size() > maxLines)
        					{
        						changePage = true;
        					}
        					if(changePage) //Encode into current entry, then move to next entry
        					{
        						String[] newStrings = new String[currentPage + 1 + 1]; //Just to show that it is increasing
        						for(int i=0; i<strings.length; i++)
        						{
        							newStrings[i] = strings[i];
        						}
        						
        						currentIEntry.setText(strings[currentPage]);
        						iEntryList.add(currentIEntry);
        						
        						currentIEntry = new EntryTextCustomText();
        						
        						currentPage++;

        						newStrings[currentPage - 1] = currentTitle + "." + pageIndex + "=" + newStrings[currentPage - 1];
        						newStrings[currentPage] = word;
        						strings = newStrings;
        						
        						pageIndex++;
        						
        						maxLines = defMaxLines;
        						
        						changePage = false;
        					}else
        					{
        						strings[currentPage] = strings[currentPage] + " " + word;
        					}
        				}
        				
        				int currentLines = Minecraft.getMinecraft().fontRenderer.listFormattedStringToWidth(strings[currentPage], 110).size();
        				while(Minecraft.getMinecraft().fontRenderer.listFormattedStringToWidth(strings[currentPage] + " ", 110).size() <= currentLines)
        				{
        					{
            					strings[currentPage] = strings[currentPage] + " ";
        					}
        				}
        				
    					//System.out.println("" + strLine);
    				}
        			
        			strings[currentPage] = strings[currentPage];
        			
        			//
        			currentIEntry.setText(strings[currentPage]);
					iEntryList.add(currentIEntry);
					
					Entry entry = new Entry(BookParser.getArrayForList(iEntryList), currentTitle, entryList.size() / entriesPerPage + 1);
					entryList.add(entry);
					iEntryList.clear();
					//
        			
//        	        File bmDirectory = new File("src/main/resources/assets/alchemicalwizardryBooks");
//        	        if(!bmDirectory.exists())
//        	        {
//        	        	bmDirectory.mkdirs();
//        	        }
//
//        	        File file = new File(bmDirectory, "books.txt");
////                    if (file.exists() && file.length() > 3L)
////                    {
////                        
////                    }else
//                    {
//                    	PrintWriter writer = new PrintWriter(file);
//            			for(String stri : strings)
//            			{
//            				writer.println(stri);
//            			}
//            			writer.close();
//                    }
        			
//        			
                }
                
        		Minecraft.getMinecraft().fontRenderer.setUnicodeFlag(false);

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	
    	return entryList;
    }
	
	public static IEntryCustomText[] getArrayForList(List<IEntryCustomText> list)
	{
		Object[] tempArray = list.toArray();
		IEntryCustomText[] customTextArray = new IEntryCustomText[tempArray.length];
		for(int i=0; i<tempArray.length; i++)
		{
			if(tempArray[i] instanceof IEntryCustomText)
			{
				customTextArray[i] = (IEntryCustomText)tempArray[i];
			}
		}
		return customTextArray;
	}
	
	public static boolean containsSpecialInfo(String unparsedString)
	{
		return unparsedString.startsWith("//IMAGE") || unparsedString.startsWith("//CRAFTING") || unparsedString.startsWith("//ITEM");
	}
	
	public static IEntryCustomText getEntryForStringTitle(String unparsedString)
	{
		if(unparsedString.startsWith("//IMAGE ")) //Format is //IMAGE maxLines xSize ySize ImageString (optional)Title
		{
			String lines = unparsedString.replaceFirst("//IMAGE ", "");
			String[] arguments = lines.split(" ");
			if(arguments.length < 4)
			{
				return null;
			}
			
			int xSize = Integer.decode(arguments[1]);
			int ySize = Integer.decode(arguments[2]);
			if(arguments.length >= 5)
			{
				return new EntryImageCustomText(arguments[3], xSize, ySize, arguments[4]);
			}else
			{
				return new EntryImageCustomText(arguments[3], xSize, ySize);
			}
		}else if(unparsedString.startsWith("//CRAFTING "))
		{
			String lines = unparsedString.replaceFirst("//CRAFTING ", "");
			ItemStack stack = APISpellHelper.getItemStackForString(lines);
			IRecipe recipe = APISpellHelper.getRecipeForItemStack(stack);
			if(recipe != null)
			{
				return new EntryCraftingRecipeCustomText(recipe);
			}
		}else if(unparsedString.startsWith("//ITEM "))
		{
			String lines = unparsedString.replaceFirst("//ITEM ", "");
			ItemStack stack = APISpellHelper.getItemStackForString(lines);
			if(stack != null)
			{
				return new EntryItemCustomText(stack);
			}
		}
		
		return new EntryTextCustomText();
	}
	
	public static int getlineLimitForStringTitle(String unparsedString, int def)
	{
		if(unparsedString.startsWith("//IMAGE "))
		{
			String lines = unparsedString.replaceFirst("//IMAGE ", "");
			String[] arguments = lines.split(" ");
			if(arguments.length < 4)
			{
				return def;
			}
			
			return Integer.decode(arguments[0]);
		}else if(unparsedString.startsWith("//CRAFTING "))
		{
			return 0;
		}else if(unparsedString.startsWith("//ITEM "))
		{
			return 9;
		}
		
		return def;
	}
}
