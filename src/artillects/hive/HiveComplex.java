package artillects.hive;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;
import artillects.VectorWorld;
import artillects.entity.EntityFabricator;
import artillects.entity.IArtillect;
import artillects.hive.structure.Building;
import artillects.hive.structure.Structure;
import artillects.hive.zone.Zone;
import artillects.hive.zone.ZoneBuilding;

/** Hive village in other words. This represents a single location in the hive. Each hive complex has
 * a task and set of structure peaces.
 * 
 * @author Dark */
public class HiveComplex extends HiveGhost
{
    public VectorWorld location;
    protected String name;

    protected final List<Structure> peaces = new ArrayList<Structure>();
    public final List<Structure> damagedPeaces = new ArrayList<Structure>();

    protected ZoneBuilding buildZone;

    private HashSet<IArtillect> artillects = new HashSet<IArtillect>();

    private HashSet<IArtillect> awaitingOrders = new HashSet<IArtillect>();

    private TreeSet<Report> inboxReports = new TreeSet<Report>();

    public HashSet<Zone> zones = new HashSet<Zone>();
    public TileEntityHiveComplexCore core;

    public HiveComplex()
    {
        // TODO Auto-generated constructor stub
    }

    public HiveComplex(String name, VectorWorld location)
    {
        this.name = name;
        this.location = location;
        HiveComplexManager.instance().addHiveComplex(this);
    }

    public HiveComplex(TileEntityHiveComplexCore tileEntityHiveComplexCore)
    {
        this("HiveComplexTR" + System.currentTimeMillis(), new VectorWorld(tileEntityHiveComplexCore));
        this.updateTileLink(tileEntityHiveComplexCore);
    }

    public void updateTileLink(TileEntityHiveComplexCore core)
    {
        this.core = core;
    }

    /** Called by a drone or the hive itself to load a report into the system waiting to be processed
     * into a task */
    public void issueReport(Report report)
    {
        // TODO add validation system to reject reports
        this.inboxReports.add(report);
    }

    /** Called when a drone is created or activated. Then needs to be loaded into the hive collection */
    public void addDrone(IArtillect drone)
    {
        artillects.add(drone);
        drone.setOwner(this);
    }

    /** Called to remove a drone from the hive. Normal when it dies or gets unloaded by the world */
    public void removeDrone(IArtillect drone)
    {
        artillects.remove(drone);
    }

    public HashSet<IArtillect> getArtillects()
    {
        return this.artillects;
    }

    public void addZone(Zone zone)
    {
        this.zones.add(zone);
    }

    public void loadFabricatorDemo()
    {
        this.load3x3Room(this.location.clone(), 2);
        this.buildZone = new ZoneBuilding(this, 50);
        for (int i = 0; i < 1; i++)
        {
            EntityFabricator fab = new EntityFabricator(this.location.world);
            fab.setPosition(this.location.x + 0.5, this.location.y + 1 + (i * 0.5), this.location.z + 0.5);
            fab.setOwner(HiveComplexManager.instance());
            buildZone.assignDrone(fab);
            this.location.world.spawnEntityInWorld(fab);
        }
    }

