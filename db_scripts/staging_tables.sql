CREATE TABLE staging.sg_advisor
(
  haplotype character varying,
  chromosome character varying,
  begin_pos character varying,
  end_pos character varying,
  vartype character varying,
  reference character varying,
  allele character varying,
  notes character varying,
  gene character varying,
  gene_type character varying,
  location character varying,
  distance character varying,
  coding_impact character varying,
  protein_pos character varying,
  original_aa character varying,
  allele_aa character varying,
  start_stop_dist character varying,
  prop_cons_affected_upstream character varying,
  prop_cons_affected_downstream character varying,
  trunc_prediction character varying,
  conserved46way character varying,
  conserved46wayplacental character varying,
  conserved46wayprimates character varying,
  asw_minallele character varying,
  ceu_minallele character varying,
  chb_minallele character varying,
  chd_minallele character varying,
  gih_minallele character varying,
  jpt_minallele character varying,
  lwk_minallele character varying,
  mex_minallele character varying,
  mkk_minallele character varying,
  tsi_minallele character varying,
  yri_minallele character varying,
  genomes_af character varying,
  wellderly_af325 character varying,
  nhlbi character varying,
  eqtl_genes character varying,
  mirna_bs_influenced character varying,
  mirna_bs_impact character varying,
  mirna_bs_direct character varying,
  mirna_bs_deltag character varying,
  mirna_genomic character varying,
  mirna_folding_deltag character varying,
  mirna_binding_deltag character varying,
  mirna_top_targets_changed character varying,
  splice_site_pred character varying,
  splicing_predictionmaxent character varying,
  ese_sites character varying,
  ess_sites character varying,
  protein_impact_predictionpolyphen character varying,
  protein_impact_probabilitypolyphen character varying,
  protein_impact_predictionsift character varying,
  protein_impact_scoresift character varying,
  protein_domains character varying,
  protein_domains_impactlogre character varying,
  protein_impact_predictioncondel character varying,
  protein_impact_probabilitycondel character varying,
  tf_binding_sites character varying,
  tfbs_deltas character varying,
  omimgene_id_omimgene_association character varying,
  protein_domain_gene_ontology character varying,
  dbsnp_id character varying,
  hgmd_variant_pubmedid character varying,
  hgmd_gene_disease_association character varying,
  genetic_association_database_pubmedid character varying,
  pharmgkb_database_drug character varying,
  inheritance_penetrance character varying,
  severity_treatability character varying,
  cosmic_variant_numsamples character varying,
  cosmic_gene_numsamples character varying,
  mskcc_cancergenes character varying,
  atlas_oncology character varying,
  sanger_cancergenes character varying,
  sanger_germline_cancergenes character varying,
  sanger_network_informed_cancergenes_pval character varying,
  segdup_region character varying,
  gene_symbol character varying,
  drugbank character varying,
  reactome_pathway character varying,
  gene_onotology character varying,
  disease_ontology character varying,
  acmg_score_clinical_disease_entry_explanation character varying,
  acmg_score_research_disease_entry_explanation character varying
)
WITH (
  OIDS=FALSE
);
ALTER TABLE staging.sg_advisor
  OWNER TO postgres;


-- Table: staging.demographics

-- DROP TABLE staging.demographics;

