drop table gene.exome_vcf cascade
GO

CREATE TABLE gene.exome_vcf AS 
SELECT chrom, pos, ref, alt, id, TypeRules(ref,alt)                                                                                                                                                                               
	AS 'vartype', ReferenceRules(TypeRules(ref, alt), ref, alt) AS 'mod_ref', 
	AltRules(TypeRules(ref, alt), ref, alt) AS 'mod_alt', PosRules(TypeRules(ref
	, alt), ref, alt) + pos AS 'mod_start_pos', 
	CASE TypeRules(ref, alt) 
		WHEN 'ins' 
		THEN pos 
		ELSE (PosRules(TypeRules(ref, alt), ref, alt) + LENGTH(ReferenceRules(
		TypeRules(ref, alt), ref, alt)) + pos) 
	END                                                                                                                                                                                                                            
	AS "mod_end_pos", qual, filter, info, now() AS "date_loaded", 'ETL Process' 
	AS "loaded_by" 
FROM staging.exome_vcf
GO

drop table gene.dbsnp_vcf cascade
GO

CREATE TABLE gene.dbsnp_vcf AS 
SELECT chrom, pos, ref, alt, id, TypeRules(ref,alt)                                                                                                                                                                               
	AS 'vartype', ReferenceRules(TypeRules(ref, alt), ref, alt) AS 'mod_ref', 
	AltRules(TypeRules(ref, alt), ref, alt) AS 'mod_alt', PosRules(TypeRules(ref
	, alt), ref, alt) + pos AS 'mod_start_pos', 
	CASE TypeRules(ref, alt) 
		WHEN 'ins' 
		THEN pos 
		ELSE (PosRules(TypeRules(ref, alt), ref, alt) + LENGTH(ReferenceRules(
		TypeRules(ref, alt), ref, alt)) + pos) 
	END                                                                                                                                                                                                                            
	AS "mod_end_pos", qual, filter, info, now() AS "date_loaded", 'ETL Process' 
	AS "loaded_by" 
FROM staging.dbsnp_vcf
GO

drop table gene.one_k_genome_vcf cascade
GO

CREATE TABLE gene.one_k_genome_vcf AS 
SELECT chrom, pos, ref, alt, id, TypeRules(ref,alt)                                                                                                                                                                               
	AS 'vartype', ReferenceRules(TypeRules(ref, alt), ref, alt) AS 'mod_ref', 
	AltRules(TypeRules(ref, alt), ref, alt) AS 'mod_alt', PosRules(TypeRules(ref
	, alt), ref, alt) + pos AS 'mod_start_pos', 
	CASE TypeRules(ref, alt) 
		WHEN 'ins' 
		THEN pos 
		ELSE (PosRules(TypeRules(ref, alt), ref, alt) + LENGTH(ReferenceRules(
		TypeRules(ref, alt), ref, alt)) + pos) 
	END                                                                                                                                                                                                                            
	AS "mod_end_pos", qual, filter, info, now() AS "date_loaded", 'ETL Process' 
	AS "loaded_by" 
FROM staging.one_k_genome_vcf
GO

drop table gene.illumina_vcf cascade
GO

CREATE TABLE gene.illumina_vcf AS 
SELECT chrom, pos, ref, alt, id, TypeRules(ref,alt)                                                                                                                                                                               
	AS 'vartype', ReferenceRules(TypeRules(ref, alt), ref, alt) AS 'mod_ref', 
	AltRules(TypeRules(ref, alt), ref, alt) AS 'mod_alt', PosRules(TypeRules(ref
	, alt), ref, alt) + pos AS 'mod_start_pos', 
	CASE TypeRules(ref, alt) 
		WHEN 'ins' 
		THEN pos 
		ELSE (PosRules(TypeRules(ref, alt), ref, alt) + LENGTH(ReferenceRules(
		TypeRules(ref, alt), ref, alt)) + pos) 
	END                                                                                                                                                                                                                            
	AS "mod_end_pos", qual, filter, info, format, file, now() AS "date_loaded", 'ETL Process' 
	AS "loaded_by" 
FROM staging.illumina_vcf
GO

select count(*) from gene.illumina_vcf3 where vartype = 'ins' limit 500

select * from transactions where description like '%gene.illumina_vcf %'

select CHANGE_CURRENT_STATEMENT_RUNTIME_PRIORITY(45035996279138358, 'high')

