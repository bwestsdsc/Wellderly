INSERT 
INTO GENE.VARIANT_QUALITY 
	(
		PATIENT_ID, LOCUS, PLOIDY, ALLELE, CHROMOSOME, BEGIN_POS, END_POS, 
		VARTYPE, REFERENCE, ALLELESEQ, VARSCOREVAF, VARSCOREEAF, VARQUALITY, 
		HAPLINK, XREF, LOAD_DATE, LOAD_PROCESS 
	)
SELECT PATIENT_ID, CAST(LOCUS AS NUMERIC), PLOIDY, ALLELE, CHROMOSOME, CAST(
	BEGIN_POS AS NUMERIC), CAST(END_POS AS NUMERIC), VARTYPE, REFERENCE, 
	ALLELESEQ, CAST(VARSCOREVAF AS NUMERIC), CAST(VARSCOREEAF AS NUMERIC), 
	VARQUALITY, CAST(HAPLINK AS NUMERIC), XREF, SYSDATE, 'ETL process' 
FROM STAGING.VARIANT_QUALITY;
	
//ETL FOR CLINVAR DATA
	
INSERT 
INTO GENE.CLINVAR 
SELECT SPLIT_PART(VARTYPE, '_', 1) AS CHROMOSOME, 
    CAST(SPLIT_PART(VARTYPE, '_', 2) AS NUMERIC) AS START_POS, 
    CAST(SPLIT_PART(VARTYPE, '_', 3) AS NUMERIC) AS END_POS, 
    SPLIT_PART(VARTYPE, '_', 4) AS VARIANT_TYPE, 
    SPLIT_PART(VARTYPE, '_', 5) AS REF, 
    SPLIT_PART(VARTYPE, '_', 6) AS ALT, 
    SPLIT_PART(VARNOTES, '~', 1) AS OUTCOME, 
    SPLIT_PART(VARNOTES, '~', 2) AS PATHOGENICITY, 
    SPLIT_PART(VARNOTES, '~', 3) AS ACCESSION_NUMBER, 
    SPLIT_PART(SPLIT_PART(VARNOTES, '~', 4), '///', 1) AS ID, 
    SPLIT_PART(VARNOTES, '///', 2) AS NOTES, NOW(), 
	'ETL Process' 
FROM STAGING.CLINVAR