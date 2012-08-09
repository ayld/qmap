package net.qmap.app.core.dao;

import java.util.Date;
import java.util.List;

import net.qmap.app.core.domain.MapEvent;
import net.qmap.app.core.enums.EventPeriod;

public interface MapEventDao {
	
	public void create(MapEvent event);
	
	public MapEvent read(Integer id);
	
	public List<MapEvent> getEventsForPeriod(EventPeriod period);
	
	public List<MapEvent> getEventsByPeriodAndMagnitude(EventPeriod period, double minMagnitude);
	
	public List<MapEvent> getLastDayEvents();
	
	public List<MapEvent> getLastHourEvents();
	
	public Date getLastEventPublishedDate();
}
