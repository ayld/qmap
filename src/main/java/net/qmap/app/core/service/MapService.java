package net.qmap.app.core.service;

import java.util.List;
import java.util.Set;

import net.qmap.app.core.dto.MapEventDto;
import net.qmap.app.core.enums.EventPeriod;

public interface MapService {

	public List<MapEventDto> loadEventsForPeriod(EventPeriod period);
	
	public List<MapEventDto> loadEventsByPeriodAndMagnitude(EventPeriod period, double minMagnitude);
	
	public Set<MapEventDto> loadLastDayEvents();
	
	public Set<MapEventDto> loadLastHourEvents();
	
	public MapEventDto read(Integer id);
}
