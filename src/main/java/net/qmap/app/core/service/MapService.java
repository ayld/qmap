package net.qmap.app.core.service;

import java.util.List;

import net.qmap.app.core.dto.MapEventDto;
import net.qmap.app.core.enums.EventPeriod;

public interface MapService {
	
	public List<MapEventDto> loadEventsByPeriodAndMagnitude(EventPeriod period, double minMagnitude);
	
	public MapEventDto read(Integer id);
}