CREATE TABLE staging.demographics
(
patient_id	 character varying,
sample_type	 character varying,
eligibility	 character varying,
elig_comm	 character varying,
enroll_dt	 character varying,
gender	 character varying,
birth_dt	 character varying,
height_ft	 character varying,
height_in	 character varying,
height_cm	 character varying,
weight_lbs	 character varying,
weight_kg	 character varying,
race___1	 character varying,
race___2	 character varying,
race___4	 character varying,
race___5	 character varying,
race___6	 character varying,
race___7	 character varying,
race___3	 character varying,
hisp_ethnicity	 character varying,
other_race_ethn	 character varying,
origin_mat_grmother	 character varying,
origin_mat_grfather	 character varying,
origin_pat_grmother	 character varying,
origin_pat_grfather	 character varying,
mother_age_alive	 character varying,
mother_age_death	 character varying,
mother_cause_death	 character varying,
father_age_alive	 character varying,
father_age_death	 character varying,
father_cause_death	 character varying,
have_siblings	 character varying,
sib1_gender	 character varying,
sib1_age_alive	 character varying,
sib1_age_death	 character varying,
sib1_cause_death	 character varying,
sib2_gender	 character varying,
sib2_age_alive	 character varying,
sib2_age_death	 character varying,
sib2_cause_death	 character varying,
sib3_gender	 character varying,
sib3_age_alive	 character varying,
sib3_age_death	 character varying,
sib3_cause_death	 character varying,
sib4_gender	 character varying,
sib4_age_alive	 character varying,
sib4_age_death	 character varying,
sib4_cause_death	 character varying,
sib5_gender	 character varying,
sib5_age_alive	 character varying,
sib5_age_death	 character varying,
sib5_cause_death	 character varying,
sib6_gender	 character varying,
sib6_age_alive	 character varying,
sib6_age_death	 character varying,
sib6_cause_death	 character varying,
sib7_gender	 character varying,
sib7_age_alive	 character varying,
sib7_age_death	 character varying,
sib7_cause_death	 character varying,
sib8_gender	 character varying,
sib8_age_alive	 character varying,
sib8_age_death	 character varying,
sib8_cause_death	 character varying,
sib9_gender	 character varying,
sib9_age_alive	 character varying,
sib9_age_death	 character varying,
sib9_cause_death	 character varying,
sib10_gender	 character varying,
sib10_age_alive	 character varying,
sib10_age_death	 character varying,
sib10_cause_death	 character varying,
sib11_gender	 character varying,
sib11_age_alive	 character varying,
sib11_age_death	 character varying,
sib11_cause_death	 character varying,
sib12_gender	 character varying,
sib12_age_alive	 character varying,
sib12_age_death	 character varying,
sib12_cause_death	 character varying,
sib13_gender	 character varying,
sib13_age_alive	 character varying,
sib13_age_death	 character varying,
sib13_cause_death	 character varying,
sib14_gender	 character varying,
sib14_age_alive	 character varying,
sib14_age_death	 character varying,
sib14_cause_death	 character varying,
sib15_gender	 character varying,
sib15_age_alive	 character varying,
sib15_age_death	 character varying,
sib15_cause_death	 character varying,
smoking_hist	 character varying,
curr_pk_yrs	 character varying,
former_pk_yrs	 character varying,
curr_alcohol	 character varying,
drinks_wk	 character varying,
exercise_3x	 character varying,
highest_educ	 character varying,
reqs_assist	 character varying,
assistance___1	 character varying,
assistance___2	 character varying,
assistance___3	 character varying,
assistance___4	 character varying,
assistance___5	 character varying,
take_meds	 character varying,
depr_anx	 character varying,
depr_meds___1	 character varying,
depr_meds___2	 character varying,
depr_meds___3	 character varying,
depr_meds___4	 character varying,
osteopor	 character varying,
osteopor_meds___1	 character varying,
osteopor_meds___2	 character varying,
osteopor_meds___3	 character varying,
hrt	 character varying,
hrt_meds___1	 character varying,
hrt_meds___2	 character varying,
hrt_meds___3	 character varying,
hrt_meds___4	 character varying,
hrt_meds___5	 character varying,
gerd	 character varying,
gerd_meds___1	 character varying,
gerd_meds___2	 character varying,
gerd_meds___3	 character varying,
gerd_meds___4	 character varying,
bph	 character varying,
bph_meds___1	 character varying,
bph_meds___2	 character varying,
bph_meds___3	 character varying,
bph_meds___4	 character varying,
osteoarth	 character varying,
osteoarth_meds___1	 character varying,
osteoarth_meds___2	 character varying,
osteoarth_meds___3	 character varying,
osteoarth_meds___4	 character varying,
hypothy	 character varying,
hypothy_meds___1	 character varying,
hypothy_meds___2	 character varying,
hypothy_meds___3	 character varying,
hyperten	 character varying,
hyperten_meds___1	 character varying,
hyperten_meds___2	 character varying,
hyperten_meds___3	 character varying,
hyperten_meds___4	 character varying,
hyperten_meds___5	 character varying,
hyperten_meds___6	 character varying,
hyperten_meds___7	 character varying,
hyperten_meds___8	 character varying,
dyslipid	 character varying,
dyslipid_meds___1	 character varying,
dyslipid_meds___2	 character varying,
dyslipid_meds___3	 character varying,
dyslipid_meds___4	 character varying,
dyslipid_meds___5	 character varying,
blad_con	 character varying,
blad_con_meds___1	 character varying,
blad_con_meds___2	 character varying,
blad_con_meds___3	 character varying,
copd_asthma	 character varying,
copd_ast_meds___1	 character varying,
copd_ast_meds___2	 character varying,
copd_ast_meds___3	 character varying,
copd_ast_meds___4	 character varying,
glaucoma	 character varying,
glaucoma_meds___1	 character varying,
glaucoma_meds___2	 character varying,
glaucoma_meds___3	 character varying,
glaucoma_meds___4	 character varying,
macular_degen	 character varying,
mac_degen_meds___1	 character varying,
mac_degen_meds___2	 character varying,
mac_degen_meds___3	 character varying,
mac_degen_meds___4	 character varying,
mac_degen_meds___5	 character varying,
mac_degen_other_descr	 character varying,
oth_med_cond_comment	 character varying,
med_hist_comment	 character varying,
last_hist_phys	 character varying,
gene_bk_related	 character varying,
gene_bk_rel1	 character varying,
gene_bk_rel1_id	 character varying,
gene_bk_rel2	 character varying,
gene_bk_rel2_id	 character varying,
gene_bk_rel3	 character varying,
gene_bk_rel3_id	 character varying,
gene_bk_rel4	 character varying,
gene_bk_rel4_id	 character varying)
WITH (
  OIDS=FALSE
);
ALTER TABLE staging.demographics
  OWNER TO postgres;
  
