package com.builtbroken.artillects.core.entity;

import net.minecraft.inventory.IInventory;
import net.minecraft.world.World;

/**
 * Extends by all entities that are human in nature
 */
public abstract class EntityHumanoid<I extends IInventory> extends EntityArtillect<I>
{
    public EntityHumanoid(World world)
    {
        super(world);
    }

    @Override
    public void onLivingUpdate()
    {
        this.updateArmSwingProgress();
    }

    @Override
    protected void onDeathUpdate()
    {
        super.onDeathUpdate();
        //TODO don't despawn body, leave it in order to do other things
        //TODO or spawn in a dead body entity/block
    }
}
