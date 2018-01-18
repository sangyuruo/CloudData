
CREATE TABLE IF NOT EXISTS modbusServer (
    id uuid,
    companyId uuid,
    hostname text,
    code text,
    ip text,
    port int,
    enable boolean,
    model int,
    encapsulated boolean,
    keepAlive boolean,
    requestTimeout int,
    replyTimeout int,
    PRIMARY KEY(id, companyId)
)WITH CLUSTERING ORDER BY (companyId ASC);


CREATE TABLE IF NOT EXISTS modbusServer_by_hostname (
    hostname text,
    companyId uuid,
    id uuid,
    PRIMARY KEY(hostname, companyId, id)
);
CREATE TABLE IF NOT EXISTS modbusServer_by_code (
    code text,
    companyId uuid,
    id uuid,
    PRIMARY KEY(code, companyId, id)
);