create table demographfics (
patient_id	character varying,
sample_type	integer,
eligibility	integer,
elig_comm	integer,
enroll_dt	date,
gender	integer,
birth_dt	date,
height_ft	integer,
height_in	integer,
height_cm	integer,
weight_lbs	integer,
weight_kg	integer,
origin_mat_grmother	character varying,
origin_mat_grfather	character varying,
origin_pat_grmother	character varying,
process_id	character varying,
load_date	date)

ALTER TABLE staging.sg_advisor ALTER COLUMN ACMG_Score_Clinical_Disease_Entry_Explanation  DROP NOT NULL;


create table staging.variant_quality(
locus	character varying,
ploidy	character varying,	
allele	character varying,	
chromosome	character varying,	
begin_pos	character varying,	
end_pos	character varying,
varType	character varying,	
reference	character varying,	
alleleSeq	character varying,	
varScoreVAF	character varying,	
varScoreEAF	character varying,	
varQuality	character varying,	
hapLink	character varying,	
xRef	character varying)

create table staging.clinvar
(vartype    varchar,
varnotes    varchar);


create table staging.dbsnp_vcf(
chrom   varchar not null,
pos integer not null,
id varchar,
ref varchar,
alt varchar,
qual varchar,
filter varchar,
info varchar);


drop table staging.dbnsfp_variants

