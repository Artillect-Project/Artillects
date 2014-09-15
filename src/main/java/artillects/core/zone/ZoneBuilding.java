package artillects.core.zone;


import net.minecraft.item.ItemStack;
import resonant.lib.type.Pair;
import artillects.core.building.BuildingPart;
import artillects.drone.HiveComplex;
import universalelectricity.core.transform.vector.Vector3;
import universalelectricity.core.transform.vector.VectorWorld;

import java.util.HashMap;
import java.util.Map;

/** Zone designed for drone to build a construct
 * 
 * @author Darkguardsman */
public class ZoneBuilding extends Zone
{
    HiveComplex complex;

    public HashMap<Vector3, Pair<ItemStack, BuildingPart>> buildPosition = new HashMap<Vector3, Pair<ItemStack, BuildingPart>>();

    public ZoneBuilding(HiveComplex complex, Vector3 start, Vector3 end)
    {
        super(complex.location.world(), start, end);
        this.complex = complex;
    }

    public ZoneBuilding(HiveComplex hiveComplex, int i)
    {
        this(hiveComplex, hiveComplex.location.clone().add(i), hiveComplex.location.clone().add(-i));
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();
        if (ticks % 20 == 0 && complex != null)
        {
            this.buildPosition.clear();
            for (BuildingPart str : complex.damagedPeaces)
            {
                str.loadBuildingRequest(this.buildPosition);
            }
        }
    }

    public Pair<Vector3, ItemStack> getClosestBlock(VectorWorld vec)
    {
        if (!buildPosition.isEmpty() && vec.world() == this.world)
        {
            Vector3 location = null;
            ItemStack stack = null;

            for (Map.Entry<Vector3, Pair<ItemStack, BuildingPart>> entry : buildPosition.entrySet())
            {
                if (location == null || entry.getKey().distance(vec) < vec.distance(location))
                {
                    location = entry.getKey();
                    stack = entry.getValue().left();
                }
            }
            return new Pair<Vector3, ItemStack>(location, stack);
        }
        return null;
    }

    /** Called by a builder to place a block.
     * 
     * @param location - location in the zone's world
     * @param stack - item stack, stack size is ignored
     * @return true if the block was placed */
    public boolean placeBlock(Vector3 location, ItemStack stack)
    {
        if (this.buildPosition.get(location) != null)
        {
            if (this.buildPosition.get(location).right() != null)
            {
                if (this.buildPosition.get(location).right().addBlock(location, stack))
                {
                    this.buildPosition.remove(location);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean doesZoneNeedWorkers()
    {
        return assignedArtillects.size() < 3 && !this.buildPosition.isEmpty();
    }
}