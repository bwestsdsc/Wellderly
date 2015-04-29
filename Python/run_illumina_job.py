
from optparse import OptionParser
import subprocess

def main():
    parser = OptionParser()
    parser.add_option("-f", "--file", dest="filename")
    parser.add_option("-c", "--chromosome", dest="chrom")
    parser.add_option("-s", "--start position", dest="start")
    parser.add_option("-t", "--total amount", dest="total")
    (options) = parser.parse_args()
      
    run_job(options.filename, options.chrom, options.start, options.total)
    
def run_job(filename, chrom, start, total):
    start = int(start)
    total = int(total)
    
    while start < total:
        job = "sudo nohup /usr/java/jdk1.8.0_25/bin/java -jar illumina_rules.jar " + filename + " " + chrom + " " + str(start) + " 1000000"
        print (job)
        subprocess.call(job, shell=True)
        start = start + 1000000

if __name__ == "__main__":
                       
    main()