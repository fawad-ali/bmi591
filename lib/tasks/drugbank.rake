require 'net/http'
require 'drugbank'

namespace :drugbank do

	namespace :import do
	
        desc "Imports all drugbank records using fresh data."
        task :all => :environment do
          #TODO: make this a variable
          importer = Drugbank::Import.new('http://www.drugbank.ca/system/downloads/current/drugbank.xml.zip')
          importer.import(){|drugs|  ::Drug.import_from_drugbank(drugs) }
        end
		
	end	
	
end