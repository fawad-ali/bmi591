class Dosage < ActiveRecord::Base

	# attr_accessible :drug_id
	# attr_accessible :form
	# attr_accessible :route
	# attr_accessible :strength

	def self.import_from_drugbank(dosages, drug_id)
		items = []
		dosages.each do |dosage|
			item = self.find_or_create_by_drug_id_and_form(drug_id, dosage.form)
			item.update_attributes(dosage.attributes)
			items << item
		end
		items
	end
end
