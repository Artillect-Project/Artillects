package artillects.core.village;

import java.io.File;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import universalelectricity.api.vector.VectorWorld;
import artillects.core.building.GhostObject;
import calclavia.lib.utility.nbt.IVirtualObject;
import calclavia.lib.utility.nbt.NBTUtility;
import calclavia.lib.utility.nbt.SaveManager;

/** Small area of buildings grouped together into an area of living.
 * 
 * @author Darkguardsman */
public class Village extends GhostObject implements IVirtualObject
{
    protected int radius = 10;
    private File saveFile;
    private String name;
    private String saveName;

    private Village()
    {
        if (name == null)
        {
            name = "Village";
        }
        if (saveName == null)
        {
            saveName = "Village" + System.currentTimeMillis();
        }
        saveFile = new File(NBTUtility.getSaveDirectory(), "artillects/villages/Village_" + this.saveName);
        SaveManager.register(this);
    }

    public Village(VectorWorld vec, int radius)
    {
        this(vec.world, vec.intX(), vec.intY(), vec.intZ(), radius);
    }

    public Village(World world, int x, int y, int z, int radius)
    {
        this();
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.radius = radius;
    }

    /** Sets display name of the village */
    public void setName(String name)
    {
        this.name = name;
    }

    /** Display name of the village */
    public String getName()
    {
        return this.name;
    }

    public static void loadVillageFromSave(NBTTagCompound tag)
    {
        Village village = new Village();
        village.load(tag);
    }

    @Override
    public void save(NBTTagCompound nbt)
    {
        super.save(nbt);
    }

    @Override
    public void load(NBTTagCompound nbt)
    {
        this.load(nbt);
    }

    @Override
    public File getSaveFile()
    {
        return this.saveFile;
    }

    @Override
    public void setSaveFile(File file)
    {
    }
}