    public void loadGeneralBuilding(boolean worldGen)
    {
        final int width = 3;
        final int height = 1;
        final int tunnelSpacing = 12;
        for (int floor = 0; floor <= height; floor++)
        {
            VectorWorld floorBase = this.location.clone().add(0, 8 * floor, 0);
            // Center room
            peaces.add(new Structure(this, Building.NODE, floorBase.clone()));
            if (floor == 0)
            {
                peaces.add(new Structure(this, Building.FLOOR, floorBase.clone()));
            }
            if (floor == 0)
            {
                this.load3x3Room(floorBase, 2);
            }
            else
            {
                this.load3x3Room(floorBase, 0);
            }

            if (floor != height)
            {
                // NorthTunnel
                this.loadTunnel(floorBase.clone().add(0, 0, -tunnelSpacing), ForgeDirection.NORTH, width);
                peaces.add(new Structure(this, Building.TUNNELC, floorBase.clone().add(0, 0, -tunnelSpacing - (6 * width))));
                this.loadTunnel(floorBase.clone().add(-6, 0, -tunnelSpacing - (6 * width)), ForgeDirection.WEST, width - 1);
                this.loadTunnel(floorBase.clone().add(+6, 0, -tunnelSpacing - (6 * width)), ForgeDirection.EAST, width - 1);
                // SouthTunnel
                this.loadTunnel(floorBase.clone().add(0, 0, +tunnelSpacing), ForgeDirection.SOUTH, width);
                peaces.add(new Structure(this, Building.TUNNELC, floorBase.clone().add(0, 0, +tunnelSpacing + (6 * width))));
                this.loadTunnel(floorBase.clone().add(-6, 0, +tunnelSpacing + (6 * width)), ForgeDirection.WEST, width - 1);
                this.loadTunnel(floorBase.clone().add(+6, 0, +tunnelSpacing + (6 * width)), ForgeDirection.EAST, width - 1);
                // EastTunnel
                this.loadTunnel(floorBase.clone().add(+tunnelSpacing, 0, 0), ForgeDirection.EAST, width);
                peaces.add(new Structure(this, Building.TUNNELC, floorBase.clone().add(+tunnelSpacing + (6 * width), 0, 0)));
                this.loadTunnel(floorBase.clone().add(+tunnelSpacing + (6 * width), 0, -6), ForgeDirection.NORTH, width - 1);
                this.loadTunnel(floorBase.clone().add(+tunnelSpacing + (6 * width), 0, 6), ForgeDirection.SOUTH, width - 1);
                // WestTunnel
                this.loadTunnel(floorBase.clone().add(-tunnelSpacing, 0, 0), ForgeDirection.WEST, width);
                peaces.add(new Structure(this, Building.TUNNELC, floorBase.clone().add(-tunnelSpacing - (6 * width), 0, 0)));
                this.loadTunnel(floorBase.clone().add(-tunnelSpacing - (6 * width), 0, -6), ForgeDirection.NORTH, width - 1);
                this.loadTunnel(floorBase.clone().add(-tunnelSpacing - (6 * width), 0, 6), ForgeDirection.SOUTH, width - 1);
            }
        }
        this.buildZone = new ZoneBuilding(this, 80);

        if (worldGen)
        {
            for (Structure str : this.peaces)
            {
                str.worldGen();
            }
        }
        else
        {
            for (int i = 0; i < 4; i++)
            {
                EntityFabricator fab = new EntityFabricator(this.location.world);
                fab.setPosition(this.location.x + 0.5, this.location.y + (i * 0.5), this.location.z + 0.5);
                fab.setOwner(HiveComplexManager.instance());
                buildZone.assignDrone(fab);
                this.location.world.spawnEntityInWorld(fab);
            }
        }

    }

    public void load3x3Room(VectorWorld start, int center)
    {
        // Center
        if (center == 0)
        {
            peaces.add(new Structure(this, Building.TUNNELC, start.clone().add(0, 0, 0)));
        }
        else if (center == 1)
        {
            peaces.add(new Structure(this, Building.SKYLIGHT, start.clone().add(0, +7, 0)));
            peaces.add(new Structure(this, Building.NODE, start.clone().add(0, 0, 0)));
            peaces.add(new Structure(this, Building.TELEFLOOR, start.clone().add(0, 0, 0)));
        }
        else if (center == 2)
        {
            peaces.add(new Structure(this, Building.SKYLIGHT, start.clone().add(0, +7, 0)));
            peaces.add(new Structure(this, Building.NODE, start.clone().add(0, 0, 0)));
            peaces.add(new Structure(this, Building.COREFLOOR, start.clone().add(0, 0, 0)).worldGen());
        }
        //South
        peaces.add(new Structure(this, Building.TUNNELC, start.clone().add(0, 0, 6)));
        //North
        peaces.add(new Structure(this, Building.TUNNELC, start.clone().add(0, 0, -6)));
        //East
        peaces.add(new Structure(this, Building.TUNNELC, start.clone().add(6, 0, 0)));
        //West
        peaces.add(new Structure(this, Building.TUNNELC, start.clone().add(-6, 0, 0)));
        //Corners
        peaces.add(new Structure(this, Building.TUNNELC, start.clone().add(6, 0, 6)));
        peaces.add(new Structure(this, Building.TUNNELC, start.clone().add(-6, 0, -6)));
        peaces.add(new Structure(this, Building.TUNNELC, start.clone().add(-6, 0, 6)));
        peaces.add(new Structure(this, Building.TUNNELC, start.clone().add(6, 0, -6)));
        //Walls
        peaces.add(new Structure(this, Building.WALLX, start.clone().add(10, 1, 6)));
        peaces.add(new Structure(this, Building.WALLZ, start.clone().add(6, 1, 10)));
        peaces.add(new Structure(this, Building.WALLX, start.clone().add(-10, 1, 6)));
        peaces.add(new Structure(this, Building.WALLZ, start.clone().add(-6, 1, 10)));
        peaces.add(new Structure(this, Building.WALLX, start.clone().add(10, 1, -6)));
        peaces.add(new Structure(this, Building.WALLZ, start.clone().add(6, 1, -10)));
        peaces.add(new Structure(this, Building.WALLX, start.clone().add(-10, 1, -6)));
        peaces.add(new Structure(this, Building.WALLZ, start.clone().add(-6, 1, -10)));

    }

