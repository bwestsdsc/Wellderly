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