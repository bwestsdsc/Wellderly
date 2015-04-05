select subject_id, chrom, pos, ref, split_part(alt, ',', 1) as allele1, 
				 split_part(alt, ',', 2) as allele2, 
				 split_part(file, ':', 1) as GT, alt 
				 from gene.illumina_vcf where (alt like '%,%' 
				 and length(split_part(alt,',', 1)) > 1 or length(split_part(alt,',', 2)) > 1 and split_part(file, ':', 1) like '%|%')
and split_part(file, ':', 1) like '%|%' 
				 order by  2, 3, 5, 6, 7  

select chrom, pos, ref, alt, split_part(file, ':', 1) as GT, subject_id, vartype 
				 from gene.illumina_vcf where chrom = 'chr1' and pos = 10583 and
				 alt not like '%,%' or (alt like '%,%' and length(split_part(alt,',', 1)) = 1 
				 and length(split_part(alt,',', 2)) = 1 and split_part(file, ':', 1) like '%|%') 
and split_part(file, ':', 1) like '%|%'
				 order by 1, 2, 4, 7 

select * from gene.illumina_vcf where
subject_id = 'HE00189'	and chrom = 'chr1' and pos = 49514


 select distinct patient_id, chromosome, begin_pos, end_pos, zygosity, vartype, 
				 case when reference is null then '-' else reference end, 
				 case when allele1Seq like '%?%' then 'N' when allele1Seq is null then '-' else allele1Seq end, 
				 case when allele2Seq like '%?%' then 'N' when allele2Seq is null then '-' else allele2Seq end 
				 from gene.variant_quality where chromosome = 'chr1' and vartype = 'complex' 
				  and zygosity != 'no-call' and length(reference) < length(allele1Seq)
				 order by chromosome, begin_pos, allele1Seq, vartype  limit 5000;


select count(*) from gene.variant_quality where reference is not null and vartype = 'ins' and allele2Seq not like '%?%' 

select * from gene.variant_quality where vartype = 'sub' limit 1000

select * from gene.variant_quality where begin_pos = 194012643 and patient_id = 'HE00389'

select * from transactions where end_timestamp is null limit 100

select close_session('stsi1.sdsc.edu-17917:0xe8548') 

create table gene.cgi_data as select distinct * from gene.variant_quality;

select distinct count(*) from gene.variant_quality


select column_name from columns where table_name = 'variant_quality'

select count(*) from (
select distinct
patient_id,
locus,
ploidy,
chromosome,
begin_pos,
end_pos,
zygosity,
varType,
reference,
allele1Seq,
allele2Seq,
allele1VarScoreVAF,
allele2VarScoreVAF,
allele1VarScoreEAF,
allele2VarScoreEAF,
allele1VarQuality,
allele2VarQuality,
allele1HapLink,
allele2HapLink,
allele1XRef,
allele2XRef,
evidenceIntervalId,
allele1ReadCount,
allele2ReadCount,
referenceAlleleReadCount,
totalReadCount,
allele1Gene,
allele2Gene,
pfam,
miRBaseId,
repeatMasker,
segDupOverlap,
relativeCoverageDiploid,
calledPloidy,
relativeCoverageNondiploid,
calledLevel
from gene.variant_quality) a;

select audit('gene.variant_gene_notation_ref')

select count(*) from gene.variant_gene_notation_ref


select * from transactions where description like '%gene.illumina_vcf%'

select count(*) from gene.cgi_data where chromosome = 'chr22'

select distinct chromosome, count(*) from gene.cgi_data 
group by chromosome
order by chromosome

select * from gene.vcf_tmp

select distinct chrom, count(*) from gene.illumina_vcf 
group by chrom
order by chrom

select * from gene.illumina_vcf order by subject_id, chrom, pos
limit 50

drop table gene.vcf_tmp

select * from gene.vcf_tmp

CREATE TABLE gene.vcf_merged  (
    subject_id  varchar, 
	chrom      	varchar(80),
	pos        	int,
	ref        	varchar(80),
	alt        	varchar(80),
	allele1    	varchar(80),
	allele2    	varchar(80),
	org_gt     	varchar(80),
	vartype    	varchar(80),
	mod_pos    	int,
	mod_ref    	varchar(80),
	mod_allele1	varchar(80),
	mod_allele2	varchar(80),
	allele_list	varchar(80),
	new_gt     	varchar(80) 
	)
GO


select distinct chrom from gene.vcf_merged limit 500

select * from locks

create table walgreen.blood_glucose(
profile_id      int,
activity_date date,
activity_type   varchar,
reading     int,
age         int,
sex varchar);

create table walgreen.blood_pressure(
profile_id      int,
activity_date date,
activity_type   varchar,
systolic    int,
enddiastolic int,
age         int,
sex varchar);

create table walgreen.pulse_ox(
profile_id      int,
activity_date date,
activity_type   varchar,
spo2     int,
age         int,
sex varchar);


drop table walgreen.sleep