    public void load5x5Room(VectorWorld start, int center, boolean worldGen)
    {
        // Center
        if (center == 0)
        {
            peaces.add(new Structure(this, Building.TUNNELC, start.clone().add(0, 0, 0)));
        }
        else if (center == 1)
        {
            peaces.add(new Structure(this, Building.SKYLIGHT, start.clone().add(0, +7, 0)));
            peaces.add(new Structure(this, Building.NODE, start.clone().add(0, 0, 0)));
            peaces.add(new Structure(this, Building.TELEFLOOR, start.clone().add(0, 0, 0)));
        }
        else if (center == 2)
        {
            peaces.add(new Structure(this, Building.SKYLIGHT, start.clone().add(0, +7, 0)));
            peaces.add(new Structure(this, Building.NODE, start.clone().add(0, 0, 0)));
            peaces.add(new Structure(this, Building.COREFLOOR, start.clone().add(0, 0, 0)).worldGen());
        }
        //South
        peaces.add(new Structure(this, Building.TUNNELC, start.clone().add(0, 0, 6)));
        peaces.add(new Structure(this, Building.TUNNELC, start.clone().add(0, 0, 12)));
        peaces.add(new Structure(this, Building.TUNNELC, start.clone().add(-6, 0, 12)));
        peaces.add(new Structure(this, Building.TUNNELC, start.clone().add(+6, 0, 12)));
        //North
        peaces.add(new Structure(this, Building.TUNNELC, start.clone().add(0, 0, -6)));
        //East
        peaces.add(new Structure(this, Building.TUNNELC, start.clone().add(6, 0, 0)));
        //West
        peaces.add(new Structure(this, Building.TUNNELC, start.clone().add(-6, 0, 0)));
        //Corners
        peaces.add(new Structure(this, Building.TUNNELC, start.clone().add(6, 0, 6)));
        peaces.add(new Structure(this, Building.TUNNELC, start.clone().add(-6, 0, -6)));
        peaces.add(new Structure(this, Building.TUNNELC, start.clone().add(-6, 0, 6)));
        peaces.add(new Structure(this, Building.TUNNELC, start.clone().add(6, 0, -6)));
        //Walls
        peaces.add(new Structure(this, Building.WALLX, start.clone().add(10, 1, 6)));
        peaces.add(new Structure(this, Building.WALLZ, start.clone().add(6, 1, 10)));
        peaces.add(new Structure(this, Building.WALLX, start.clone().add(-10, 1, 6)));
        peaces.add(new Structure(this, Building.WALLZ, start.clone().add(-6, 1, 10)));
        peaces.add(new Structure(this, Building.WALLX, start.clone().add(10, 1, -6)));
        peaces.add(new Structure(this, Building.WALLZ, start.clone().add(6, 1, -10)));
        peaces.add(new Structure(this, Building.WALLX, start.clone().add(-10, 1, -6)));
        peaces.add(new Structure(this, Building.WALLZ, start.clone().add(-6, 1, -10)));

        if (worldGen)
        {
            for (Structure str : this.peaces)
            {
                str.worldGen();
            }
        }

    }

