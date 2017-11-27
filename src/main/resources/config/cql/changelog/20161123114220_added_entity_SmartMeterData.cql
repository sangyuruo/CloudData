
CREATE TABLE IF NOT EXISTS smartMeterData (
    meterId uuid,
    id timeuuid,
    companyId uuid,
    name text,
    serverId uuid,
    hostname text,
    serverCode text,
    ip text,
    code int,
    category int,
    ymd int,
    hour int,
    minute int,
    sec int,
    data map<text, float>,
    auxiliary map<text, text>,
    PRIMARY KEY(meterId, id, companyid)
)WITH CLUSTERING ORDER BY (id DESC);
