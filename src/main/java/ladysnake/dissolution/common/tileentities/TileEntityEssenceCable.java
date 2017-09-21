package ladysnake.dissolution.common.tileentities;

import java.lang.ref.WeakReference;

import ladysnake.dissolution.api.IEssentiaHandler;
import ladysnake.dissolution.common.capabilities.CapabilityEssentiaHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;

public class TileEntityEssenceCable extends TileEntity implements ITickable {
	
	private IEssentiaHandler essentiaHandler;
	private int transferCooldown;
	
	public TileEntityEssenceCable() {
		this.essentiaHandler = new CapabilityEssentiaHandler.DefaultEssentiaHandler(1);
	}

	@Override
	public void update() {
		if(!world.isRemote && transferCooldown++ % 20 == 0) {
			IEssentiaHandler strongestSuction = this.findStrongestSuction();
			if(strongestSuction != null) {
				if(strongestSuction.getSuction() > this.essentiaHandler.getSuction())
					this.essentiaHandler.setSuction(strongestSuction.getSuction() - 1, this.essentiaHandler.getSuctionType());
				this.essentiaHandler.flow(strongestSuction);
				this.markDirty();
			}
		}
	}
	
	private IEssentiaHandler findStrongestSuction() {
		IEssentiaHandler strongestSuction = null;
		for (EnumFacing facing : EnumFacing.values()) {
			TileEntity te = world.getTileEntity(pos.offset(facing));
			if(te != null && te.hasCapability(CapabilityEssentiaHandler.CAPABILITY_ESSENTIA, facing.getOpposite())) {
				IEssentiaHandler handler = te.getCapability(CapabilityEssentiaHandler.CAPABILITY_ESSENTIA, facing.getOpposite());
				if(handler != null && (strongestSuction == null || strongestSuction.getSuction() < handler.getSuction())) {
					strongestSuction = handler;
				}
			}
		}
		
		return strongestSuction;
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == CapabilityEssentiaHandler.CAPABILITY_ESSENTIA || super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if(capability == CapabilityEssentiaHandler.CAPABILITY_ESSENTIA)
			return CapabilityEssentiaHandler.CAPABILITY_ESSENTIA.cast(essentiaHandler);
		return super.getCapability(capability, facing);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound = super.writeToNBT(compound);
		compound.setTag("essentiaHandler", CapabilityEssentiaHandler.CAPABILITY_ESSENTIA.getStorage().writeNBT(CapabilityEssentiaHandler.CAPABILITY_ESSENTIA, this.essentiaHandler, null));
		compound.setInteger("cooldown", transferCooldown);
		return compound;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		try {
			CapabilityEssentiaHandler.CAPABILITY_ESSENTIA.getStorage().readNBT(CapabilityEssentiaHandler.CAPABILITY_ESSENTIA, essentiaHandler, null, compound.getCompoundTag("essentiaHandler"));
			this.transferCooldown = compound.getInteger("cooldown");
		} catch (NullPointerException e) {}
	}

}