create table staging.dbnsfp_variants(
chrom varchar not null,
pos varchar not null,
ref	varchar,
alt varchar,
aaref varchar,	
aaalt   varchar,	
hg18_pos varchar,	
genename varchar,
Uniprot_acc	varchar,
Uniprot_id	varchar,
Uniprot_aapos	varchar,
Interpro_domain	varchar,
cds_strand	varchar,
refcodon	varchar,
SLR_test_statistic 	varchar,
codonpos	varchar,
fold_degenerate varchar,
Ancestral_allele    varchar,	
Ensembl_geneid	varchar,
Ensembl_transcriptid	varchar,
aapos	varchar,
aapos_SIFT	varchar,
aapos_FATHMM	varchar,
SIFT_score	varchar,
SIFT_converted_rankscore	varchar,
SIFT_pred	varchar,
Polyphen2_HDIV_score	varchar,
Polyphen2_HDIV_rankscore	varchar,
Polyphen2_HDIV_pred	varchar,
Polyphen2_HVAR_score	varchar,
Polyphen2_HVAR_rankscore	varchar,
Polyphen2_HVAR_pred	varchar,
LRT_score	varchar,
LRT_converted_rankscore	varchar,
LRT_pred	varchar,
MutationTaster_score	varchar,
MutationTaster_converted_rankscore	varchar,
MutationTaster_pred	varchar,
MutationAssessor_score	varchar,
MutationAssessor_rankscore	varchar,
MutationAssessor_pred	varchar,
FATHMM_score	varchar,
FATHMM_rankscore	varchar,
FATHMM_pred	varchar,
RadialSVM_score	varchar,
RadialSVM_rankscore	varchar,
RadialSVM_pred	varchar,
LR_score    varchar,	
LR_rankscore varchar,
LR_pred	varchar,
Reliability_index varchar,
VEST3_score	varchar,
VEST3_rankscore	varchar,
CADD_raw	varchar,
CADD_raw_rankscore	varchar,
CADD_phred	varchar,
GERP_NR	varchar,
GERP_RS	varchar,
GERP_RS_rankscore	varchar,
phyloP46way_primate varchar,
phyloP46way_primate_rankscore	varchar,
phyloP46way_placental	varchar,
phyloP46way_placental_rankscore	varchar,
phyloP100way_vertebrate	varchar,
phyloP100way_vertebrate_rankscore	varchar,
phastCons46way_primate	varchar,
phastCons46way_primate_rankscore	varchar,
phastCons46way_placental	varchar,
phastCons46way_placental_rankscore	varchar,
phastCons100way_vertebrate	varchar,
phastCons100way_vertebrate_rankscore	varchar,
SiPhy_29way_pi	varchar,
SiPhy_29way_logOdds	varchar,
SiPhy_29way_logOdds_rankscore	varchar,
LRT_Omega	varchar,
UniSNP_ids	varchar,
a1000Gp1_AC varchar,
a1000Gp1_AF	varchar,
a1000Gp1_AFR_AC	varchar,
a1000Gp1_AFR_AF	varchar,
a1000Gp1_EUR_AC	varchar,
a1000Gp1_EUR_AF	varchar,
a1000Gp1_AMR_AC	varchar,
a1000Gp1_AMR_AF	varchar,
a1000Gp1_ASN_AC	varchar,
a1000Gp1_ASN_AF	varchar, 
ESP6500_AA_AF	varchar,
ESP6500_EA_AF   varchar);

create table staging.dbNSFP_gene(
Gene_name varchar,
Ensembl_gene varchar,
chr varchar,
Gene_old_names varchar,
Gene_other_names  varchar,
Uniprot_acc varchar,
Uniprot_id  varchar,
Entrez_gene_id  varchar,
CCDS_id varchar,
Refseq_id   varchar,
ucsc_id varchar,
MIM_id  varchar,
Gene_full_name  varchar,
Pathway_Uniprot varchar,
Pathway_ConsensusPathDB varchar,
Function_description    varchar,
Disease_description varchar,
MIM_phenotype_id    varchar,
MIM_disease varchar,
Trait_association   varchar,
GO_Slim_biological_process  varchar,
GO_Slim_cellular_component  varchar,
GO_Slim_molecular_function  varchar,
Expression varchar,
Expression_GNF  varchar,
Interactions_IntAct varchar,
Interactions_BioGRID    varchar,
Interactions_ConsensusPathDB    varchar,
P_HI    varchar,
P_rec   varchar,
Known_rec_info varchar,
Essential_gene  varchar)

