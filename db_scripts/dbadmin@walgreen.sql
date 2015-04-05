create schema readings;

CREATE TABLE readings.blood_glucose
(
    profile_id int,
    activity_date date,
    activity_type varchar(80),
    reading int,
    age int,
    sex varchar(80)
);


CREATE TABLE readings.blood_pressure
(
    profile_id int,
    activity_date date,
    activity_type varchar(80),
    systolic int,
    enddiastolic int,
    age int,
    sex varchar(80)
);

select * from nodes;

CREATE NETWORK INTERFACE wellderly_inf ON v_walgreen_node0001 WITH '198.202.90.181'


ALTER NODE v_walgreen_node0001 EXPORT ON wellderly_inf;

EXPORT TO VERTICA walgreen.readings.blood_glucose FROM walgreen.blood_glucose;

CONNECT TO VERTICA wellderly USER dbadmin PASSWORD '3Nathan$' ON 'stsi1.sdsc.edu',5433;

EXPORT TO VERTICA walgreen.gene.variant_quality FROM gene.variant_quality;

EXPORT TO VERTICA walgreen.readings.blood_pressure FROM walgreen.blood_pressure;


select * from readings.blood_pressure

CREATE TABLE readings.pulse_ox
(
    profile_id int,
    activity_date date,
    activity_type varchar(80),
    spo2 int,
    age int,
    sex varchar(80)
);

CREATE TABLE readings.sleep
(
    profile_id int,
    activity_date date,
    activity_type varchar(80),
    sleep_hours numeric(38,0),
    age int,
    sex varchar(80)
);

CREATE TABLE readings.tobacco
(
    profile_id int,
    activity_date date,
    activity_type varchar(80),
    nrt int,
    age int,
    sex varchar(80)
);


CREATE TABLE readings.walk_run_cycle
(
    profile_id int,
    activity_date date,
    activity_type varchar(80),
    miles numeric(38,0),
    age int,
    sex varchar(80)
);

CREATE TABLE readings.weight
(
    profile_id int,
    activity_date date,
    activity_type varchar(80),
    lbs numeric(38,0),
    age int,
    sex varchar(80)
);

select count(*) from readings.tobacco;

select * from transactions limit 50

select profile_id, activity_date, activity_type,  age, sex, reading, '' from readings.blood_glucose
union
select profile_id, activity_date, activity_type, age, sex, systolic, to_char(enddiastolic) from readings.blood_pressure
limit 50

select table_name, column_name, data_type from columns where table_schema = 'readings'

select profile_id as ID, activity_date as Date, sex, age, count(age) as Count, systolic as 'SBP', enddiastolic as 'DBP'
from readings.blood_pressure group by profile_id, activity_date, age, systolic, enddiastolic, sex order by 1,2,3 limit 50


select count(*) from gene.illumina_vcf


drop table gene.illumina_vcf

CREATE TABLE gene.illumina_vcf  ( 
	subject_id   	varchar(20) NOT NULL,
	chrom        	varchar(80) NOT NULL,
	pos          	int NOT NULL,
	ref          	varchar(80) NOT NULL,
	alt          	varchar(80) NOT NULL,
	id           	varchar(80),
	vartype      	varchar(5),
	mod_ref      	varchar(25),
	mod_alt      	varchar(80),
	mod_start_pos	int,
	mod_end_pos  	int,
	qual         	varchar(8000),
	filter       	varchar(8000),
	info         	varchar(8000),
	format       	varchar(8000),
	file         	varchar(8000),
	date_loaded  	timestamptz,
	loaded_by    	varchar(11),
	PRIMARY KEY(alt,chrom,pos,ref,subject_id)
)
PARTITION BY illumina_vcf.chrom
GO

select * from gene.illumina_vcf

CONNECT TO VERTICA wellderly user dbadmin password '3Nathan$' ON 'stsi1', 5433;

truncate table well_test.cgi_data

select count(*) from well_test.cgi_data

grant usage on schema well_test to dbadmin

truncate table well_test.cgi_data

drop table well_test.cgi_data


copy well_test.cgi_data 
from vertica wellderly.gene.cgi_data limit 5000000
GO



copy well_test.cgi_data
from vertica wellderly.gene.cgi_raw
GO

truncate table well_test.cgi_data


create table well_test.cgi_data as select * from gene.cgi_raw
GO

copy well_test.cgi_data from vertica wellderly.gene.cgi_raw

select count(*) from well_test.cgi_merged


select * from well_test.cgi_merged where genotype = 'null' limit 1000

select * from well_test.cgi_data where patient_id = 'HE00001' and chromosome = 'chr1' and begin_pos = 192090801

select * from well_test.cgi_merged limit 1000 offset 300001