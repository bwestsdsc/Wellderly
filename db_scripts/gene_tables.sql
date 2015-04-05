CREATE TABLE gene.illumina_vcf2  ( 
	subject_id   	varchar(80),
	chromosome   	varchar(80),
	start_pos    	numeric(37,15),
	reference    	varchar(80),
	alt          	varchar(80),
	rs_id        	varchar(80),
	vartype      	varchar(80),
	mod_reference	varchar(80),
	mod_alt      	varchar(80),
	mod_start_pos	numeric(37,15),
	end_pos      	numeric(37,15),
	qual         	varchar(8000),
	filter       	varchar(8000),
	info         	varchar(8000),
	format       	varchar(8000),
	file         	varchar(8000),
	date_loaded  	date,
	loaded_by    	varchar(80),
primary key (chromosome, mod_start_pos, mod_reference, mod_alt) 
	)
GO
GRANT SELECT, INSERT, UPDATE, DELETE, REFERENCES ON TABLE gene.illumina_vcf2 TO dbadmin WITH GRANT OPTION
GO
GRANT SELECT ON TABLE gene.illumina_vcf2 TO stsi
GO


drop table gene.variant_quality

CREATE TABLE gene.variant_quality  ( 
	patient_id                	varchar(80) NOT NULL DEFAULT 'NA',
	locus                     	int,
	ploidy                    	varchar(80),
	chromosome                	varchar(80) NOT NULL,
	begin_pos                 	int NOT NULL,
	end_pos                   	int NOT NULL,
	zygosity                  	varchar(80),
	varType                   	varchar(80),
	reference                 	varchar(80) NOT NULL,
	allele1Seq                	varchar(80),
	allele2Seq                	varchar(80),
	allele1VarScoreVAF        	int,
	allele2VarScoreVAF        	int,
	allele1VarScoreEAF        	int,
	allele2VarScoreEAF        	int,
	allele1VarQuality         	varchar(80),
	allele2VarQuality         	varchar(80),
	allele1HapLink            	varchar(80),
	allele2HapLink            	varchar(80),
	allele1XRef               	varchar(80),
	allele2XRef               	varchar(80),
	evidenceIntervalId        	varchar(80),
	allele1ReadCount          	int,
	allele2ReadCount          	int,
	referenceAlleleReadCount  	varchar(80),
	totalReadCount            	int,
	allele1Gene               	varchar(80),
	allele2Gene               	varchar(80),
	pfam                      	varchar(80),
	miRBaseId                 	varchar(80),
	repeatMasker              	varchar(80),
	segDupOverlap             	varchar(80),
	relativeCoverageDiploid   	varchar(80),
	calledPloidy              	varchar(80),
	relativeCoverageNondiploid	varchar(80),
	calledLevel               	varchar(80),
	load_date                 	date,
	load_process              	varchar(80),
	PRIMARY KEY(begin_pos,chromosome,end_pos,patient_id,reference)
)
PARTITION BY variant_quality.chromosome
GO
GRANT SELECT, INSERT, UPDATE, DELETE, REFERENCES ON TABLE gene.variant_quality TO dbadmin WITH GRANT OPTION
GO
GRANT SELECT, REFERENCES ON TABLE gene.variant_quality TO stsi
GO

drop table gene.ref_coverage

CREATE TABLE gene.ref_coverage  ( 
	patient_id                    	varchar(80) NOT NULL DEFAULT 'NA',
	chromosome                    	varchar(80) NOT NULL DEFAULT 'NA',
	offset_cov                    	numeric(38,0),
	refScore                      	int,
	uniqueSequenceCoverage        	int,
	weightSumSequenceCoverage     	int,
	gcCorrectedCoverage           	varchar(80),
	grossWeightSumSequenceCoverage	int,
	loaded_by                     	varchar(80),
	load_date                     	date 
	)
partition by patient_id
GO


drop table gene.did_subject_xref

CREATE TABLE gene.did_subject_xref  ( 
	did       	varchar(32) NOT NULL,
	subject_id	varchar(20) NOT NULL 
	)
GO

drop table gene.subject_illumina_xref 

create table gene.subject_illumina_xref (
    subject_id  varchar(20) not null,
    illumina_id varchar
)
GO

drop table gene.depth_of_coverage;