drop table staging.illumina_vcf

CREATE TABLE staging.illumina_vcf  ( 
    subject_id  varchar(20),
	chrom 	varchar(80) NOT NULL,
	pos   	int NOT NULL,
	id    	varchar(80),
	ref   	varchar(80),
	alt   	varchar(80),
	qual  	varchar(8000),
	filter	varchar(8000),
	info  	varchar(8000), 
    format  varchar(8000),
    file    varchar(8000)
	)
GO

drop table staging.one_k_genome_vcf

CREATE TABLE staging.one_k_genome_vcf  ( 
	chrom 	varchar(80) NOT NULL,
	pos   	int NOT NULL,
	id    	varchar(80),
	ref   	varchar(80),
	alt   	varchar(80),
	qual  	varchar(8000),
	filter	varchar(8000),
	info  	varchar(8000)
	)
GO

CREATE TABLE staging.exome_vcf  ( 
	chrom 	varchar(80) NOT NULL,
	pos   	int NOT NULL,
	id    	varchar(80),
	ref   	varchar(80),
	alt   	varchar(80),
	qual  	varchar(8000),
	filter	varchar(8000),
	info  	varchar(8000)
	)
GO

create table staging.clinvar{ 
release_type    varchar,
release_date    date,
clinvar_id      numeric,
record_status   varchar,
title   varchar,
ref_assertion_id    numeric,
ref_assertion_date_created   date,
ref_assertion_date_updated  date,
accession_id numeric, 
accession_date_created  date,
accesion_date_updated   date,
review_status   varchar,
description varchar,
assertion_type  varchar,

ReleaseSet {'Type': 'full', 'Dated': '2014-08-07'}
ClinVarSet {'ID': '1738117'}
RecordStatus {}
Title {}
ReferenceClinVarAssertion {'ID': '86824', 'DateCreated': '2012-08-13', 'DateLastUpdated': '2014-04-18'}
ClinVarAccession {'Type': 'RCV', 'Version': '1', 'DateUpdated': '2014-04-19', 'Acc': 'RCV000029321'}
RecordStatus {}
ClinicalSignificance {'DateLastEvaluated': '2011-08-18'}
ReviewStatus {}
Description {}
Assertion {'Type': 'variation to disease'}
AttributeSet {}
Attribute {'Type': 'ModeOfInheritance', 'integerValue': '483'}
ObservedIn {}
Sample {}
Origin {}
Species {'TaxonomyId': '9606'}
AffectedStatus {}
Method {}
Purpose {}
MethodType {}
ObservedData {'ID': '1502787'}
Attribute {'Type': 'VariantAlleles', 'integerValue': '1'}
MeasureSet {'ID': '35673', 'Type': 'Variant'}
Measure {'ID': '44338', 'Type': 'single nucleotide variant'}
Name {}
ElementValue {'Type': 'preferred name'}
AttributeSet {}
Attribute {'Type': 'HGVS, non-validated'}
CytogeneticLocation {}
MeasureRelationship {'Type': 'variant in gene'}
Name {}
ElementValue {'Type': 'Preferred'}
Symbol {}
ElementValue {'Type': 'Preferred'}
SequenceLocation {'Assembly': 'GRCh37', 'Chr': '2', 'Accession': 'NC_000002.11', 'start': '73612885', 'stop': '73837045', 'Strand': '+'}
SequenceLocation {'Assembly': 'GRCh38', 'Chr': '2', 'Accession': 'NC_000002.12', 'start': '73385757', 'stop': '73609918', 'Strand': '+'}
XRef {'ID': '7840', 'DB': 'Gene'}
XRef {'DB': 'OMIM', 'ID': '606844', 'Type': 'MIM'}
Name {}
ElementValue {'Type': 'preferred name'}
TraitSet {'ID': '8589', 'Type': 'Disease'}
Trait {'ID': '16502', 'Type': 'Disease'}
Name {}
ElementValue {'Type': 'Preferred'}
XRef {'ID': 'Cardiomyopathy/1090', 'DB': 'Genetic Alliance'}
XRef {'ID': 'HP:0001638', 'DB': 'Human Phenotype Ontology'}
XRef {'ID': '85898001', 'DB': 'SNOMED CT'}
Symbol {}
ElementValue {'Type': 'Preferred'}
AttributeSet {}
Attribute {'Type': 'mode of inheritance'}
XRef {'ID': 'GTR000500688', 'DB': 'Ambry Genetics'}
XRef {'ID': 'C0878544', 'DB': 'MedGen'}
ClinVarAssertion {'ID': '56117'}
ClinVarSubmissionID {'title': 'ALMS1:c.11869+18G>A and Cardiomyopathy', 'submitter': 'LabCorp', 'localKey': 'ALMS1_11869+18G_A_111110'}
ClinVarAccession {'Type': 'SCV', 'OrgID': '500026', 'Version': '1', 'DateUpdated': '2014-04-19', 'Acc': 'SCV000051967'}
RecordStatus {}
ClinicalSignificance {'DateLastEvaluated': '2011-08-18'}
ReviewStatus {}
Description {}
CustomAssertionScore {'Value': '0', 'Type': 'QOD'}
CustomAssertionScore {'Value': 'NA', 'Type': 'aFXN'}
CustomAssertionScore {'Value': '0', 'Type': 'pFXN'}
CustomAssertionScore {'Value': 'NA', 'Type': 'pbGP'}
CustomAssertionScore {'Value': 'NA', 'Type': 'tbGP'}
Assertion {'Type': 'variation to disease'}
AttributeSet {}
Attribute {'Type': 'ModeOfInheritance'}
ObservedIn {}
Sample {}
Origin {}
Tissue {}
Species {'TaxonomyId': '9606'}
AffectedStatus {}
Method {}
Purpose {}
MethodType {}
ObservedData {}
Attribute {'Type': 'VariantAlleles'}
MeasureSet {'Type': 'Variant'}
Measure {'Type': 'single nucleotide variant'}
AttributeSet {}
Attribute {'Type': 'HGVS'}
AttributeSet {}
Attribute {'Type': 'Description'}
MeasureRelationship {'Type': 'variant in gene'}
Symbol {}
ElementValue {'Type': 'Preferred'}
TraitSet {'Type': 'Disease'}
Trait {'Type': 'Disease'}
Name {}
ElementValue {'Type': 'Preferred'}
Symbol {}
ElementValue {'Type': 'Preferred'}

from '/localdisk/exome_vcf/ESP6500SI-V2-SSA137.updatedProteinHgvs.chr6.snps_indels.vcf' 

select make_ahm_now()

select * from delete_vectors

select purge_projection('gene.variant_quality_DBD_1_seg_load_notation_b0')

select refresh('gene.variant_quality')

select  purge()

select count(*) from gene.cgi_merged where chrom = 'chr18' and allele_list is null

select * from gene.cgi_merged where chrom = 'chrX' limit 500

HE00214 chr18 42296341

select * from gene.cgi_merged where mod_genotype isnull limit 50


update gene.variant_quality set patient_id = 'HE00552', load_date = to_date('2015-02-26', 'dd/mm/yyy'), load_process ='ETL Process' where patient_id = 'NA'


