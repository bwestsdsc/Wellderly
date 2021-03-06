from all_variants_by_patient import all_variants_report

def create_page(patient_id):
    
    nr = all_variants_report()
    my_content = nr.get_records(patient_id)
    my_page =  "<!DOCTYPE HTML PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN'><html lang='en'>\n<head>\n"
    my_page = my_page + "<style>body{font-family:Arial;} h1{font-family:Arial; text-align:center; color:003399;} h2{font-family:Arial; text-align:center; color:003399;} hr{height:60px; background-color:003399; padding:0 border:0} table, td, th{font-family:Arial; border:1px solid blue;border-collapse:collapse;font-size:12px} table{margin-left: auto;margin-right: auto;} th{background-color:003399;color:white;}</style>"
    my_page = my_page + "<h>\n"
    my_page = my_page + "<title>All Variants by Patient Query</title>\n"
    my_page = my_page + "<body>\n<hr>\n<img src='stsi_logo.jpg'><br>\n<h1>All Variants by Patient</h1>\n<h2>Patient ID: "+ patient_id +"</h2><br>\n<table>\n"
    my_page = my_page + "<tr><th>Patient ID</th><th>Gene Ontology ID</th><th>Gene</th><th>Chromosome</th><th>Allele Variant</th><th>Reference</th><th>Begin</th><th>End</th><th>Genome AF</th><th>Wellderly AF</th></tr>\n"
    for record in my_content:
        my_page = my_page + "<tr>"
        for field in record:
            my_page = my_page + "<td>" + str(field) + "</td>"
        my_page = my_page + "</tr>\n"
    my_page = my_page + "</body>\n</html>\n"
    f = open('variant.html', 'w+')
    f.write(my_page)
    
