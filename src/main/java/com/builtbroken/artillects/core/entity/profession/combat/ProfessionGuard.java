package com.builtbroken.artillects.core.entity.profession.combat;

import com.builtbroken.artillects.core.entity.ai.npc.combat.NpcTaskGuardArea;
import com.builtbroken.artillects.core.entity.npc.EntityNpc;
import com.builtbroken.mc.imp.transform.vector.Pos;
import net.minecraft.nbt.NBTTagCompound;

/**
 * NPC profession geared towards protected areas, not just fighting.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/5/2016.
 */
public class ProfessionGuard extends ProfessionCombat
{
    /** Should we stay near our protection zone */
    public boolean stayNearZone = true;
    public double zoneDistance = 10;

    /** TEMP var will be replaced with guard task, used to track center of guard position */
    public Pos centerOfGuardZone;

    public ProfessionGuard(EntityNpc entity)
    {
        super(entity);
        entity.tasks.add(2, new NpcTaskGuardArea(entity, zoneDistance)); //TODO add getter so we can update the distance easier
    }

    @Override
    public boolean hasActiveTask()
    {
        //TODO replace with zone, and zone task
        return centerOfGuardZone != null;
    }

    @Override
    public void loadAITasks()
    {
        super.loadAITasks();
    }

    @Override
    public void unloadAITask()
    {
        super.unloadAITask();
    }

    @Override
    public void load(NBTTagCompound nbt)
    {
        super.load(nbt);
        if(nbt.hasKey("stayNearZone"))
        {
            stayNearZone = nbt.getBoolean("stayNearZone");
        }
        if(nbt.hasKey("zoneDistance"))
        {
            zoneDistance = nbt.getDouble("zoneDistance");
        }
        if(nbt.hasKey("centerOfGuardZone"))
        {
            centerOfGuardZone = new Pos(nbt.getCompoundTag("centerOfGuardZone"));
        }
    }

    @Override
    public NBTTagCompound save(NBTTagCompound nbt)
    {
        super.save(nbt);
        nbt.setBoolean("stayNearZone", stayNearZone);
        nbt.setDouble("zoneDistance", zoneDistance);
        if(centerOfGuardZone != null)
        {
            nbt.setTag("centerOfGuardZone", centerOfGuardZone.toNBT());
        }
        return nbt;
    }
}
