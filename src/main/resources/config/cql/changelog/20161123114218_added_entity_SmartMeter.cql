
CREATE TABLE IF NOT EXISTS smartMeter (
    id uuid,
    serverId uuid,
    companyId uuid,
    code int,
    longcode bigint,
    name text,
    category int,
    longitude double,
    latitude double,
    enable boolean,
    bigEndian boolean,
    startOffset int,
    numberOfRegisters int,
    dataTypes map<text, text>,
    allowDuplicate boolean,
    controlAddress int,
    func int,
    PRIMARY KEY(id, serverId, code)
)WITH CLUSTERING ORDER BY (serverId ASC, code DESC);

CREATE TABLE IF NOT EXISTS smartMeter_by_server_and_code (
    serverId uuid,
    code int,
    id uuid,
    PRIMARY KEY(serverId, code, id)
);

CREATE TABLE IF NOT EXISTS smartMeter_by_company_and_server (
    companyId uuid,
    serverId uuid,
    id uuid,
    PRIMARY KEY(companyId, serverId, id)
);
