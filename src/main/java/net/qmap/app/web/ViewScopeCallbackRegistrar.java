package net.qmap.app.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PostConstructViewMapEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.ViewMapListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

public class ViewScopeCallbackRegistrar implements ViewMapListener {
	
	private static final ConcurrentMap<String, List<Runnable>> destructionCallbacks = 
			new ConcurrentHashMap<String, List<Runnable>>();
	
	@SuppressWarnings("rawtypes")
	public void processEvent(SystemEvent event) throws AbortProcessingException {
		if(event instanceof PostConstructViewMapEvent) {
			
			final FacesContext fCtx = FacesContext.getCurrentInstance();
			final HttpSession session = (HttpSession) fCtx.getExternalContext().getSession(false);
			
			
			// CLEAR SESSION VIEW MAP
			if(((HttpServletRequest)fCtx.getExternalContext().getRequest()).getMethod().equalsIgnoreCase("GET")) {
				if (session.getAttribute("com.sun.faces.renderkit.ServerSideStateHelper.LogicalViewMap") != null) {
					for (Object entry : ((Map) session.getAttribute("com.sun.faces.renderkit.ServerSideStateHelper.LogicalViewMap")).entrySet()) {
						Entry currentEntry = (Entry)entry;
						
						if (currentEntry.getValue() instanceof Map) {
							
							for (Object subEntry : ((Map) currentEntry.getValue()).entrySet()) {
								Entry currentSubEntry = (Entry)subEntry;
								
								Object[] array = (Object[]) currentSubEntry.getValue();
								if (array != null) {
									for (Object arrayItem : array) {
										if (arrayItem != null && arrayItem instanceof Map) {
											((Map) arrayItem).clear();
										}
									}
								}
							}
						}
					}
				}
			}
			// END CLEAR SESSION VIEW MAP
			
			
			final String sessionId = session.getId();
		
			if (StringUtils.isNotEmpty(sessionId)) {
				executeDestructionCallbacksForSession(sessionId);
				clearDestructionCallbacksForSession(sessionId);
			}
		}
	}
	 
	public boolean isListenerForSource(Object source) {
		return source instanceof UIViewRoot;
	}
	
	public static void addDestructionCallBack(final String sessionId, final Runnable destructionCallback) {
		if (!destructionCallbacks.containsKey(sessionId)) {
			destructionCallbacks.put(sessionId, new ArrayList<Runnable>());
		}
		destructionCallbacks.get(sessionId).add(destructionCallback);
	}
	
	public static List<Runnable> getDestructionCallbacksForSession(final String sessionId) {
		return destructionCallbacks.get(sessionId);
	}
	
	public static void clearDestructionCallbacksForSession(final String sessionId) {
		if (destructionCallbacks.containsKey(sessionId)) {
			destructionCallbacks.get(sessionId).clear();
		}
	}
	
	public static String getCurrentSessionId() {
		final FacesContext fCtx = FacesContext.getCurrentInstance();
		final HttpSession session = (HttpSession) fCtx.getExternalContext().getSession(true);
		final String sessionId = session.getId();

		return sessionId;
	}
	
	public static void executeDestructionCallbacksForSession(final String sessionId) {
		final List<Runnable> currentViewDestructionCallbacks = getDestructionCallbacksForSession(sessionId);
		
		if (currentViewDestructionCallbacks != null) {
			for (final Runnable destructionCallback : currentViewDestructionCallbacks) {
				destructionCallback.run();
			}
		}
	}
}