select chromosome, begin_pos, end_pos, vartype, reference, allele1Seq, count(allele1Seq), allele2Seq, count(allele2Seq)
from gene.variant_quality where vartype = 'del' and reference is not null
group by  chromosome, begin_pos, end_pos, vartype, reference, allele1Seq, allele2Seq
order by chromosome, begin_pos, end_pos, length(allele1Seq), allele1Seq, length(allele2Seq), allele2Seq limit 5000

select * from gene.variant_quality limit 1000

select chrom, mod_start_pos, mod_end_pos, vartype, mod_ref, mod_alt, count(mod_alt)
from gene.illumina_vcf
group by  chrom, mod_start_pos, mod_end_pos, vartype, mod_ref, mod_alt
order by chrom, mod_start_pos, mod_end_pos, length(mod_alt), mod_alt limit 5000

select chrom, pos, ref, alt, info from gene.illumina_vcf limit 5000

select * from locks;

select count(*) from gene.illumina_vcf  //769837125 
select count(*) from 
gene.illumina_vcf 
where alt like '%,%' and length(split_part(mod_alt, ',', 1)) > 1

select 968122/769837125 


select 1202256/769837125

select chrom, pos, ref, alt, split_part(file, ':', 1) as GT 
				from gene.illumina_vcf where alt like '%,%' 
				and length(split_part(alt,',', 1)) > 1 or length(split_part(alt,',', 2)) > 1 
				order by 1, 2, 4 
				limit 5000;


select * from gene.illumina where 

select subject_id, chrom, pos, ref, split_part(alt, ',', 1) as allele1,  
split_part(alt, ',', 2) as allele2,
TypeRules(ref,split_part(alt, ',', 1)) as vartype1, 
TypeRules(ref,split_part(alt, ',', 2)) as vartype2, 
id, qual, filter, info, format, file
from gene.illumina_vcf where  alt like '%,%' 
and length(split_part(alt,',', 1)) > 1 or length(split_part(alt,',', 2)) > 1 
			
				limit 5000;

select subject_id, chrom, pos, ref, split_part(alt, ',', 1) as allele1, 
				split_part(alt, ',', 2) as allele2, 
				split_part(file, ':', 1) as GT 
				from gene.illumina_vcf where pos = 54720 and  alt like '%,%' 
				and length(split_part(alt,',', 1)) > 1 or length(split_part(alt,',', 2)) > 1 
				order by  2, 3, 5, 6, 7  limit 1300;

select * from gene.illumina_vcf where  alt like '%,%' 
				and length(split_part(alt,',', 1)) 1 or length(split_part(alt,',', 2)) > 1 
order by chrom, pos, alt
limit 100  
 

select chrom, pos, ref, alt, split_part(file, ':', 1) as GT, subject_id, vartype 
				from gene.illumina_vcf where 
				alt not like '%,%' or (alt like '%,%' and length(split_part(alt,',', 1)) = 1 
				and length(split_part(alt,',', 2)) = 1) 
				order by 1, 2, 4, 7   limit 250;

select chrom, pos, ref, alt, split_part(file, ':', 1) as GT, subject_id, vartype 
				from gene.illumina_vcf where chrom = 'chr1'  
                and 
				alt not like '%,%' or (alt like '%,%' and length(split_part(alt,',', 1)) = 1 
				and length(split_part(alt,',', 2)) = 1)  order by 1, 2, 4 limit 2500

drop table gene.vcf_tmp;

create temporary table gene.vcf_tmp (
chrom   varchar2,
pos     integer,
ref     varchar2,
alt     varchar2,
allele1 varchar2,
allele2 varchar2,
org_genotype    varchar2,
vartype    varchar2,
mod_pos integer,
mod_ref varchar2,
mod_allele1 varchar2,
mod_allele2 varchar2,
allele_list varchar2,
new_gt  varchar2);



select subject_id, chrom, pos, ref, split_part(alt, ',', 1) as allele1, 
				split_part(alt, ',', 2) as allele2, 
				split_part(file, ':', 1) as GT, alt 
				from gene.illumina_vcf where chrom = 'chr1'  and (alt like '%,%' 
				and length(split_part(alt,',', 1)) > 1 or length(split_part(alt,',', 2)) > 1) 
				order by  2, 3, 5, 6, 7  limit 500;

select count(*)
				from gene.illumina_vcf where  alt like '%,%' 
				and length(split_part(alt,',', 1)) > 1 or length(split_part(alt,',', 2)) > 1

select 1202256/769837125   

				




