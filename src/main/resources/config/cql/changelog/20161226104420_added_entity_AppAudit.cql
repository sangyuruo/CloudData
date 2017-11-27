
CREATE TABLE IF NOT EXISTS AppAudit(
    id timeuuid,
    principal text,
    event_date timestamp,
    event_type text,
    event_data map<text, text>,
    PRIMARY KEY(principal, id, event_type)
)WITH CLUSTERING ORDER BY (id DESC);
