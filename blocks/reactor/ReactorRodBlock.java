package blocks.reactor;

public class ReactorRodBlock extends BaseReactorBlock {
	public static final ReactorRodBlock REACTOR_ROD_WITHOUT_FUEL = new ReactorRodBlock(1, 1, false);
	public static final ReactorRodBlock REACTOR_DOUBLE_ROD_WITHOUT_FUEL = new ReactorRodBlock(2, 4, false);
	public static final ReactorRodBlock REACTOR_QUAD_ROD_WITHOUT_FUEL = new ReactorRodBlock(4, 12, false);
	public static final ReactorRodBlock REACTOR_ROD_WITH_FUEL = new ReactorRodBlock(1, 1);
	public static final ReactorRodBlock REACTOR_DOUBLE_ROD_WITH_FUEL = new ReactorRodBlock(2, 4);
	public static final ReactorRodBlock REACTOR_QUAD_ROD_WITH_FUEL = new ReactorRodBlock(4, 12);
	public static final ReactorRodBlock REACTOR_ROD = REACTOR_ROD_WITH_FUEL;
	public static final ReactorRodBlock REACTOR_DOUBLE_ROD = REACTOR_DOUBLE_ROD_WITH_FUEL;
	public static final ReactorRodBlock REACTOR_QUAD_ROD = REACTOR_QUAD_ROD_WITH_FUEL;


	private final int rodCount;
	private final int internalPulseCount;
	private final boolean hasFuel;

	public ReactorRodBlock(int rodCount, int internalPulseCount) {
		super();
		this.rodCount = rodCount;
		this.internalPulseCount = internalPulseCount;
		this.hasFuel = true;

	}

	public ReactorRodBlock(int rodCount, int internalPulseCount, boolean hasFuel) {
		super();
		this.rodCount = rodCount;
		this.internalPulseCount = internalPulseCount;
		this.hasFuel = hasFuel;
	}

	public boolean isHasFuel() {
		return hasFuel;
	}


	public int getRodCount() {
		return rodCount;
	}

	public int getInternalPulseCount() {
		return internalPulseCount;
	}

}