create table walgreen.sleep(
profile_id      int,
activity_date date,
activity_type   varchar,
sleep_hours     number,
age         int,
sex varchar);


create table walgreen.tobacco(
profile_id      int,
activity_date date,
activity_type   varchar,
nrt     int,
age         int,
sex varchar);

drop table walgreen.walk_run_cycle

create table walgreen.walk_run_cycle(
profile_id      int,
activity_date date,
activity_type   varchar,
miles     number,
age         int,
sex varchar);

drop table walgreen.weight

create table walgreen.weight(
profile_id      int,
activity_date date,
activity_type   varchar,
lbs     number,
age         int,
sex varchar);

drop table digimed.staged_readings

create table digimed.staged_readings(
reading_time     int,
reading         int/*,
subject_id      varchar,
activity_date date,
activity_type   varchar*/
);

drop table digimed.staged_data

create table digimed.staged_data(
subject_id      varchar,
activity_date date,
activity_type   varchar,
reading_time     number,
reading         number
);


truncate table digimed.staged_readings
select count(*) from digimed.staged_data limit 1000

select count(*) from digimed.staged_readings

insert into digimed.staged_data
select 'Subject_1', '11-4-2013', 'VitalConnect_AccelerometerX', reading_time, reading from digimed.staged_readings

select pos from gene.illumina_vcf where chrom = 'chrX' order by pos offset 1000000 limit 1000


select * from gene.vcf_merged where vartype != 'snp' limit 1000


 select count(*) from digimed.staged_data offset 400000000 limit 1000;

select count from table


select table_name from tables where table_schema = 'walgreen'

alter table digimed.staged_data partition by activity_type

ALTER TABLE digimed.staged_data 
   ADD PRIMARY KEY (subject_id, activity_date, activity_type, reading_time);



alter table digimed.staged_data alter column activity_type set not null

update digimed.staged_readings set subject_id ='Subject_1', activity_date = '11-4-2013', activity_type = 'ARM Acc_AccelerometerX'
 where subject_id is null

select count(*) from walgreen.sleep

select * from walgreen.sleep limit 50



SELECT 
      /*ANCHOR_TABLE_NAME,
       PROJECTION_SCHEMA,*/
      ((SUM(USED_BYTES))/1024/1024/1024)  AS TOTAL_SIZE
  FROM PROJECTION_STORAGE WHERE /*ANCHOR_TABLE_NAME = 'tobacco'
  AND*/ ANCHOR_TABLE_SCHEMA='walgreen'
  //AND PROJECTION_NAME like '%b0'
 //GROUP BY PROJECTION_SCHEMA, ANCHOR_TABLE_NAME;


truncate table walgreen.sleep

12579093

select count(*) from digimed.staged_data where subject_id = 'Subject_11' and activity_date = '11/4/2013'

select count(*) from gene.cgi_data where chromosome = 'chrY'

alter table gene.cgi_data partition by chromosome

alter table gene.illumina_vcf partition by chrom

select distinct begin_pos from gene.cgi_merged where chrom = 'chr22' 
order by begin_pos limit 500

select count(*)
				 from gene.cgi_data where  /*reference <> '=' and*/ chromosome = 'chr22' //and vartype != 'ins' 
				  and zygosity != 'no-call' 
				 Union 
				 select count(*)
				 from gene.cgi_data where chromosome = 'chr22' and vartype = 'ins' 
				  and zygosity != 'no-call' 
				 order by chromosome, begin_pos, allele1Seq, vartype  limit ? offset ?;

select chromosome, count(*) from gene.cgi_data where chromosome
group by chromosome order by chromosome 

select chrom, count(*) from gene.vcf_merged where chrom = 'chrY' limit 500
group by chrom order by chrom

select * from gene.vcf_merged where chrom = 'chrY' limit 500


select chrom from gene.illumina_vcf where chrom = 'chrX' offset 16000000 limit 5000


select chrom, pos, ref, alt, split_part(file, ':', 1) as GT, subject_id, vartype 
				 from gene.illumina_vcf where chrom = 'chrX' and 
				 alt not like '%,%' or (alt like '%,%' and length(split_part(alt,',', 1)) = 1 
				 and length(split_part(alt,',', 2)) = 1)  
				 order by 1, 2, 4, 7 offset 200001 limit 400;

CREATE USER inova identified by 'Remedy5' password expire

grant select on table gene.vcf_merged to inova


select chrom, count(*) from gene.cgi_merged
group by chrom


select chrom, count(*) from gene.vcf_merged
group by chrom


select count(*) from gene.cgi_data where chromosome = 'chr20'

select chromosome, count(*) from gene.variant_quality 
group by chromosome


select count(*) from gene.variant_quality
select export_tables('', 'walgreen.weight')

insert /*+direct*/ into walgreen.readings.blood_glucose
select * from walgreen.blood_glucose

CREATE NETWORK INTERFACE walgreen_inf ON v_wellderly_node0001 WITH '198.202.90.32'

CREATE NETWORK INTERFACE wellderly_inf ON v_walgreen_node0001 WITH '198.202.90.181'

