package com.distribution.data.collector.listener;

import com.codahale.metrics.annotation.Timed;
import com.datastax.driver.core.utils.UUIDs;
import com.distribution.data.collector.event.ServerStatusEvent;
import com.distribution.data.domain.ServerStatus;
import com.distribution.data.repository.ServerStatusRepository;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.GenericApplicationListener;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Component
public class ServerStatusAppenderListener implements GenericApplicationListener {

    @Inject
    private ServerStatusRepository serverStatusRepository;
//
//    @Inject
//    private ServerStatusMapper serverStatusMapper;

	@Override
	@Timed
	public void onApplicationEvent(ApplicationEvent event) {
		ServerStatus result= (ServerStatus)event.getSource();
		LocalDateTime dt = null;
		if(result != null){
            dt = result.getLastUpdate().toLocalDateTime();
		}else{
			dt = LocalDateTime.now();
		}
        if(result.getId() == null){
			UUID id = UUIDs.endOf(dt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
			result.setId(id);
		}
		serverStatusRepository.save(result);
	}

	@Override
	public int getOrder() {
		return 4;
	}

	@Override
	public boolean supportsEventType(ResolvableType eventType) {
		Class<?> type = eventType.getRawClass();
		return ServerStatusEvent.class.isAssignableFrom(type);
	}

	@Override
	public boolean supportsSourceType(Class<?> sourceType) {
		return true;
	}

}
