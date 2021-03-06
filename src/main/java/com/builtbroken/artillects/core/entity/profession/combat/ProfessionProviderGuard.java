package com.builtbroken.artillects.core.entity.profession.combat;

import com.builtbroken.artillects.core.entity.npc.EntityNpc;
import com.builtbroken.artillects.core.entity.profession.ProfessionProvider;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/5/2016.
 */
public class ProfessionProviderGuard extends ProfessionProvider<ProfessionGuard, EntityNpc>
{
    @Override
    public ProfessionGuard newProfession(EntityNpc entity)
    {
        return new ProfessionGuard(entity);
    }

    @Override
    public ProfessionGuard loadFromSave(EntityNpc entity, NBTTagCompound compound)
    {
        ProfessionGuard guard = newProfession(entity);
        guard.load(compound);
        return guard;
    }
}
