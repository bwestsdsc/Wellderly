'''
Created on Aug 12, 2014

@author: bwest
'''

from datetime import date
from well_conn import well_conn
from subprocess import call
import sys
import shutil
import os
import re

class IlluminaFiles():
   
    def __init__(self, my_dir):    
         self.my_dir = my_dir
         my_files = self.getFiles(my_dir)
         
    def getFiles(self,my_dir):
         print(my_dir)
         my_files = []
         part_no = ''
         try:
             for root, dir, files in os.walk(my_dir):
                 for file in files:
                     part_no = file.split('-', -1)
                     part2 = part_no[1].split('.', -1)
                     my_part = part_no[0] + '-' + part2[0]
                     patient_id = self.get_patient(my_part)
                     print(my_part, patient_id)
                     self.upload_file(file, patient_id)
                 #self.process_files(dir_path, patient_id)
                                                  
         except:
            print "Unexpected error:", sys.exc_info()[0]
            raise
                
         return my_files
     
    def upload_file(self, my_file, patient_id):
        load_date = date.today()
        load_process = 'ETL Process'
        
        conn_obj = well_conn()
        conn = conn_obj.db_conn()
        cur = conn.cursor()
        
        copy_sql = "copy staging.illumina_genome(chrom, pos, id, ref, alt, qual, filter, info, format, file) " +\
                    "from " + my_dir + my_file +\
                    "' GZIP DELIMITER E'\t' skip 112 rejected data '/tmp/rejected_data' EXCEPTIONS '/tmp/exceptions' " +\
                    "trailing nullcols DIRECT;"
                    
        update_sql = "update staging.illumina_genome set subject_id = " + patient_id + " and data_file = '" + my_file + "'  where subject_id is null";
       
        try:
            cur.execute(copy_sql)
            print("Data loaded for " + patient_id)
            conn.commit()      
            cur.execute(update_sql)
            print("Data updated for " + patient_id)
            conn.commit()
        except:
            print "Unexpected error:", sys.exc_info()[0]
            raise
    
    def get_patient(self, gs_id):
         
         conn_obj = well_conn()
         conn = conn_obj.db_conn()
         cur = conn.cursor()
         did_list = gs_id.split(',')
         sql = "select subject_id from gene.did_subject_xref where illumina_ref = '" + gs_id + "'"
         cur.execute(sql)
         subject_id = cur.fetchone()
        
         return str(subject_id).strip('[u]')
     
my_dir = '/Users/bwest/Documents/illumina_genome/'     
        
my_uploads = IlluminaFiles(my_dir)

   