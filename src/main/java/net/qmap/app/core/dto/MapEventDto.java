package net.qmap.app.core.dto;

import java.math.BigDecimal;
import java.util.Date;

public class MapEventDto {

	private Integer id;
	private BigDecimal latitude;
	private BigDecimal longitude;
	private BigDecimal magnitude;
	private Date date;
	private Date publishDate;
	private String location;
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public BigDecimal getLatitude() {
		return latitude;
	}
	
	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}
	
	public BigDecimal getLongitude() {
		return longitude;
	}
	
	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}
	
	public BigDecimal getMagnitude() {
		return magnitude;
	}
	
	public void setMagnitude(BigDecimal magnitude) {
		this.magnitude = magnitude;
	}
}
