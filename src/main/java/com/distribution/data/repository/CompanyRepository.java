package com.distribution.data.repository;

import com.datastax.driver.core.*;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.distribution.data.domain.Company;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Cassandra repository for the CompanyDTO entity.
 */
@Repository
public class CompanyRepository {

    @Inject
    private Session session;

    private Mapper<Company> mapper;

    private PreparedStatement findAllStmt;

    private PreparedStatement findOneByNameStmt;

    private PreparedStatement insertByNameStmt;

    private PreparedStatement deleteByNameStmt;

    private PreparedStatement truncateStmt;
    private PreparedStatement truncateByNameStmt;

    @PostConstruct
    public void init() {
        mapper = new MappingManager(session).mapper(Company.class);
        findAllStmt = session.prepare("SELECT * FROM company");
        findOneByNameStmt = session.prepare("SELECT id FROM company_by_name where name=:name");
        insertByNameStmt = session.prepare(
                "INSERT INTO company_by_name (name, id) " +
                    "VALUES (:name, :id)");
        deleteByNameStmt = session.prepare(
                "DELETE FROM company_by_name " +
                    "WHERE name = :name");
        truncateStmt = session.prepare("TRUNCATE company");
        truncateByNameStmt = session.prepare("TRUNCATE company_by_name");
    }

    public List<Company> findAll() {
        List<Company> companies = new ArrayList<>();
        BoundStatement stmt =  findAllStmt.bind();
        session.execute(stmt).all().stream().map(
            row -> {
                Company company = new Company();
                company.setId(row.getUUID("id"));
                company.setName(row.getString("name"));
                company.setAddress(row.getString("address"));
                company.setTelephone(row.getString("telephone"));
                company.setEmail(row.getString("email"));
                return company;
            }
        ).forEach(companies::add);
        return companies;
    }

    public List<com.distribution.modules.ws.Company> findAllForService() {
        List<com.distribution.modules.ws.Company> companies = new ArrayList<>();
        BoundStatement stmt =  findAllStmt.bind();
        session.execute(stmt).all().stream().map(
            row -> {
                com.distribution.modules.ws.Company company = new com.distribution.modules.ws.Company();
                company.setId(row.getUUID("id").toString());
                company.setName(row.getString("name"));
                company.setAddress(row.getString("address"));
                company.setTelephone(row.getString("telephone"));
                company.setEmail(row.getString("email"));
                return company;
            }
        ).forEach(companies::add);
        return companies;
    }

    public Company findOne(UUID id) {
        return mapper.get(id);
    }

    public Optional<Company> findOneByName(String name) {
    	BoundStatement stmt = findOneByNameStmt.bind();
        stmt.setString("name", name);
        return findOneFromIndex(stmt);
    }

    private Optional<Company> findOneFromIndex(BoundStatement stmt) {
        ResultSet rs = session.execute(stmt);
        if (rs.isExhausted()) {
            return Optional.empty();
        }
        return Optional.ofNullable(rs.one().getString("id"))
            .map(id -> Optional.ofNullable(mapper.get(id)))
            .get();
    }

    public Company save(Company company) {
        if (company.getId() == null) {
            company.setId(UUID.randomUUID());
        }else{
        	Company oldCom = mapper.get(company.getId());
        	if(oldCom != null){
        		session.execute(deleteByNameStmt.bind().setString("name", oldCom.getName()));
        	}
        }
//        mapper.save(company);
        BatchStatement batch = new BatchStatement();
        batch.add(mapper.saveQuery(company));
        batch.add(insertByNameStmt.bind()
                .setString("name", company.getName())
                .setUUID("id", company.getId()));
            session.execute(batch);
        return company;
    }

    public void delete(UUID id) {
    	BatchStatement batch = new BatchStatement();
    	Company com = findOne(id);
        batch.add(mapper.deleteQuery(com));
        batch.add(deleteByNameStmt.bind().setString("name", com.getName()));
        session.execute(batch);
    }

    public void deleteAll() {
    	BatchStatement batch = new BatchStatement();
        batch.add(truncateStmt.bind());
        batch.add(truncateByNameStmt.bind());
        session.execute(batch);
    }
}
