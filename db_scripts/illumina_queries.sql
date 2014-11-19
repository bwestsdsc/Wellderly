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

