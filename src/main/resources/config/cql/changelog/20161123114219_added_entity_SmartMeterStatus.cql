
CREATE TABLE IF NOT EXISTS smartMeterStatus (
    meterId uuid,
    id timeuuid,
    lastUpdate timestamp,
    createDate timestamp,
    lastState int,
    lastRetryDate timestamp,
    lastRetryLevel int,
    retryCount int,
    success int,
    total int,
    switchStatus int,
    volume float,
    PRIMARY KEY(meterId, id)
)WITH CLUSTERING ORDER BY (id DESC);
