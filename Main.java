import block.AirBlock;
import block.Block;
import blocks.reactor.ReactorAbsorberBlock;
import blocks.reactor.ReactorHeatPipeBlock;
import blocks.reactor.ReactorReflectorBlock;
import blocks.reactor.ReactorRodBlock;
import entity.reactor.ReactorControllerBlockEntity;

public class Main {

	private static ReactorControllerBlockEntity getBlock() {
		var blocks = new Block[][]{
				{
						ReactorHeatPipeBlock.INSTANCE,
						ReactorHeatPipeBlock.INSTANCE,
						ReactorReflectorBlock.INSTANCE,
						ReactorHeatPipeBlock.INSTANCE,
						ReactorHeatPipeBlock.INSTANCE
				},
				{
						ReactorAbsorberBlock.INSTANCE,
						ReactorHeatPipeBlock.INSTANCE,
						ReactorRodBlock.REACTOR_QUAD_ROD,
						ReactorHeatPipeBlock.INSTANCE,
						ReactorAbsorberBlock.INSTANCE
				},
				{
						ReactorHeatPipeBlock.INSTANCE,
						ReactorHeatPipeBlock.INSTANCE,
						ReactorHeatPipeBlock.INSTANCE,
						ReactorHeatPipeBlock.INSTANCE,
						ReactorHeatPipeBlock.INSTANCE
				},
				{
						AirBlock.INSTANCE,
						ReactorAbsorberBlock.INSTANCE,
						ReactorHeatPipeBlock.INSTANCE,
						ReactorAbsorberBlock.INSTANCE,
						AirBlock.INSTANCE
				}
		};
		return new ReactorControllerBlockEntity(blocks);
	}


	public static void main(String[] args) {
		var controller = getBlock();
		var totalSimulation = 100;

		System.out.println("Tick, MaxStoredHeat, MaxHeatChanged, ProducedEnergy");
		for(int i = 0; i < totalSimulation; i++) {
			var data = controller.tick();
			var energy = data.getRight();
			var maxStoredHeat = data.getLeft().stream().map(
					each -> each.getRight().storedHeat()
			).max(Long::compare).orElse(-1);
			var maxHeatChanged = data.getLeft().stream().map(
					each -> each.getRight().heatChanged()
			).max(Long::compare).orElse((short) -1);
			System.out.printf("%d, %d, %d, %d%n", i, maxStoredHeat, maxHeatChanged, energy);
		}
	}

}
