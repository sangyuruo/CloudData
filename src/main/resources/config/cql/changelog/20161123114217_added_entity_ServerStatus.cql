
CREATE TABLE IF NOT EXISTS serverStatus (
    serverId uuid,
    id timeuuid,
    lastUpdate timestamp,
    createDate timestamp,
    lastState int,
    success int,
    total int,
    PRIMARY KEY(serverId, id)
)WITH CLUSTERING ORDER BY (id DESC);

