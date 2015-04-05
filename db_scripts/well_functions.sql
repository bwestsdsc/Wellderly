
drop library wellderly_extensions cascade 
GO
CREATE LIBRARY wellderly_extensions AS '/localdisk/java/wellderly_extensions/extensions.jar' language 'Java'
GO
CREATE FUNCTION ReferenceRules AS LANGUAGE 'Java' NAME 'edu.sdsc.extensions.ReferenceRulesFactory' LIBRARY wellderly_extensions;
GO
CREATE FUNCTION AltRules AS LANGUAGE 'Java' NAME 'edu.sdsc.extensions.AltRulesFactory' LIBRARY wellderly_extensions;
GO
CREATE FUNCTION PosRules AS LANGUAGE 'Java' NAME 'edu.sdsc.extensions.PosRulesFactory' LIBRARY wellderly_extensions;
GO
CREATE FUNCTION TypeRules AS LANGUAGE 'Java' NAME 'edu.sdsc.extensions.TypeRulesFactory' LIBRARY wellderly_extensions;
GO


ALTER TABLE gene.cgi_data
	ADD CONSTRAINT cgi_pk
	PRIMARY KEY (chromosome, begin_pos, end_pos, patient_id, reference)
GO

select * from gene.exome_vcf limit 1000

alter table gene.cgi_data partition by chromosome

select patient_id, chromosome, begin_pos from gene.cgi_data
intersect
select patient_id, chrom, begin_pos from gene.cgi_merged

SELECT SET_CONFIG_PARAMETER('JavaBinaryForUDx','/usr/java/jdk1.8.0_25/bin/java'); 

select count(*) from gene.cgi_data where chromosome = 'chr21' and vartype != 'ref' and zygosity = 'no-call'

select min(begin_pos)/* chromosome, begin_pos, end_pos, zygosity, vartype,  patient_id,case when reference is null then '-' else reference end, 
case when allele1Seq like '%?%' then 'N' when allele1Seq is null then '-' else allele1Seq end, 
case when allele2Seq like '%?%' then 'N' when allele2Seq is null then '-' else allele2Seq end */
from gene.cgi_data where chromosome = 'chr21' and reference <> '=' and vartype not in ('ins', 'ref') and zygosity != 'no-call' 
Union 
select  max(begin_pos) /*chromosome, begin_pos, end_pos, zygosity, vartype, patient_id,
case when reference is null then '-' else reference end, 
case when allele1Seq like '%?%' then 'N' when allele1Seq is null then '-' else allele1Seq end, 
case when allele2Seq like '%?%' then 'N' when allele2Seq is null then '-' else allele2Seq end */
from gene.cgi_data where chromosome = 'chr21' and vartype = 'ins' and zygosity != 'no-call' 
order by chromosome, begin_pos, allele1Seq, vartype limit 35000000 offset 1000000



CREATE TABLE gene.cgi_mergedb  ( 
	patient_id  	varchar(80),
	chrom       	varchar(80),
	begin_pos   	int,
	ref         	varchar(80),
	allele1     	varchar(80),
	allele2     	varchar(80),
	genotype    	varchar(80),
	vartype     	varchar(80),
	mod_pos     	int,
	mod_ref     	varchar(80),
	mod_alt1    	varchar(80),
	mod_alt2    	varchar(80),
	allele_list 	varchar(80),
	mod_genotype	varchar(80) 
	)
GO

select count(*) from (
select distinct * from gene.cgi_mergeda where chrom = 'chr21' and vartype = 'ref') as a

 and allele_list = 'null'//9411197 34201349

select count(*) from gene.cgi_mergeda where allele_list = 'null'

select max(begin_pos) from gene.cgi_data where chromosome = 'chr21' and reference <> '=' and zygosity <> 'no-call' //48119886


select * from gene.cgi_merged1 where chrom = 'chr21'
minus
select * from gene.cgi_mergeda
limit 5000

select * from gene.cgi_mergeda where patient_id = 'HE00008' and chrom = 'chr21' and begin_pos = 19663154


select a.patient_id, count(a.patient_id), b.patient_id, count(b.patient_id) from gene.cgi_merged1 a, gene.cgi_mergeda b
where a.patient_id = b.patient_id 
and a.chrom = b.chrom
and a.begin_pos = b.begin_pos
and  a.chrom = 'chr21'
group by a.patient_id, b.patient_id 
limit 50

select * from gene.cgi_mergeda where vartype = 'ref'
