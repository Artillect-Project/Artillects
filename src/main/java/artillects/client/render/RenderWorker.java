package artillects.client.render;

import artillects.client.model.ModelArtillect;
import artillects.client.model.ModelWorker;
import artillects.core.Reference;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class RenderWorker extends RenderLiving
{
    public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.DOMAIN, Reference.MODEL_TEXTURE_DIRECTORY + "Worker.png");
    public static final ModelArtillect MODEL = new ModelWorker();

    public RenderWorker()
    {
        super(MODEL, 1.0F);
    }

    @Override
    protected float getDeathMaxRotation(EntityLivingBase entity)
    {
        return 180.0F;
    }

    /** Returns the location of an entity's texture. Doesn't seem to be called unless you call
     * Render.bindEntityTexture. */
    @Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return TEXTURE;
    }
}