    public void loadTunnel(VectorWorld start, ForgeDirection direction, int tunnelPeaces)
    {
        for (int i = 0; i < tunnelPeaces; i++)
        {
            VectorWorld spot = start.clone().add(direction.offsetX * (6 * i), direction.offsetY * (6 * i), direction.offsetZ * (6 * i));
            if (direction == ForgeDirection.NORTH || direction == ForgeDirection.SOUTH)
            {
                peaces.add(new Structure(this, Building.TUNNELZ, spot));
            }
            else if (direction == ForgeDirection.EAST || direction == ForgeDirection.WEST)
            {
                peaces.add(new Structure(this, Building.TUNNELX, spot));
            }
            else if (direction == ForgeDirection.UP || direction == ForgeDirection.DOWN)
            {
                peaces.add(new Structure(this, Building.NODE, spot));
                peaces.add(new Structure(this, Building.WALLX, spot.clone().add(3, 1, 0)));
                peaces.add(new Structure(this, Building.WALLZ, spot.clone().add(-3, 1, 0)));
                peaces.add(new Structure(this, Building.WALLX, spot.clone().add(0, 1, -3)));
                peaces.add(new Structure(this, Building.WALLZ, spot.clone().add(0, 1, 3)));
            }
        }
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();
        if (this.ticks % 20 == 0)
        {
            Iterator<Structure> it = peaces.iterator();
            while (it.hasNext())
            {
                Structure str = it.next();
                try
                {
                    if (str.isValid())
                    {
                        str.updateEntity();
                        if (str.isDamaged() && !this.damagedPeaces.contains(str))
                        {
                            this.damagedPeaces.add(str);
                        }
                    }
                    else
                    {
                        str.invalidate();
                        it.remove();
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            Iterator<Zone> itZone = zones.iterator();
            while (itZone.hasNext())
            {
                Zone zone = itZone.next();
                if (zone.isValid())
                {
                    zone.updateEntity();
                    if (zone.doesZoneNeedWorkers())
                    {
                        Iterator<IArtillect> droneIterator = this.awaitingOrders.iterator();
                        while (droneIterator.hasNext())
                        {
                            IArtillect drone = droneIterator.next();
                            if (zone.canAssignDrone(drone))
                            {
                                zone.assignDrone(drone);
                                if (drone.getZone() == zone)
                                {
                                    droneIterator.remove();
                                }
                            }
                            if (!zone.doesZoneNeedWorkers())
                            {
                                break;
                            }
                        }
                    }
                }
                else
                {
                    zone.invalidate();
                    itZone.remove();
                }
            }
            Iterator<IArtillect> droneIt = artillects.iterator();
            while (droneIt.hasNext())
            {
                IArtillect drone = droneIt.next();
                if (drone.getOwner() != this)
                {
                    droneIt.remove();
                }
                else
                {
                    if (!zones.contains(drone.getZone()))
                    {
                        drone.setZone(null);
                    }
                    if (drone.getZone() == null)
                    {
                        if (!this.awaitingOrders.contains(drone))
                            this.awaitingOrders.add(drone);
                    }
                }
            }
        }

    }

    @Override
    public boolean isValid()
    {
        return super.isValid() && location != null && peaces != null && !peaces.isEmpty();
    }

    @Override
    public void invalidate()
    {
        super.invalidate();
        peaces.clear();
    }

    @Override
    public void save(NBTTagCompound nbt)
    {
        nbt.setCompoundTag("location", this.location.save(new NBTTagCompound()));
        nbt.setString("name", this.name);
    }

    @Override
    public void load(NBTTagCompound nbt)
    {
        this.location = new VectorWorld(nbt.getCompoundTag("location"));
        this.loadGeneralBuilding(false);
        this.name = nbt.getName();
    }
}
