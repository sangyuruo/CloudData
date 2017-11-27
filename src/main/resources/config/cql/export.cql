# export
copy smartmeterdata(meterid, id, companyId, serverId, hostname, serverCode, ip, code, category, ymd, hour, minute, sec, data) to 'smartmeterdata.csv' ;

# import
copy smartmeterdata(meterid, id, companyId, serverId, hostname, serverCode, ip, code, category, ymd, hour, minute, sec, data) from 'smartmeterdata.csv' ;


copy modbusServer(id, companyId, hostname,code, ip, port, enable, model,encapsulated,keepAlive,requestTimeout,replyTimeout) to 'modbusserver.csv' ;

copy modbusServer(id, companyId, hostname,code, ip, port, enable, model,encapsulated,keepAlive,requestTimeout,replyTimeout) from 'modbusserver.csv' ;



copy smartmeter(id,serverId,companyId,  code,func,longcode,name,category,longitude,latitude,enable, bigEndian,startOffset,numberOfRegisters,dataTypes,allowDuplicate,controlAddress) to 'smartmeter.csv' ;

copy smartmeter(id,serverId,companyId, code,func,longcode,name,category,longitude,latitude,enable, bigEndian,startOffset,numberOfRegisters,dataTypes,allowDuplicate,controlAddress) from 'smartmeter.csv' ;


#add column
alter table smartmeterdata add auxiliary map<text,text>;
#drop column
alter table smartmeterdata drop auxiliary;

#query

select * from smartmeterdata where meterid=fd614c7d-b28d-4b60-b5f9-f5c391943ab5 and id>minTimeuuid('2017-07-31 00:00:00+0000') and id<maxTimeuuid('2017-07-31 00:00:00+0000') limit 2;