alter database wellderly export on '198.202.90'

ALTER NODE v_wellderly_node0001 EXPORT ON walgreen_inf;

CONNECT TO VERTICA walgreen USER dbadmin PASSWORD '3Nathan$' ON '198.202.90.181',5433;

EXPORT TO VERTICA walgreen.gene.illumina_vcf FROM gene.illumina_vcf;

CONNECT TO VERTICA walgreen USER dbadmin PASSWORD '3Nathan$' ON 'bigonc.sdsc.edu',5433;

CONNECT TO VERTICA wellderly USER dbadmin PASSWORD '3Nathan$' ON 'stsi1.sdsc.edu',5433;

select * from v_monitor.database_backups

drop table 
select count(*), patient_id, did from gene.variant_quality vq, gene.did_subject_xref d
where subject_id = patient_id
group by patient_id, did 
order by 3
GO

select distinct patient_id, count(*) from gene.variant_quality
group by patient_id order by 1


select count(*) from gene.variant_quality where patient_id = 'None'

select * from delete_vectors


select refresh('gene.variant_quality')

delete from gene.variant_quality where patient_id = 'NA'

select subject_id, did, assembly_id from gene.did_subject_xref where subject_id not in (
select distinct patient_id from gene.variant_quality)
and did is not null
order by 3

select * from gene.dbsnp_vcf limit 50



select * //, case when reference is null then '-' else reference end, 
//case when allele1Seq like '%?%' then 'N' when allele1Seq is null then '-' else allele1Seq end, 
//case when allele2Seq like '%?%' then 'N' when allele2Seq is null then '-' else allele2Seq end 
from gene.cgi_data where chromosome = 'chr18' and reference <> '='  and zygosity != 'no-call' 
Union select  chromosome, begin_pos, end_pos, zygosity, vartype, patient_id,case when reference is null then '-' else reference end, 
case when allele1Seq like '%?%' then 'N' when allele1Seq is null then '-' else allele1Seq end, 
case when allele2Seq like '%?%' then 'N' when allele2Seq is null then '-' else allele2Seq end from gene.cgi_data 
where chromosome = 'chr12' and vartype = 'ins'  and zygosity != 'no-call' order by chromosome, begin_pos, allele1Seq, vartype  limit 1 offset 500

select * from gene.cgi_merged limit 10


select make_ahm_now();

select * from delete_vectors

select purge()

drop table gene.cgi_raw

create table gene.cgi_raw as
select distinct * from gene.cgi_data where vartype != 'ref' and zygosity != 'no-call' limit 5000000


select count(*) from gene.cgi_raw




select sv.subject_id, sv.chrom, sv.pos, sv.ref 
from gene.illumina_vcf sv, gene.vcf_merged vm 
where sv.chrom = 'chr22' and sv.chrom = vm.chrom
and sv.subject_id = vm.subject_id and 
sv.pos = vm.pos
and sv.ref = vm.ref
limit 50


select chrom, count(distinct subject_id), count(*) from gene.illumina_vcf where alt != '.' //10534481 
group by chrom

select count(*) from gene.vcf_merged where chrom = 'chr22'

select subject_id, chrom, pos, ref from gene.illumina_vcf where chrom = 'chr22'
and (subject_id, chrom, pos, ref) not in (select subject_id, chrom, pos, ref from gene.vcf_merged where chrom = 'chr22')
limit 50


alter table gene.illumina_vcf1 rename to illumina_vcf

select chrom, pos, ref, alt, split_part(file, ':', 1) as GT, subject_id, vartype 
from gene.illumina_vcf 
where chrom = 'chr22' and pos = 16096678 and alt not like '%,%' or ( chrom = 'chr22' and pos = 16096678 and alt like '%,%' and length(split_part(alt,',', 1)) = 1 
and length(split_part(alt,',', 2)) = 1) order by 1, 2, 4, 7 


select chrom, pos, ref, alt, split_part(file, ':', 1) as GT, subject_id, vartype 
from gene.illumina_vcf where chrom = 'chr22' and pos = 16096678 and alt != '.' and alt not like '%,%' or (chrom = 'chr22' and pos = 16096678 and alt != '.' and alt like '%,%' and length(split_part(alt,',', 1)) = 1 
and length(split_part(alt,',', 2)) = 1) 
and pos = 16096678 order by 1, 2, 4, 7 offset 0 limit 50

select * from gene.illumina_vcf where chrom = 'chr22' and pos = 16096678


select chromosome, count(distinct patient_id), count(*) from gene.cgi_data where
/*(patient_id, chromosome, begin_pos, reference) not in (select patient_id, chrom, begin_pos, ref
from gene.cgi_merged where chrom = 'chr18') and  chromosome = 'chr18'  and */ reference != '=' and zygosity != 'no-call'
group by chromosome

select max(begin_pos) from gene.cgi_data where chromosome = 'chr8'

select begin_pos from gene.cgi_data where chromosome = 'chr8' and reference != '=' and zygosity != 'no-call' offset 400000 limit 1

