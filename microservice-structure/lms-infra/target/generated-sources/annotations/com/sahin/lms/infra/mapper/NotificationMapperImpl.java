package com.sahin.lms.infra.mapper;

import com.sahin.lms.infra.entity.redis.NotificationEntity;
import com.sahin.lms.infra.model.notification.Notification;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-06-27T15:17:23+0300",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 1.8.0_101 (Oracle Corporation)"
)
@Component
public class NotificationMapperImpl implements NotificationMapper {

    @Override
    public NotificationEntity toEntity(Map<String, String> map) {
        if ( map == null ) {
            return null;
        }

        NotificationEntity notificationEntity = new NotificationEntity();

        doMappings( map, notificationEntity );

        return notificationEntity;
    }

    @Override
    public Notification toModel(NotificationEntity entity) {
        if ( entity == null ) {
            return null;
        }

        Notification notification = new Notification();

        notification.setId( entity.getId() );
        notification.setAboutId( entity.getAboutId() );
        notification.setCardBarcode( entity.getCardBarcode() );
        notification.setMail( entity.getMail() );
        notification.setType( entity.getType() );
        notification.setTimeToSend( entity.getTimeToSend() );
        notification.setContent( entity.getContent() );

        return notification;
    }

    @Override
    public List<Notification> toModels(List<NotificationEntity> entities) {
        if ( entities == null ) {
            return null;
        }

        List<Notification> list = new ArrayList<Notification>( entities.size() );
        for ( NotificationEntity notificationEntity : entities ) {
            list.add( toModel( notificationEntity ) );
        }

        return list;
    }
}
