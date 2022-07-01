CREATE TABLE ShortUrl (
    id bigint(32) auto_increment,
    originalurl varchar(1024),
    shorturl varchar(512) NOT NULL,
    hash varchar(256) NOT NULL,
    accesscount int(32) default 0,
    createdon datetime,
    expiredon datetime,
    expired tinyint(1) default 0,
    deleteon datetime,
    deleted tinyint(1) default 0,
    primary key(id),
    unique key(originalurl),
    key `hash_idx` (hash)
);
