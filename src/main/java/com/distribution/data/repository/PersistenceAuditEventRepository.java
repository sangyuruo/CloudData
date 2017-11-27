package com.distribution.data.repository;

import com.datastax.driver.core.*;
import com.datastax.driver.core.utils.UUIDs;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.distribution.data.collector.type.DateUtils;
import com.distribution.data.domain.PersistentAuditEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for the PersistentAuditEvent entity.
 */
@Repository
public class PersistenceAuditEventRepository {
    @Inject
    private Session session;

    private Mapper<PersistentAuditEvent> mapper;
    private PreparedStatement findByPrincipalStmt;
    private PreparedStatement findOneByIdStmt;
    private PreparedStatement findByIdStmt;
    private PreparedStatement findByPrincipalAndIdStmt;
    private PreparedStatement findByPrincipalAndIdAndTypeStmt;
    private PreparedStatement findByIdRangStmt;

    @PostConstruct
    public void init() {
        mapper = new MappingManager(session).mapper(PersistentAuditEvent.class);
        findByPrincipalStmt = session.prepare("SELECT * FROM AppAudit where principal=:principal allow filtering");
        findOneByIdStmt = session.prepare("SELECT * FROM AppAudit where id=:id allow filtering");
        findByIdStmt = session.prepare("SELECT * FROM AppAudit where id>=:st allow filtering ");
        findByPrincipalAndIdStmt = session.prepare("SELECT * FROM AppAudit where id>=:st and principal=:principal allow filtering ");
        findByPrincipalAndIdAndTypeStmt = session.prepare("SELECT * FROM AppAudit where id>=:st and principal=:principal and event_type=:type allow filtering ");
        findByIdRangStmt = session.prepare("SELECT * FROM AppAudit where id>=:st and id<=:ed allow filtering ");

    }
    public List<PersistentAuditEvent> findByPrincipal(String principal){
        List<PersistentAuditEvent> list = new ArrayList<>();
        BoundStatement stmt =  findByPrincipalStmt.bind();
        stmt.setString("principal", principal);
        session.execute(stmt).all().stream().map(
            row -> {
                return getPersistentAuditEvent(row);
            }
        ).forEach(list::add);

        return list;
    }

    private PersistentAuditEvent getPersistentAuditEvent(Row row) {
       PersistentAuditEvent pa = new PersistentAuditEvent();
       pa.setId(row.getUUID("id"));
       pa.setPrincipal(row.getString("principal"));
       pa.setAuditEventDate(row.getTimestamp("event_date").toInstant().atZone(ZoneId.systemDefault()));
       pa.setAuditEventType(row.getString("event_type"));
       pa.setData(row.getMap("event_data", String.class, String.class));
       return pa;
    }

    public List<PersistentAuditEvent> findByAuditEventDateAfter(LocalDateTime after){
        List<PersistentAuditEvent> list = new ArrayList<>();
        BoundStatement stmt =  findByIdStmt.bind();
        stmt.setUUID("st", UUIDs.startOf(DateUtils.getTodayStart(after).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()));
        session.execute(stmt).all().stream().map(
            row -> {
                return getPersistentAuditEvent(row);
            }
        ).forEach(list::add);

        return list;
    }

    public List<PersistentAuditEvent> findByPrincipalAndAuditEventDateAfter(String principal, LocalDateTime after){
        List<PersistentAuditEvent> list = new ArrayList<>();
        BoundStatement stmt =  findByPrincipalAndIdStmt.bind();
        stmt.setUUID("st", UUIDs.startOf(DateUtils.getTodayStart(after).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()));
        stmt.setString("principal", principal);
        session.execute(stmt).all().stream().map(
            row -> {
                return getPersistentAuditEvent(row);
            }
        ).forEach(list::add);
        return list;
    }

    public List<PersistentAuditEvent> findByPrincipalAndAuditEventDateAfterAndAuditEventType(String principal, LocalDateTime after, String type){
        List<PersistentAuditEvent> list = new ArrayList<>();
        BoundStatement stmt =  findByPrincipalAndIdAndTypeStmt.bind();
        stmt.setUUID("st", UUIDs.startOf(DateUtils.getTodayStart(after).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()));
        stmt.setString("principal", principal);
        stmt.setString("type", type);
        session.execute(stmt).all().stream().map(
            row -> {
                return getPersistentAuditEvent(row);
            }
        ).forEach(list::add);
        return list;
    }

    public Page<PersistentAuditEvent> findAllByAuditEventDateBetween(LocalDateTime fromDate, LocalDateTime toDate, Pageable pageable){
        List<PersistentAuditEvent> list = new ArrayList<>();
        BoundStatement stmt =  findByIdRangStmt.bind();
        stmt.setUUID("st", UUIDs.startOf(DateUtils.getTodayStart(fromDate).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()));
        stmt.setUUID("ed", UUIDs.startOf(DateUtils.getTodayStart(toDate).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()));
        session.execute(stmt).all().stream().map(
            row -> {
                return getPersistentAuditEvent(row);
            }
        ).forEach(list::add);
        return new PageImpl<PersistentAuditEvent>(list);
    }

    public void save(PersistentAuditEvent persistentAuditEvent) {
        if(persistentAuditEvent.getId() == null){
            persistentAuditEvent.setId(UUIDs.timeBased());
        }
        mapper.save(persistentAuditEvent, Mapper.Option.ttl(604800));
    }

    /**
     * 故意返回空，数据量太大，无意义。
     * @return
     */
    public Iterable<PersistentAuditEvent> findAll() {
        return new ArrayList<>();
    }

    public Page<PersistentAuditEvent> findAll(Pageable pageable) {

        return null;
    }

    public Optional<PersistentAuditEvent> findOne(UUID id) {
        BoundStatement stmt = findOneByIdStmt.bind();
        stmt.setUUID("id", id);
        return findOneFromIndex(stmt);
    }

    private Optional<PersistentAuditEvent> findOneFromIndex(BoundStatement stmt) {
        ResultSet rs = session.execute(stmt);
        if (rs.isExhausted()) {
            return Optional.empty();
        }
        return  Optional.ofNullable(getPersistentAuditEvent(rs.one()));
    }
}
