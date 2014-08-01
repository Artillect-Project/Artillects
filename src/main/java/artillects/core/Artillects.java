package artillects.core;

import java.io.File;
import java.util.Arrays;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.oredict.ShapedOreRecipe;
import resonant.lib.content.ContentRegistry;
import resonant.lib.content.IDManager;
import resonant.lib.network.PacketEntity;
import resonant.lib.network.PacketHandler;
import resonant.lib.network.PacketTile;
import resonant.lib.recipe.UniversalRecipe;
import resonant.lib.utility.LanguageUtility;
import resonant.lib.utility.nbt.SaveManager;
import artillects.content.items.claim.ItemClaimFlag;
import artillects.content.tool.extractor.TileExtractor;
import artillects.content.tool.surveyor.TileSurveyor;
import artillects.core.creation.ContentFactory;
import artillects.core.region.Faction;
import artillects.core.region.Village;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

/** @author DarkGuardsman */
@Mod(modid = Reference.NAME, name = Reference.NAME, version = Reference.VERSION, dependencies = "required-after:ResonantEngine;")
@NetworkMod(channels = Reference.CHANNEL, clientSideRequired = true, serverSideRequired = false, packetHandler = PacketHandler.class)
public class Artillects
{

    @Instance(Reference.NAME)
    public static Artillects INSTANCE;

    @SidedProxy(clientSide = "artillects.core.ClientProxy", serverSide = "artillects.core.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Metadata(Reference.NAME)
    public static ModMetadata metadata;

    public static Configuration CONFIG;

    /** Packets */
    public static final PacketTile PACKET_TILE = new PacketTile(Reference.CHANNEL);
    public static final PacketEntity PACKET_ENTITY = new PacketEntity(Reference.CHANNEL);

    /** Blocks and Items */
    public static ContentRegistry contentRegistry;
    public static Item itemClaimFlag;
    public static Block blockSurveyor;
    public static Block blockExtractor;

    @EventHandler
    public void preInit(FMLPreInitializationEvent evt)
    {
        NetworkRegistry.instance().registerGuiHandler(this, proxy);
        SaveManager.registerClass("Faction", Faction.class);
        SaveManager.registerClass("Village", Village.class);
        //Create config 
        CONFIG = new Configuration(new File(Loader.instance().getConfigDir(), Reference.NAME + ".cfg"));

        //load configs
        CONFIG.load();
        
        //create id manager
        int blockIDPrefix = CONFIG.get(Configuration.CATEGORY_GENERAL, "BlockIDPrefix", 1700).getInt(1700);
        int itemIDPrefix = CONFIG.get(Configuration.CATEGORY_GENERAL, "ItemIDPrefix", 20150).getInt(20150);

        //create content registry
        contentRegistry = new ContentRegistry(CONFIG, new IDManager(blockIDPrefix, itemIDPrefix), Reference.NAME).setPrefix(Reference.PREFIX).setTab(ArtillectsTab.instance());
        
        ContentFactory loader = new ContentFactory(contentRegistry);
        try
        {
            loader.load();
            loader.createAll();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        itemClaimFlag = contentRegistry.createItem(ItemClaimFlag.class);

        blockSurveyor = contentRegistry.newBlock(TileSurveyor.class);
        blockExtractor = contentRegistry.newBlock(TileExtractor.class);

        LanguageUtility.loadLanguages(Reference.LANGUAGE_DIRECTORY, Reference.LANGUAGES);
        proxy.preInit();
        setModMetadata();
    }

    @EventHandler
    public void init(FMLInitializationEvent evt)
    {
        proxy.init();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent evt)
    {
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockSurveyor), "IGI", "ICI", "I I", 'G', Block.glass, 'C', UniversalRecipe.CIRCUIT_T2.get(), 'I', UniversalRecipe.PRIMARY_METAL.get()));
        //save configs
        CONFIG.save();
        proxy.postInit();
    }

    public void setModMetadata()
    {
        metadata.modId = Reference.NAME;
        metadata.name = Reference.NAME;
        metadata.description = LanguageUtility.getLocal("meta.artillects.description");
        metadata.url = "ARTILLECTS-MOD.COM";
        metadata.logoFile = "artillects_logo.png";
        metadata.version = Reference.VERSION + "." + Reference.BUILD_VERSION;
        metadata.authorList = Arrays.asList(new String[] { "DarkCow", "Calclavia", "Archadia" });
        metadata.credits = LanguageUtility.getLocal("meta.artillects.credits");
        metadata.autogenerated = false;
    }

    public static boolean isOp(String username)
    {
        MinecraftServer theServer = FMLCommonHandler.instance().getMinecraftServerInstance();

        if (theServer != null)
        {
            return theServer.getConfigurationManager().getOps().contains(username.trim().toLowerCase());
        }

        return false;
    }
}