CREATE TABLE gene.depth_of_coverage  ( 
	patient_id               	varchar(80),
	chromosome               	varchar(80),
	begin_pos                	int,
	end_pos                  	int,
	uniqueSequenceCoverage   	numeric(38,0),
	weightSumSequenceCoverage	numeric(38,0),
	gcCorrectedCvg           	numeric(38,0),
	avgNormalizedCoverage    	numeric(38,0),
	loaded_by                	varchar(80),
	load_date                	date 
	)
GO

drop table gene.nondiploid_beta_detail;
 
create table gene.nondiploid_beta_detail(patient_id   varchar,
chr	varchar,
begin_pos	integer,
end_pos integer,	
avgNormalizedCvg	number, 
gcCorrectedCvg	number,
fractionUnique	number,
relativeCvg	number,
calledLevel	varchar, 
calledCNVType	varchar,
levelScore	integer,
CNVTypeScore varchar,
loaded_by   varchar,
load_date   date);
GO

drop table gene.diploid_beta_detail

create table gene.diploid_beta_detail(patient_id   varchar,
chr	varchar,
begin_pos	integer,
end_pos integer,	
avgNormalizedCvg	varchar, 
gcCorrectedCvg	varchar,
fractionUnique	number,
relativeCvg	varchar,
calledPloidy	varchar, 
calledCNVType	varchar,
ploidyScore	integer,
CNVTypeScore varchar,
loaded_by   varchar,
load_date   date);
GO

drop table gene.segments_nondiploid_beta;

create table gene.segments_nondiploid_beta (patient_id varchar,
chr varchar,
begin_pos integer,	
end_pos integer,
avgNormalizedCvg    number,	
relativeCvg number,
calledLevel    number,
calledCNVType   varchar,	
levelScore  number,	
CNVTypeScor varchar,
loaded_by varchar,
load_date date);
GO

drop table gene.segments_diploid_beta;


create table gene.segments_diploid_beta (patient_id varchar,
chr varchar,
begin_pos integer,
end_pos integer,
avgNormalizedCvg    number,
relativeCvg number,
calledPloidy varchar,	
calledCNVType   varchar,
ploidyScore integer,	
CNVTypeScore    integer,
overlappingGene varchar,	
knownCNV varchar,
repeats integer,
loaded_by varchar,
load_date   date)
GO

create table gene.dbnsfp_variants(
chrom   integer,
pos     integer,
ref varchar,
alt varchar(8000),
aaref varchar,
aaalt varchar(8000),
hg18_pos    integer,
genename    varchar,
Uniprot_acc varchar(8000),
Uniport_id varchar(8000),
Uniprot_aapos   varchar(8000),
Interpro_domain varchar(8000),
cds_strand  varchar,
refcondon   varchar,
SLR_test_statisitics    numeric,
codonpos integer,
fold_degenerate integer,
ancestral_allele varchar,
ensemble_geneid varchar,
ensemble_transcriptid   varchar,
aapos   integer,
aapos_SIFT 

select subject_id, count(*) from staging.illumina_vcfa
group by subject_id

select count(*) from gene.illumina_vcf2

truncate table gene.illumina_vcf2

select * from staging.illumina_vcf limit 100 //where ref is null


select distinct subject_id, count(*) from gene.illumina_vcf1
group by subject_id


select count(*) from gene.illumina_vcf2

select  29959857 + 1261

select distinct subject_id from gene.illumina_vcf2

select count(*) from gene.illumina_vcf1 where subject_id = 'HE01036'



DROP FUNCTION gene.GetVariantType(ref Varchar, alt Varchar)
GO


create function gene.GetVariantType(ref varchar, alt varchar) return varchar
as begin  
RETURN (CASE WHEN(length(ref) = 1 and length(alt) = 1 or REGEXP_LIKE(alt, ',')) THEN 'SNP'
            WHEN(length(ref) = length(alt) and Length(ref) > 1) THEN 'INDEL'
            WHEN (length(ref) > length(alt)) THEN 'DEL' 
            when (length(ref) < length(alt)) THEN 'IN'  END); 
                
end;


ALTER TABLE staging.illumina_vcf
	ADD CONSTRAINT illumina_vcf_pk
	PRIMARY KEY (subject_id, chrom, ref, alt, pos)
GO

ALTER TABLE gene.illumina_vcf
	ADD CONSTRAINT illumina_vcf_pk
	PRIMARY KEY (subject_id, chrom, ref, alt, pos)
GO

ALTER TABLE gene.cgi_data
	ADD CONSTRAINT cgi_data_pk
	PRIMARY KEY (patient_id, chromosome, reference, begin_pos)
GO


select analyze_statistics('')

