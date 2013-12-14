// Date: 12/13/2013 12:46:35 PM
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX

package artillects.client.model;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelCombatWalker extends ModelDrone
{
    //fields
    ModelRenderer Body;
    ModelRenderer rightUpperLeg;
    ModelRenderer rightLowwerLeg;
    ModelRenderer rightFoot;
    ModelRenderer leftLowwerLeg;
    ModelRenderer leftFoot;
    ModelRenderer leftUpperLeg;
    ModelRenderer face;

    public ModelCombatWalker()
    {
        textureWidth = 128;
        textureHeight = 128;

        Body = new ModelRenderer(this, 0, 0);
        Body.addBox(-4.5F, -3F, -4.5F, 9, 9, 9);
        Body.setRotationPoint(0F, 8F, 0F);
        Body.setTextureSize(128, 128);
        Body.mirror = true;
        setRotation(Body, 0F, 0F, 0F);
        rightUpperLeg = new ModelRenderer(this, 0, 0);
        rightUpperLeg.addBox(-3.5F, -1.5F, -2.5F, 3, 9, 5);
        rightUpperLeg.setRotationPoint(-4F, 9F, 0F);
        rightUpperLeg.setTextureSize(128, 128);
        rightUpperLeg.mirror = true;
        setRotation(rightUpperLeg, -0.3490659F, 0F, 0F);
        rightLowwerLeg = new ModelRenderer(this, 0, 0);
        rightLowwerLeg.addBox(0.5F, -1.5F, -2.5F, 3, 9, 5);
        rightLowwerLeg.setRotationPoint(4F, 14F, -2F);
        rightLowwerLeg.setTextureSize(128, 128);
        rightLowwerLeg.mirror = true;
        setRotation(rightLowwerLeg, 0.4712389F, 0F, 0F);
        rightFoot = new ModelRenderer(this, 0, 0);
        rightFoot.addBox(-4.5F, -1.5F, -2.5F, 5, 5, 10);
        rightFoot.setRotationPoint(-4F, 21F, -2F);
        rightFoot.setTextureSize(128, 128);
        rightFoot.mirror = true;
        setRotation(rightFoot, 0F, 0F, 0F);
        leftLowwerLeg = new ModelRenderer(this, 0, 0);
        leftLowwerLeg.addBox(-3.5F, -1.5F, -2.5F, 3, 9, 5);
        leftLowwerLeg.setRotationPoint(-4F, 14F, -2F);
        leftLowwerLeg.setTextureSize(128, 128);
        leftLowwerLeg.mirror = true;
        setRotation(leftLowwerLeg, 0.4712389F, 0F, 0F);
        leftFoot = new ModelRenderer(this, 0, 0);
        leftFoot.addBox(-0.5F, -1.5F, -2.5F, 5, 5, 10);
        leftFoot.setRotationPoint(4F, 21F, -2F);
        leftFoot.setTextureSize(128, 128);
        leftFoot.mirror = true;
        setRotation(leftFoot, 0F, 0F, 0F);
        leftUpperLeg = new ModelRenderer(this, 0, 0);
        leftUpperLeg.addBox(0.5F, -1.5F, -2.5F, 3, 9, 5);
        leftUpperLeg.setRotationPoint(4F, 9F, 0F);
        leftUpperLeg.setTextureSize(128, 128);
        leftUpperLeg.mirror = true;
        setRotation(leftUpperLeg, -0.3490659F, 0F, 0F);
        face = new ModelRenderer(this, 0, 0);
        face.addBox(-3.5F, -3F, -1.5F, 7, 7, 4);
        face.setRotationPoint(0F, 9F, -5F);
        face.setTextureSize(128, 128);
        face.mirror = true;
        setRotation(face, 0F, 0F, 0F);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z)
    {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    @Override
    public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7)
    {
        this.setRotationAngles(par2, par3, par4, par5, par6, par7, par1Entity);
        this.render(par7);
    }

    @Override
    public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity entity)
    {
        //Walking
        this.rightUpperLeg.rotateAngleX = MathHelper.cos((par1 - 20) * 0.6662F) * 1.4F * par2;
        this.leftUpperLeg.rotateAngleX = MathHelper.cos((par1 - 20) * 0.6662F + (float) Math.PI) * 1.4F * par2;
        this.rightUpperLeg.rotateAngleY = 0.0F;
        this.leftUpperLeg.rotateAngleY = 0.0F;
        float rx = (MathHelper.cos((float) (this.rightUpperLeg.rotateAngleX * 0.0174532925)) * (6) - MathHelper.sin((float) (this.rightUpperLeg.rotateAngleX * 0.0174532925)));
        float ry = (MathHelper.sin((float) (this.rightUpperLeg.rotateAngleX * 0.0174532925)) * (6) + MathHelper.cos((float) (this.rightUpperLeg.rotateAngleX * 0.0174532925)));
        float lx = (MathHelper.cos((float) (this.leftUpperLeg.rotateAngleX * 0.0174532925)) * (6) - MathHelper.sin((float) (this.leftUpperLeg.rotateAngleX * 0.0174532925)));
        float ly = (MathHelper.sin((float) (this.leftUpperLeg.rotateAngleX * 0.0174532925)) * (6) + MathHelper.cos((float) (this.leftUpperLeg.rotateAngleX * 0.0174532925)));
        leftLowwerLeg.setRotationPoint(-4F, 14F + ry, -2F + rx);
        rightLowwerLeg.setRotationPoint(4F, 14F + ly, -2F + lx);
        this.rightLowwerLeg.rotateAngleX = -MathHelper.cos(par1 * 0.6662F) * 1.4F * par2;
        this.leftLowwerLeg.rotateAngleX = -MathHelper.cos(par1 * 0.6662F + (float) Math.PI) * 1.4F * par2;
        this.rightLowwerLeg.rotateAngleY = 0.0F;
        this.leftLowwerLeg.rotateAngleY = 0.0F;
        //Pitch 
        this.Body.rotateAngleX = par5 / (180F / (float) Math.PI);
        this.face.rotateAngleX = par5 / (180F / (float) Math.PI);
    }

    @Override
    public void render(float f5)
    {
        Body.render(f5);
        rightUpperLeg.render(f5);
        rightLowwerLeg.render(f5);
        rightFoot.render(f5);
        leftLowwerLeg.render(f5);
        leftFoot.render(f5);
        leftUpperLeg.render(f5);
        face.render(f5);

    }

}