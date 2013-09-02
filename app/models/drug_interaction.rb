class DrugInteraction < ActiveRecord::Base

	# attr_accessible	:drug_id
	# attr_accessible	:interaction_drug_id
	# attr_accessible	:description
	# attr_accessible	:name

	belongs_to :drug
	belongs_to :interaction_drug, :primary_key => :drugbank_id, :class_name => 'Drug'

	def self.import_from_drugbank(interactions,drug_id)
		items = []
		interactions.each do |interaction|
			item = self.find_or_create_by_drug_id_and_interaction_drug_id(drug_id, interaction.interaction_drug_id)
			item.update_attributes(interaction.attributes)
			items << item
		end
		items
	end

end
