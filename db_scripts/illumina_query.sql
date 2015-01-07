SELECT * 
FROM gene.illumina_vcf 
WHERE file LIKE '%|4%' LIMIT 1000 
SELECT * 
FROM (	SELECT split_part(file, ':', 1) AS a 
		FROM gene.illumina_vcf) ALIAS 
WHERE ALIAS LIKE "%3%" 
SELECT pos 
FROM gene.illumina_vcf 
WHERE chrom = 'chr22' INTERSECT 
SELECT pos 
FROM gene.illumina_vcf 
WHERE chrom = 'chr1' 
SELECT chrom, COUNT(chrom) 
FROM staging.illumina_vcfa 
GROUP BY chrom 
SELECT patient_id, chromosome, begin_pos, end_pos, zygosity, vartype, CASE WHEN 
		reference IS NULL THEN '-' ELSE reference END, CASE WHEN allele1Seq LIKE 
		'%?%' THEN 'N' WHEN allele1Seq IS NULL THEN '-' ELSE allele1Seq END, 
	CASE WHEN allele2Seq LIKE '%?%' THEN 'N' WHEN allele2Seq IS NULL THEN '-' 
		ELSE allele2Seq END 
FROM gene.variant_quality 
WHERE reference <> '=' AND chromosome = ? AND vartype != 'ins' AND zygosity != 
	'no-call' 
UNION 
SELECT DISTINCT patient_id, chromosome, begin_pos, end_pos, zygosity, vartype, 
	CASE WHEN reference IS NULL THEN '-' ELSE reference END, CASE WHEN 
		allele1Seq LIKE '%?%' THEN 'N' WHEN allele1Seq IS NULL THEN '-' ELSE 
		allele1Seq END, CASE WHEN allele2Seq LIKE '%?%' THEN 'N' WHEN allele2Seq 
		IS NULL THEN '-' ELSE allele2Seq END 
FROM gene.variant_quality 
WHERE chromosome = ? AND vartype = 'ins' AND zygosity != 'no-call' 
ORDER BY chromosome, begin_pos, allele1Seq, vartype; 
SELECT chrom, pos, REF, alt, split_part(file, ':', 1) AS GT, subject_id, vartype 
FROM gene.illumina_vcf 
WHERE chrom = ? AND alt NOT LIKE '%,%' OR (alt LIKE '%,%' AND LENGTH(split_part( 
	alt,',', 1)) = 1 AND LENGTH(split_part(alt,',', 2)) = 1) 
ORDER BY 1, 2, 4, 7 offset ? LIMIT ?; 


SELECT subject_id, chrom, pos, REF, split_part(alt, ',', 1) AS allele1, 
	split_part(alt, ',', 2) AS allele2, split_part(file, ':', 1) AS GT, alt 
FROM gene.illumina_vcf 
WHERE alt LIKE '%,%' AND (LENGTH(split_part(alt,',', 1)) > 1 OR LENGTH(
	split_part(alt,',', 2)) > 1) 
ORDER BY 2, 3, 5, 6, 7;

select * from gene.illumina_genome limit 200000

select * from transactions where description like '%gene.variant_quality%'

select * from locks


