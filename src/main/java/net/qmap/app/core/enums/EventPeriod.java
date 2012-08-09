package net.qmap.app.core.enums;

public enum EventPeriod {
	
	HOUR(1),
	TWO_HOURS(2),
	SIX_HOURS(3),
	TWELVE_HOURS(4),
	DAY(5);
	
	private final int id;

	private EventPeriod(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
	
	public static EventPeriod byId(int id) {
		for (EventPeriod per : values()) {
			
			if (per.id == id) {
				
				return per;
			}
		}
		throw new IllegalArgumentException("Unknown id: " + id);
	}
}
