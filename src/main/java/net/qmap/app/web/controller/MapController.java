package net.qmap.app.web.controller;

import java.util.List;

import javax.faces.model.ListDataModel;

import net.qmap.app.core.dto.MapEventDto;
import net.qmap.app.core.enums.EventPeriod;
import net.qmap.app.core.service.MapService;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.SelectableDataModel;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.springframework.beans.factory.annotation.Required;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

public class MapController {

	private final MapModel mapModel = new DefaultMapModel();
	
	private List<MapEventDto> events = Lists.newArrayListWithCapacity(0);
	private QuakeTableSelectableDataModel selectableTableEvents = new QuakeTableSelectableDataModel();
	
	private String quakeInfo = "";
	private String mapCenter = "42.622433, 25.150236";
	
	private boolean showDetail = false;
	private boolean filtersDisabled = false;
	
	private int period = 1;
	private double desiredMagnitude = 0.0d;
	
	private MapEventDto selected = new MapEventDto();
	
	private transient MapService service;
	
	public MapModel getModel() {
		
		final List<MapEventDto> eventsForPeriod = service.loadEventsByPeriodAndMagnitude(EventPeriod.byId(period), desiredMagnitude);
		
		this.selectableTableEvents = new QuakeTableSelectableDataModel(Lists.newArrayList(eventsForPeriod));
		
		this.events = Lists.newArrayList(eventsForPeriod);
		for (MapEventDto event : events) {
			
			final double latitude = event.getLatitude().doubleValue();
			final double longitude = event.getLongitude().doubleValue();
			final String magnitude = event.getMagnitude().toString();
			
			final String title = "Id: " + event.getId() + ", Lat: " + latitude + ", Lng: " + longitude + ", Mag: " + magnitude; // YES ! I don't even care its slow !!!
			
			mapModel.addOverlay(new Marker(new LatLng(latitude, longitude), title));
		}
		return mapModel;
	}
	
	public void onTableRowSelect(SelectEvent selectEvent) {
		final MapEventDto event = (MapEventDto) selectEvent.getObject();
		
		this.mapCenter = event.getLatitude() + ", " + event.getLongitude();
		this.selected = event;
		this.filtersDisabled = true;
	}
	
	public void onTableRowUnselect(UnselectEvent unselectEvent) {
		this.filtersDisabled = false;
	}
	
	public void onMarkerSelect(OverlaySelectEvent overlayEvent) {  
        final Marker marker = (Marker) overlayEvent.getOverlay();
        final String title = marker.getTitle();
        final String id = title.split(",")[0].split(":")[1].trim(); // YES ! I don't even care its unreadable !!!
		
        this.selected = service.read(Integer.valueOf(id));
        
        this.showDetail = true;
    }  
	
	private static class QuakeTableSelectableDataModel extends ListDataModel<MapEventDto> implements SelectableDataModel<MapEventDto> {

		private List<MapEventDto> data = Lists.newArrayListWithCapacity(0);
		
		public QuakeTableSelectableDataModel() {
		}

		public QuakeTableSelectableDataModel(List<MapEventDto> data) {
			super(data);
			Preconditions.checkArgument(data != null);
			
			this.data = data;
		}

		@Override
		public MapEventDto getRowData(String key) {
			Preconditions.checkArgument(!Strings.isNullOrEmpty(key));
			
			for (MapEventDto event : this.data) {
				
				if (Integer.valueOf(key).equals(event.getId())) {
					
					return event;
				}
			}
			return null;
		}

		@Override
		public Object getRowKey(MapEventDto dto) {
			Preconditions.checkArgument(dto != null);
			Preconditions.checkArgument(dto.getId() != null && dto.getId() > 0);
			
			return dto.getId();
		}
	}
	
	public String getQuakeInfo() {
		return quakeInfo;
	}
	
	public boolean isFiltersDisabled() {
		return filtersDisabled;
	}

	public void setFiltersDisabled(boolean filtersDisabled) {
		this.filtersDisabled = filtersDisabled;
	}

	public double getDesiredMagnitude() {
		return desiredMagnitude;
	}

	public void setDesiredMagnitude(double desiredMagnitude) {
		this.desiredMagnitude = desiredMagnitude;
	}

	public boolean isShowDetail() {
		return showDetail;
	}
	
	public boolean getShowDetail() {
		return showDetail;
	}
	
	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public MapEventDto getDto() {
		return selected;
	}
	
	public String getMapCenter() {
		return mapCenter;
	}

	public QuakeTableSelectableDataModel getSelectableTableEvents() {
		return selectableTableEvents;
	}

	public List<MapEventDto> getEvents() {
		return events;
	}
	
	// IoC

	@Required
	public void setService(MapService service) {
		this.service = service;
	}
}
