package org.demo.write.eventstore;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.demo.write.aggregate.Event;
import org.hibernate.annotations.CreationTimestamp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import static jakarta.persistence.TemporalType.TIMESTAMP;

/**
 * The {@code PersistentEvent} class represents an entity that holds the serialized version of an event.
 *
 * @author mmroczkowski
 */
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PersistentEvent {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"));
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private UUID aggregateId;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String payload;

    @Column(nullable = false)
    @CreationTimestamp
    @Temporal(TIMESTAMP)
    private Date createDate;

    /**
     * Serializes an {@link Event} into a {@link PersistentEvent}.
     *
     * @param event The event to be serialized.
     * @return A {@link PersistentEvent} representation of the input event.
     */
    public static PersistentEvent serialize(Event event) {
        PersistentEvent persistentEvent = new PersistentEvent();
        persistentEvent.aggregateId = event.getAggregateId();
        persistentEvent.type = event.getClass().getName();
        try {
            persistentEvent.payload = MAPPER.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Unexpected error during serialization of event:" + event, e);
        }
        return persistentEvent;
    }

    /**
     * Deserializes a {@link PersistentEvent} into an {@link Event}.
     *
     * @param persistentEvent The persistent event to be deserialized.
     * @return An {@link Event} representation of the input persistent event.
     */
    public static Event deserialize(PersistentEvent persistentEvent) {
        try {
            return (Event) MAPPER.readValue(persistentEvent.payload, Class.forName(persistentEvent.getType()));
        } catch (JsonProcessingException | ClassNotFoundException e) {
            throw new RuntimeException("Unexpected error during deserialization of event:" + persistentEvent, e);
        }
    }
}
